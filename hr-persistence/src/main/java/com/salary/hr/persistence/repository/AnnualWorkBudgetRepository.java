package com.salary.hr.persistence.repository;

import com.salary.hr.persistence.entity.AnnualWorkBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnnualWorkBudgetRepository extends JpaRepository<AnnualWorkBudget, Long> {

    // Для проверки при импорте
    Optional<AnnualWorkBudget> findByEmployeeIdAndYearValueAndMonthValue(Long employeeId, Integer year, Integer month);

    // Для расчета отпускных (находит все 12 месяцев года)
    List<AnnualWorkBudget> findByEmployeeIdAndYearValue(Long employeeId, Integer year);
}

