package com.salary.hr.web.controller;

import com.salary.hr.batch.service.AnnualTimesheetImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/timesheet")
@RequiredArgsConstructor
public class TimesheetImportController {

    private final AnnualTimesheetImportService importService;

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importAnnualExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("year") int year) {

        // Проверяем, что файл не пустой
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Файл Excel пуст или не выбран."));
        }

        // Проверяем расширение файла для безопасности
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".xlsx")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Разрешены только файлы формата .xlsx"));
        }

        try {
            log.info("Получен запрос на импорт годового табеля. Файл: {}, Год: {}", fileName, year);

            // Получаем InputStream файла и передаем в hr-batch сервис
            importService.importAnnualTimesheet(file.getInputStream(), year);

            return ResponseEntity.ok(Map.of(
                    "message", "Годовой табель успешно обработан и импортирован",
                    "fileName", fileName,
                    "targetYear", year
            ));

        } catch (Exception e) {
            log.error("Ошибка при импорте файла табеля: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Ошибка при обработке файла: " + e.getMessage()));
        }
    }
}

