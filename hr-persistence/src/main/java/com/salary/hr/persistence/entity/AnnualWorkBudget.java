package com.salary.hr.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "annual_work_budgets",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"employee_id", "year_value", "month_value"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnualWorkBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "year_value", nullable = false)
    private Integer yearValue;  // Например, 2025

    @Column(name = "month_value", nullable = false)
    private Integer monthValue; // 1 - Январь, 2 - Февраль и т.д.

    @Column(name = "hours_worked", nullable = false)
    private Double hoursWorked; // Фактически отработанные часы (например, 154.0)

    @Column(name = "days_worked", nullable = false)
    private Integer daysWorked;   // Фактически отработанные дни (дни смен)

    @Column(name = "vacation_days", nullable = false)
    private Integer vacationDays; // Сколько дней человек был в отпуске в этом месяце

    @Column(name = "gross_salary_paid", nullable = false, precision = 12, scale = 2)
    private BigDecimal grossSalaryPaid; // Начисленная зарплата за этот месяц (база для СДЗ)
}

