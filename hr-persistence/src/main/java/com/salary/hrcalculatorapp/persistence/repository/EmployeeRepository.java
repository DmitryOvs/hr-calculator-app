package com.salary.hrcalculatorapp.persistence.repository;

import com.salary.hrcalculatorapp.persistence.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, UUID> {

    // Поиск по табельному номеру
    Optional<EmployeeEntity> findByPersonnelNumber(String personnelNumber);

    // Поиск по имени (частичное совпадение)
    List<EmployeeEntity> findByFullNameContainingIgnoreCase(String fullName);

    // Поиск активных сотрудников
    List<EmployeeEntity> findByActiveTrue();

    // Поиск сотрудников с северной надбавкой
    List<EmployeeEntity> findByNorthRegionTrue();

    // Поиск по должности
    List<EmployeeEntity> findByPosition(String position);

    // Проверка существования по табельному номеру
    boolean existsByPersonnelNumber(String personnelNumber);

    // Подсчет сотрудников с зарплатой выше указанной
    @Query("SELECT COUNT(e) FROM EmployeeEntity e WHERE e.hourlyRate > :rate")
    long countEmployeesWithRateGreaterThan(@Param("rate") BigDecimal rate);

    // Поиск сотрудников с опытом работы на севере больше указанного
    @Query("SELECT e FROM EmployeeEntity e WHERE e.northRegion = true AND e.northExperienceYears >= :years")
    List<EmployeeEntity> findNorthEmployeesWithExperience(@Param("years") int years);

    // Кастомный запрос с JOIN
    @Query("SELECT DISTINCT e FROM EmployeeEntity e LEFT JOIN FETCH e.workDays w WHERE w.workDate BETWEEN :startDate AND :endDate")
    List<EmployeeEntity> findEmployeesWithWorkDaysBetween(@Param("startDate") LocalDate startDate,
                                                          @Param("endDate") LocalDate endDate);
}
