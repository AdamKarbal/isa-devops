package fr.univcotedazur.skimaster.cli.model;

import java.time.LocalDateTime;

public record IncidentDTO(
        Long id,
        IncidentType type,
        String description,
        LocalDateTime time,
        IncidentTargetType targetType,
        String targetCode,
        Boolean closes
) { }
