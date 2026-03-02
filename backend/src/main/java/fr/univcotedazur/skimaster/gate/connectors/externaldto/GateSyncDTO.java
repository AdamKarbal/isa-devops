package fr.univcotedazur.skimaster.gate.connectors.externaldto;

import java.util.List;

public record GateSyncDTO(List<Long> authorizedCardIds) {
}
