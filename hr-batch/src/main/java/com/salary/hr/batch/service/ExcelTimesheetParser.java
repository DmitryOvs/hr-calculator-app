package com.salary.hr.batch.service;

import com.salary.hr.common.dto.MonthlyWorkSummaryDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ExcelTimesheetParser {

    public List<MonthlyWorkSummaryDto> parseTimesheet(InputStream inputStream, YearMonth targetPeriod) {
        List<MonthlyWorkSummaryDto> summaries = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            // Предположим, что табель находится на первом листе
            Sheet sheet = workbook.getSheetAt(0);

            // Итерируемся по строкам. Нам нужны строки, где есть данные сотрудников
            for (Row row : sheet) {
                Cell firstCell = row.getCell(0);
                if (firstCell == null || firstCell.getCellType() != CellType.NUMERIC) {
                    continue; // Пропускаем шапки, графики режима работы и пустые строки
                }

                // Читаем данные строки сотрудника (сверяясь со структурой вашего файла)
                String fullName = getCellValueAsString(row.getCell(1));
                String tabNumberStr = getCellValueAsString(row.getCell(2));

                if (tabNumberStr.isEmpty() || fullName.isEmpty()) continue;

                Integer tabNumber = Double.valueOf(tabNumberStr).intValue();

                double totalWorkHours = 0;
                int nightShifts = 0;
                int travelDays = 0;

                // Дни месяца в вашем файле занимают колонки с 6 по 36 (1-31 числа)
                for (int dayCol = 6; dayCol <= 36; dayCol++) {
                    Cell dayCell = row.getCell(dayCol);
                    if (dayCell == null) continue;

                    String value = getCellValueAsString(dayCell).trim();

                    if (value.equals("11")) {
                        totalWorkHours += 11;
                    } else if (value.equals("11/2")) {
                        totalWorkHours += 11;
                        nightShifts++; // Засчитываем ночную смену
                    } else if (value.equals("Д")) {
                        travelDays++; // День в пути
                    } else if (value.equalsIgnoreCase("В")) {
                        // Выходной, игнорируем часы
                    }
                }

                // Собираем агрегированные данные по сотруднику за месяц
                MonthlyWorkSummaryDto summary = MonthlyWorkSummaryDto.builder()
                        .tabNumber(tabNumber)
                        .period(targetPeriod)
                        .totalWorkHours(totalWorkHours)
                        .nightShiftsCount(nightShifts)
                        .travelDaysCount(travelDays)
                        .build();

                summaries.add(summary);
                log.info("Успешно распарсен сотрудник: {}, часов: {}, ночей: {}", fullName, totalWorkHours, nightShifts);
            }

        } catch (Exception e) {
            log.error("Ошибка при парсинге Excel файла", e);
            throw new RuntimeException("Не удалось обработать табель: " + e.getMessage());
        }

        return summaries;
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

