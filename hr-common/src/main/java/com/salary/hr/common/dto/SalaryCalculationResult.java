package com.salary.hr.common.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class SalaryCalculationResult {
    private Integer tabNumber;
    private String fullName;
    private BigDecimal baseWorkPayment;   // Оплата обычных часов
    private BigDecimal travelPayment;     // Оплата дней в пути (Д * 8 часов * ставка)
    private BigDecimal nightBonusPayment;  // Надбавка за ночные (Ночи * 7 часов * 20% от оклада/ставки)
    private BigDecimal northernAllowances; // Сумма северных и районных надбавок
    private BigDecimal totalGrossSalary;   // Итого начислено (До вычета НДФЛ)
}

