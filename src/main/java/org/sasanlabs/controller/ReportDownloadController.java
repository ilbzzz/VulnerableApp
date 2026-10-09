package org.sasanlabs.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class ReportDownloadController {

    private static final String REPORTS_DIR = "/tmp/vulnerableapp/reports/";

    @GetMapping("/download")
    public ResponseEntity<String> download(@RequestParam String fileName) throws IOException {
        if (fileName == null || fileName.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Path baseDir = Paths.get(REPORTS_DIR).toAbsolutePath().normalize();
        Path reportPath;
        try {
            reportPath = baseDir.resolve(fileName).normalize();
        } catch (InvalidPathException e) {
            return ResponseEntity.badRequest().build();
        }

        if (!reportPath.startsWith(baseDir) || reportPath.equals(baseDir)) {
            return ResponseEntity.badRequest().build();
        }

        if (!Files.exists(reportPath) || !Files.isRegularFile(reportPath)) {
            return ResponseEntity.notFound().build();
        }

        try {
            Path realBase = baseDir.toRealPath();
            Path realPath = reportPath.toRealPath();
            if (!realPath.startsWith(realBase)) {
                return ResponseEntity.badRequest().build();
            }
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }

        byte[] content = Files.readAllBytes(reportPath);
        return ResponseEntity.ok(new String(content, StandardCharsets.UTF_8));
    }
}
