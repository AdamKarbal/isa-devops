package fr.univcotedazur.skimaster.incident.dto;

import fr.univcotedazur.skimaster.incident.entities.IncidentType;

import java.time.LocalDateTime;

public record IncidentDTO(Long id, IncidentType type, String description, LocalDateTime time) { }
