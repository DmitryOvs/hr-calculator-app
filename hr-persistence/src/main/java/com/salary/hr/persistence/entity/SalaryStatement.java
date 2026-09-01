package com.salary.hr.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.YearMonth;

@Entity
@Table(name = "salary_statements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private String period; // Храним в формате "2027-01"

    private BigDecimal basePayment;
    private BigDecimal travelPayment;
    private BigDecimal nightPayment;
    private BigDecimal northernAllowances;
    private BigDecimal totalGross;
}

