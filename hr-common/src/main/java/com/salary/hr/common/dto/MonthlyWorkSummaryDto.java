package com.salary.hr.common.dto;

import lombok.Builder;
import lombok.Data;
import java.time.YearMonth;

@Data
@Builder
public class MonthlyWorkSummaryDto {
    private Integer tabNumber;
    private YearMonth period;
    private double totalWorkHours;       // Обычные часы вахты (например, 176)
    private double holidayWorkHours;     // Праздничные часы (например, 33)
    private double restIntervaxHours;    // Часы межвахтового отдыха (например, 56)
    private int nightShiftsCount;        // Ночные смены
    private int travelDaysCount;         // Дни в пути
    private int vacationDaysCount;       // Дни отпуска в этом месяце (если есть)
    private boolean hasTravelCompensation;
}



