package skimaster.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import skimaster.components.DisplayService;
import skimaster.dtos.DisplayAlertDTO;
import skimaster.dtos.DisplayAlertRequestDTO;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DisplayController.class)
class DisplayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DisplayService displayService;

    @Test
    void shouldAcceptAlert() throws Exception {
        DisplayAlertRequestDTO request = new DisplayAlertRequestDTO("Alerte portique", "G-NORD-01");
        doNothing().when(displayService).pushAlert(request);

        mockMvc.perform(post("/display/alerts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(displayService).pushAlert(request);
    }

    @Test
    void shouldReturnAllAlerts() throws Exception {
        when(displayService.getAlerts()).thenReturn(List.of(
                new DisplayAlertDTO("Alerte 1", "G-NORD-01", Instant.parse("2026-03-04T10:00:00Z"))
        ));

        mockMvc.perform(get("/display/alerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value("Alerte 1"))
                .andExpect(jsonPath("$[0].gateCode").value("G-NORD-01"));
    }

    @Test
    void shouldReturnAlertsForSpecificGateCode() throws Exception {
        when(displayService.getAlertsForGateCode("gate-1")).thenReturn(List.of(
                new DisplayAlertDTO("Alerte ciblee", "gate-1", Instant.parse("2026-03-04T10:00:00Z"))
        ));

        mockMvc.perform(get("/display/alerts/gate-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value("Alerte ciblee"))
                .andExpect(jsonPath("$[0].gateCode").value("gate-1"));
    }

    @Test
    void shouldClearAllAlerts() throws Exception {
        doNothing().when(displayService).clearAlerts();

        mockMvc.perform(delete("/display/alerts"))
                .andExpect(status().isNoContent());

        verify(displayService).clearAlerts();
    }

    @Test
    void shouldClearAlertsForSpecificGate() throws Exception {
        doNothing().when(displayService).clearAlertsForGateCode("gate-1");

        mockMvc.perform(delete("/display/alerts/gate-1"))
                .andExpect(status().isNoContent());

        verify(displayService).clearAlertsForGateCode("gate-1");
    }

    @Test
    void shouldReturnDisplayId() throws Exception {
        when(displayService.getDisplayId()).thenReturn("display-1");

        mockMvc.perform(get("/display/id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("display-1"));
    }
}
