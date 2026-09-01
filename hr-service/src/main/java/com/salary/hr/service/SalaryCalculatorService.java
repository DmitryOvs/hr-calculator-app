package com.salary.hr.service;

import com.salary.hr.common.dto.MonthlyWorkSummaryDto;
import com.salary.hr.persistence.entity.Employee;
import com.salary.hr.persistence.enums.WorkMethod;
import com.salary.hr.persistence.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class SalaryCalculatorService {

    private final EmployeeRepository employeeRepository;
    private static final BigDecimal DAILY_ALLOWANCE_RATE = BigDecimal.valueOf(400.00); // Суточные

    @Transactional(readOnly = true)
    public BigDecimal calculateSalary(MonthlyWorkSummaryDto summary) {
        Employee employee = employeeRepository.findByTabNumber(summary.getTabNumber())
                .orElseThrow(() -> new IllegalArgumentException("Работник не найден"));

        BigDecimal hourlyRate = employee.getHourlyRate();

        // 1. Оплата за обычные часы вахты
        BigDecimal basePayment = hourlyRate.multiply(BigDecimal.valueOf(summary.getTotalWorkHours()));

        // 2. Оплата праздничных (в двойном размере)
        BigDecimal holidayPayment = hourlyRate.multiply(BigDecimal.valueOf(summary.getHolidayWorkHours()))
                .multiply(BigDecimal.valueOf(2.0));

        // 3. Дни в пути (8 часов * тариф)
        BigDecimal travelPayment = hourlyRate.multiply(BigDecimal.valueOf(summary.getTravelDaysCount() * 8L));

        // 4. Доплата за ночное время (Смены * 7 часов * 20%)
        BigDecimal nightHours = BigDecimal.valueOf(summary.getNightShiftsCount() * 7L);
        BigDecimal nightPayment = hourlyRate.multiply(nightHours).multiply(BigDecimal.valueOf(0.20));

        // 5. НАДБАВКА ЗА МЕЖВАХТОВЫЙ ОТДЫХ (Часы межвахты * тариф)
        BigDecimal interVaxthaPayment = hourlyRate.multiply(BigDecimal.valueOf(summary.getRestIntervaxHours()));

        // 6. Надбавка за вредность (только от базовых часов операторов)
        BigDecimal harmPercent = employee.getHarmfulConditionsPercent().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal harmPayment = basePayment.multiply(harmPercent);

        // --- СЕВЕРНЫЙ МУЛЬТИПЛИКАТОР ---
        // База для северных включает: Вахту, Праздники, Ночные, Вредность и Межвахту!
        BigDecimal northernBase = basePayment
                .add(holidayPayment)
                .add(nightPayment)
                .add(harmPayment)
                .add(interVaxthaPayment);

        BigDecimal districtAllowances = northernBase.multiply(employee.getDistrictCoefficient().subtract(BigDecimal.ONE));
        BigDecimal northernAllowances = northernBase.multiply(employee.getNorthernPercentage().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));

        // --- Фиксированные выплаты ---
        // Вахтовая надбавка (Суточные за дни работы + дни в пути)
        long activeVaxthaDays = (long) Math.ceil(summary.getTotalWorkHours() / 11.0);
        long totalAllowanceDays = activeVaxthaDays + summary.getTravelDaysCount();
        BigDecimal totalDailyAllowances = DAILY_ALLOWANCE_RATE.multiply(BigDecimal.valueOf(totalAllowanceDays));

        // Компенсация проезда
        BigDecimal transportCompensation = BigDecimal.ZERO;
        if (employee.getWorkMethod() == WorkMethod.VAXTA && summary.isHasTravelCompensation()) {
            transportCompensation = employee.getTravelCompensationRate();
        }

        // --- ИТОГО Gross (Без учета ранее выплаченных отпускных) ---
        return basePayment
                .add(holidayPayment)
                .add(travelPayment)
                .add(nightPayment)
                .add(interVaxthaPayment)
                .add(harmPayment)
                .add(districtAllowances)
                .add(northernAllowances)
                .add(totalDailyAllowances)
                .add(transportCompensation)
                .setScale(2, RoundingMode.HALF_UP);
    }
}


