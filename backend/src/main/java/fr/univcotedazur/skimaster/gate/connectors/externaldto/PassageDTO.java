package fr.univcotedazur.skimaster.gate.connectors.externaldto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record PassageDTO(@JsonProperty("nfcCardId") Long forfeitId, LocalDateTime dateTime) {
}
