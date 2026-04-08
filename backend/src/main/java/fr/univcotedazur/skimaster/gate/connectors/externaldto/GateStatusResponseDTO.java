package fr.univcotedazur.skimaster.gate.connectors.externaldto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record GateStatusResponseDTO(String gateId, String status, @JsonAlias("threshold") int alertThreshold) {
}
