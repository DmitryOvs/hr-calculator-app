package com.salary.hr.common.dto;


import com.salary.hr.common.enums.VacationType;
import lombok.Data;
import java.time.LocalDate;

@Data
public class VacationRequestDto {
    private Integer tabNumber;      // Кого отправляем (например, 55)
    private LocalDate startDate;     // С какого числа (2026-01-06)
    private LocalDate endDate;       // По какое число (2026-01-31)
    private VacationType vacationType; // Какой тип отпуска считаем
}

