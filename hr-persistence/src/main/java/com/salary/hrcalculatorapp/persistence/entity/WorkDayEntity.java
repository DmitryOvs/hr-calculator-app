package com.salary.hrcalculatorapp.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "work_days",
        indexes = {
                @Index(name = "idx_workday_employee_date", columnList = "employee_id, work_date"),
                @Index(name = "idx_workday_month_year", columnList = "work_date")
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkDayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    @Column(name = "hours_worked", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal hoursWorked = BigDecimal.ZERO;

    @Column(name = "is_holiday")
    @Builder.Default
    private boolean holiday = false;

    @Column(name = "is_sick_leave")
    @Builder.Default
    private boolean sickLeave = false;

    @Column(name = "is_vacation")
    @Builder.Default
    private boolean vacation = false;

    @Column(name = "overtime_hours", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal overtimeHours = BigDecimal.ZERO;

    @Column(name = "comment", length = 500)
    private String comment;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
