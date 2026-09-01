package com.salary.hr.batch.service;

import com.salary.hr.persistence.entity.AnnualWorkBudget;
import com.salary.hr.persistence.entity.Employee;
import com.salary.hr.persistence.repository.AnnualWorkBudgetRepository;
import com.salary.hr.persistence.repository.EmployeeRepository;
import com.salary.hr.service.SalaryCalculatorService;
import com.salary.hr.common.dto.MonthlyWorkSummaryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnnualTimesheetImportService {

    private final EmployeeRepository employeeRepository;
    private final AnnualWorkBudgetRepository budgetRepository;
    private final SalaryCalculatorService salaryCalculatorService;

    @Transactional
    public void importAnnualTimesheet(InputStream inputStream, int targetYear) {
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            // Перебираем все листы в Excel файле (каждый лист — это отдельный месяц, например "Январь", "Февраль")
            for (int s = 0; s < workbook.getNumberOfSheets(); s++) {
                Sheet sheet = workbook.getSheetAt(s);
                int monthValue = s + 1; // Номер месяца от 1 до 12
                YearMonth period = YearMonth.of(targetYear, monthValue);

                log.info("Начало обработки листа: {}, период: {}", sheet.getSheetName(), period);

                for (Row row : sheet) {
                    Cell firstCell = row.getCell(0);
                    // Проверяем, что строка начинается с порядкового номера сотрудника (числовой тип)
                    if (firstCell == null || firstCell.getCellType() != CellType.NUMERIC) {
                        continue;
                    }

                    String tabNumberStr = getCellValueAsString(row.getCell(2)); // Колонка 3: Табельный номер
                    if (tabNumberStr.isEmpty()) continue;

                    Integer tabNumber = Double.valueOf(tabNumberStr).intValue();

                    // Ищем сотрудника в нашей БД, созданной через Docker
                    Optional<Employee> employeeOpt = employeeRepository.findByTabNumber(tabNumber);
                    if (employeeOpt.isEmpty()) {
                        log.warn("Сотрудник с табельным № {} обнаружен в Excel, но отсутствует в БД. Пропуск.", tabNumber);
                        continue;
                    }
                    Employee employee = employeeOpt.get();

                    double totalWorkHours = 0;
                    double holidayHours = 0;
                    double restIntervaxHours = 0;
                    int nightShifts = 0;
                    int travelDays = 0;
                    int vacationDays = 0;

                    // Сканируем ячейки дней месяца с 1 по 31 число (колонки 6-36 в структуре вашего файла)
                    for (int dayCol = 6; dayCol <= 36; dayCol++) {
                        Cell dayCell = row.getCell(dayCol);
                        if (dayCell == null) continue;

                        String mark = getCellValueAsString(dayCell).trim().toUpperCase();

                        switch (mark) {
                            case "11" -> totalWorkHours += 11;
                            case "11/2" -> {
                                totalWorkHours += 11;
                                nightShifts++; // Фиксируем ночную смену
                            }
                            case "Д" -> travelDays++; // День в пути
                            case "ОТ" -> vacationDays++; // День основного или северного отпуска
                            // Если в табеле стоит выходной на межвахте — здесь можно аккумулировать часы отдыха,
                            // либо считывать их из справочной строки баланса (в данном примере берем базовые отметки)
                        }
                    }

                    // Собираем промежуточный DTO для отправки в калькулятор зарплаты
                    MonthlyWorkSummaryDto summaryDto = MonthlyWorkSummaryDto.builder()
                            .tabNumber(tabNumber)
                            .period(period)
                            .totalWorkHours(totalWorkHours)
                            .holidayWorkHours(holidayHours)
                            .restIntervaxHours(restIntervaxHours)
                            .nightShiftsCount(nightShifts)
                            .travelDaysCount(travelDays)
                            .vacationDaysCount(vacationDays)
                            .hasTravelCompensation(travelDays > 0) // Если летел на вахту, значит было возмещение проезда
                            .build();

                    // Рассчитываем точную брутто-зарплату за этот исторический месяц
                    BigDecimal calculatedGross = salaryCalculatorService.calculateSalary(summaryDto);

                    // Сохраняем агрегированную строчку в годовой бюджет сотрудника
                    AnnualWorkBudget budgetRecord = budgetRepository
                            .findByEmployeeIdAndYearValueAndMonthValue(employee.getId(), targetYear, monthValue)
                            .orElse(new AnnualWorkBudget());

                    budgetRecord.setEmployee(employee);
                    budgetRecord.setYearValue(targetYear);
                    budgetRecord.setMonthValue(monthValue);
                    budgetRecord.setHoursWorked(totalWorkHours + holidayHours);
                    budgetRecord.setDaysWorked((int) Math.ceil((totalWorkHours + holidayHours) / 11.0));
                    budgetRecord.setVacationDays(vacationDays);
                    budgetRecord.setGrossSalaryPaid(calculatedGross);

                    budgetRepository.save(budgetRecord);
                }
            }
            log.info("Импорт годового табеля за {} год успешно завершен.", targetYear);
        } catch (Exception e) {
            log.error("Критическая ошибка при импорте годового файла табеля", e);
            throw new RuntimeException("Ошибка пакетной обработки табеля: " + e.getMessage());
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            default -> "";
        };
    }
}

