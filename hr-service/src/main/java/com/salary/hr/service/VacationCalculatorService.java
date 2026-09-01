package com.salary.hr.service;

import com.salary.hr.common.dto.VacationRequestDto;
import com.salary.hr.persistence.entity.AnnualWorkBudget;
import com.salary.hr.persistence.entity.Employee;
import com.salary.hr.persistence.repository.AnnualWorkBudgetRepository;
import com.salary.hr.persistence.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacationCalculatorService {

    private final AnnualWorkBudgetRepository budgetRepository;
    private final EmployeeRepository employeeRepository;

    private static final BigDecimal AVERAGE_MONTH_DAYS = BigDecimal.valueOf(29.3);

    @Transactional(readOnly = true)
    public BigDecimal calculateVacationPayout(VacationRequestDto request) {
        Employee employee = employeeRepository.findByTabNumber(request.getTabNumber())
                .orElseThrow(() -> new IllegalArgumentException("Сотрудник не найден"));

        // 1. Извлекаем годовой табель (все 12 месяцев за указанный рабочий год)
        int targetYear = request.getStartDate().getYear() - 1; // Берем прошлый календарный год
        List<AnnualWorkBudget> yearlyRecords = budgetRepository
                .findByEmployeeIdAndYearValue(employee.getId(), targetYear);

        if (yearlyRecords.isEmpty()) {
            throw new IllegalStateException("В базе данных отсутствуют годовые табели за " + targetYear + " год.");
        }

        BigDecimal totalEarningsForYear = BigDecimal.ZERO;
        BigDecimal totalEstimatedDays = BigDecimal.ZERO;

        // 2. Рассчитываем сумму заработка и общее количество учитываемых дней за год
        for (AnnualWorkBudget monthBudget : yearlyRecords) {
            // Суммируем заработок, облагаемый для расчета отпускных
            totalEarningsForYear = totalEarningsForYear.add(monthBudget.getGrossSalaryPaid());

            if (monthBudget.getVacationDays() == 0 && monthBudget.getHoursWorked() > 0) {
                // Если месяц отработан полностью (без отпусков) — добавляем стандартные 29.3 дня
                totalEstimatedDays = totalEstimatedDays.add(AVERAGE_MONTH_DAYS);
            } else {
                // Если месяц не полный (были отпуска/межвахта), считаем дни пропорционально:
                // (Календарные дни месяца - дни отпуска) / Календарные дни месяца * 29.3
                long totalDaysInMonth = request.getStartDate().withMonth(monthBudget.getMonthValue()).lengthOfMonth();
                double activeDaysPercent = (double) (totalDaysInMonth - monthBudget.getVacationDays()) / totalDaysInMonth;

                BigDecimal calculatedMonthDays = AVERAGE_MONTH_DAYS.multiply(BigDecimal.valueOf(activeDaysPercent));
                totalEstimatedDays = totalEstimatedDays.add(calculatedMonthDays);
            }
        }

        // 3. Вычисляем Средний Дневной Заработок (СДЗ)
        if (totalEstimatedDays.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;

        BigDecimal averageDailyEarnings = totalEarningsForYear.divide(totalEstimatedDays, 2, RoundingMode.HALF_UP);

        // 4. Итоговая сумма отпускных = СДЗ * Количество дней планируемого отпуска
        BigDecimal totalVacationDays = BigDecimal.valueOf(request.getVacationDaysCount());

        return averageDailyEarnings.multiply(totalVacationDays).setScale(2, RoundingMode.HALF_UP);
    }
}

