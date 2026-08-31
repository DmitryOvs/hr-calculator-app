package com.salary.hrcalculatorapp.persistence.repository;

import com.salary.hrcalculatorapp.persistence.entity.WorkDayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface WorkDayRepository extends JpaRepository<WorkDayEntity, UUID> {

    // Найти все рабочие дни сотрудника за период
    List<WorkDayEntity> findByEmployeeIdAndWorkDateBetween(UUID employeeId, LocalDate startDate, LocalDate endDate);

    // Найти все рабочие дни за месяц
    @Query("SELECT w FROM WorkDayEntity w WHERE YEAR(w.workDate) = :year AND MONTH(w.workDate) = :month")
    List<WorkDayEntity> findByMonth(@Param("year") int year, @Param("month") int month);

    // Подсчитать общее количество часов за период
    @Query("SELECT SUM(w.hoursWorked) FROM WorkDayEntity w WHERE w.employee.id = :employeeId AND w.workDate BETWEEN :startDate AND :endDate")
    BigDecimal sumHoursWorked(@Param("employeeId") UUID employeeId,
                              @Param("startDate") LocalDate startDate,
                              @Param("endDate") LocalDate endDate);

    // Найти рабочие дни с переработкой
    List<WorkDayEntity> findByEmployeeIdAndOvertimeHoursGreaterThan(UUID employeeId, BigDecimal overtime);

    // Удалить все за период
    void deleteByEmployeeIdAndWorkDateBetween(UUID employeeId, LocalDate startDate, LocalDate endDate);
}