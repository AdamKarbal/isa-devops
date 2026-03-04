package skimaster.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import skimaster.dtos.DisplayAlertDTO;
import skimaster.dtos.DisplayAlertRequestDTO;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class DisplayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayService.class);
    private final List<DisplayAlertDTO> alerts = new CopyOnWriteArrayList<>();

    public void pushAlert(DisplayAlertRequestDTO request) {
        String gateCode = (request.gateCode() == null || request.gateCode().isBlank()) ? "AUCUNE" : request.gateCode();
        DisplayAlertDTO alert = new DisplayAlertDTO(request.message(), gateCode, Instant.now());
        alerts.add(alert);
        LOGGER.info("[DISPLAY] Porte concernee={} | {}", gateCode, request.message());
    }

    public List<DisplayAlertDTO> getAlerts() {
        return alerts;
    }
}
