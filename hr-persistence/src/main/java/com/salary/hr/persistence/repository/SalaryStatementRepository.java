package com.salary.hr.persistence.repository;

import com.salary.hr.persistence.entity.SalaryStatement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryStatementRepository extends JpaRepository<SalaryStatement, Long> {
}
