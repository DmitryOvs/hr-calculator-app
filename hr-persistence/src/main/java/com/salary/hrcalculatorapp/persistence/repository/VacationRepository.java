package com.salary.hrcalculatorapp.persistence.repository;

import com.salary.hrcalculatorapp.persistence.entity.VacationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface VacationRepository extends JpaRepository<VacationEntity, UUID> {

    // Найти все отпуска сотрудника
    List<VacationEntity> findByEmployeeId(UUID employeeId);

    // Найти отпуска за период
    List<VacationEntity> findByEmployeeIdAndStartDateBetween(UUID employeeId, LocalDate startDate, LocalDate endDate);

    // Найти пересекающиеся отпуска
    @Query("SELECT v FROM VacationEntity v WHERE v.employee.id = :employeeId " +
            "AND v.startDate <= :endDate AND v.endDate >= :startDate")
    List<VacationEntity> findOverlappingVacations(@Param("employeeId") UUID employeeId,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);

    // Подсчет дней отпуска за год
    @Query("SELECT SUM(v.daysCount) FROM VacationEntity v WHERE v.employee.id = :employeeId " +
            "AND YEAR(v.startDate) = :year")
    Integer sumVacationDaysByYear(@Param("employeeId") UUID employeeId, @Param("year") int year);

    // Найти утвержденные отпуска
    List<VacationEntity> findByEmployeeIdAndApprovedTrue(UUID employeeId);
}
