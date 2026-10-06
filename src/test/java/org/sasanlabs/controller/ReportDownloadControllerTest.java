package org.sasanlabs.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class ReportDownloadControllerTest {

    private MockMvc mockMvc;
    private ReportDownloadController controller;

    @BeforeEach
    void setUp() {
        controller = new ReportDownloadController();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void download_withDirectoryTraversal_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/reports/download").param("fileName", "../../../../etc/passwd"))
                .andExpect(status().isBadRequest());

        ResponseEntity<String> response = controller.download("../../../../etc/passwd");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void download_withAbsolutePath_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/reports/download").param("fileName", "/etc/passwd"))
                .andExpect(status().isBadRequest());

        ResponseEntity<String> response = controller.download("/etc/passwd");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void download_withBackslashTraversal_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/reports/download").param("fileName", "..\\..\\..\\etc\\passwd"))
                .andExpect(status().isBadRequest());

        ResponseEntity<String> response = controller.download("..\\..\\..\\etc\\passwd");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void download_withEmptyFileName_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/reports/download").param("fileName", ""))
                .andExpect(status().isBadRequest());

        ResponseEntity<String> response = controller.download("");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void download_withDotPath_returnsBadRequest() throws Exception {
        ResponseEntity<String> response = controller.download(".");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void download_withNonExistentFileUnderReportsDir_returnsNotFound() throws IOException {
        ResponseEntity<String> response = controller.download("nonexistent-report.txt");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
