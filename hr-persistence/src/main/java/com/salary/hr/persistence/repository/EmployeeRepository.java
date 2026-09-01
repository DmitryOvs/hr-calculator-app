package com.salary.hr.persistence.repository;

import com.salary.hr.persistence.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // Поиск сотрудника по табельному номеру для сопоставления с Excel
    Optional<Employee> findByTabNumber(Integer tabNumber);
}
