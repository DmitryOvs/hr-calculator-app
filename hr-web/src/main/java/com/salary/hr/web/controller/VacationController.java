package com.salary.hr.web.controller;

import com.salary.hr.common.dto.VacationRequestDto;
import com.salary.hr.service.VacationCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/vacation")
@RequiredArgsConstructor
public class VacationController {

    private final VacationCalculatorService vacationCalculatorService;

    @PostMapping("/calculate")
    public ResponseEntity<?> calculateVacation(@RequestBody VacationRequestDto requestDto) {
        try {
            // Вызываем расчет годовых отпускных на основе базы данных
            BigDecimal payout = vacationCalculatorService.calculateVacationPayout(requestDto);
            return ResponseEntity.ok(Map.of(
                    "tabNumber", requestDto.getTabNumber(),
                    "vacationPayout", payout,
                    "vacationType", requestDto.getVacationType()
            ));
        } catch (IllegalStateException e) {
            // Если годовой табель еще не загружен
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Ошибка расчета: " + e.getMessage()));
        }
    }
}

