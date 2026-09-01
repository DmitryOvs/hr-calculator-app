package com.salary.hr.web.controller;


import com.salary.hr.common.dto.MonthlyWorkSummaryDto;
import com.salary.hr.service.SalaryCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/salary")
@RequiredArgsConstructor
public class SalaryCalculationController {

    private final SalaryCalculatorService salaryCalculatorService;

    @PostMapping("/calculate")
    public ResponseEntity<?> calculateSalary(@RequestBody MonthlyWorkSummaryDto summaryDto) {
        try {
            // Вызываем наш доработанный сервис
            var result = salaryCalculatorService.calculateSalary(summaryDto);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            // Если сотрудника нет в БД, вернем понятную ошибку
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Внутренняя ошибка сервера"));
        }
    }
}

