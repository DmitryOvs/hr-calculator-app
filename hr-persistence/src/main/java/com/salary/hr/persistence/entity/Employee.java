package com.salary.hr.persistence.entity;

import com.salary.hr.persistence.enums.WorkMethod;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tab_number", unique = true, nullable = false)
    private Integer tabNumber;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String position;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "hourly_rate", nullable = false, precision = 10, scale = 2)
    private BigDecimal hourlyRate; // Часовая ставка

    @Column(name = "district_coefficient", nullable = false, precision = 3, scale = 2)
    private BigDecimal districtCoefficient; // Например, 2.00

    @Column(name = "northern_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal northernPercentage; // Например, 100.00

    @Enumerated(EnumType.STRING)
    @Column(name = "work_method", nullable = false)
    private WorkMethod workMethod;

    @Column(name = "harmful_conditions_percent", nullable = false, precision = 4, scale = 2)
    private BigDecimal harmfulConditionsPercent; // Процент за вредность (например, 6.60%)

    @Column(name = "travel_compensation_rate", nullable = false, precision = 10, scale = 2)
    private BigDecimal travelCompensationRate; // Фиксированная стоимость проезда (например, 5747.00)
}


