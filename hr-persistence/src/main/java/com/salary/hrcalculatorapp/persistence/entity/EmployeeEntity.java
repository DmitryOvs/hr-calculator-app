package com.salary.hrcalculatorapp.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "employees",
        indexes = {
                @Index(name = "idx_personnel_number", columnList = "personnel_number", unique = true),
                @Index(name = "idx_full_name", columnList = "full_name")
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(name = "position", length = 100)
    private String position;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "personnel_number", unique = true, nullable = false, length = 50)
    private String personnelNumber;

    @Column(name = "hourly_rate", nullable = false, precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    @Column(name = "is_north_region")
    @Builder.Default
    private boolean northRegion = false;

    @Column(name = "north_experience_years")
    @Builder.Default
    private Integer northExperienceYears = 0;

    @Column(name = "hiring_date")
    private LocalDateTime hiringDate;

    @Column(name = "is_active")
    @Builder.Default
    private boolean active = true;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "birth_date")
    private LocalDateTime birthDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Связь с рабочими днями
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<WorkDayEntity> workDays = new ArrayList<>();

    // Связь с отпусками
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<VacationEntity> vacations = new ArrayList<>();

    // Вспомогательные методы для управления связями
    public void addWorkDay(WorkDayEntity workDay) {
        workDays.add(workDay);
        workDay.setEmployee(this);
    }

    public void removeWorkDay(WorkDayEntity workDay) {
        workDays.remove(workDay);
        workDay.setEmployee(null);
    }

    public void addVacation(VacationEntity vacation) {
        vacations.add(vacation);
        vacation.setEmployee(this);
    }

    public void removeVacation(VacationEntity vacation) {
        vacations.remove(vacation);
        vacation.setEmployee(null);
    }
}