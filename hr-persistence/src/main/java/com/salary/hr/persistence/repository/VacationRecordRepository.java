package com.salary.hr.persistence.repository;

import com.salary.hr.persistence.entity.VacationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacationRecordRepository extends JpaRepository<VacationRecord, Long> {
}
