package org.sasanlabs.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.HttpStatus;
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
        if (fileName == null || fileName.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            Path baseDir = Paths.get(REPORTS_DIR).toAbsolutePath().normalize();
            Path reportPath = baseDir.resolve(fileName).normalize();
            if (!reportPath.startsWith(baseDir)
                    || reportPath.equals(baseDir)
                    || !Files.isRegularFile(reportPath)
                    || !reportPath.toRealPath().startsWith(baseDir.toRealPath())) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            byte[] content = Files.readAllBytes(reportPath);
            return ResponseEntity.ok(new String(content, StandardCharsets.UTF_8));
        } catch (InvalidPathException | IOException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
