package org.sasanlabs.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ReportDownloadControllerTest {

    private final ReportDownloadController controller = new ReportDownloadController();

    @Test
    void testDownloadPathTraversalParentDir() throws IOException {
        ResponseEntity<String> response = controller.download("../../../../etc/passwd");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDownloadAbsolutePath() throws IOException {
        ResponseEntity<String> response = controller.download("/etc/passwd");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDownloadEmptyFileName() throws IOException {
        ResponseEntity<String> response = controller.download("");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDownloadDotFileName() throws IOException {
        ResponseEntity<String> response = controller.download(".");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDownloadDotDotFileName() throws IOException {
        ResponseEntity<String> response = controller.download("..");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDownloadNonExistentFile() throws IOException {
        ResponseEntity<String> response = controller.download("does-not-exist.txt");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
