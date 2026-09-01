package com.salary.hr.persistence.entity;

import com.salary.hr.common.enums.VacationType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "vacation_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate; // Дата начала отпуска (например, 2026-01-06)

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;   // Дата окончания (например, 2026-01-31)

    @Column(name = "vacation_days", nullable = false)
    private Integer vacationDays; // Количество дней отпуска (например, 23 дня)

    @Enumerated(EnumType.STRING)
    @Column(name = "vacation_type", nullable = false)
    private VacationType vacationType;

    @Column(name = "calculated_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal calculatedAmount; // Итоговая сумма отпускных
}

