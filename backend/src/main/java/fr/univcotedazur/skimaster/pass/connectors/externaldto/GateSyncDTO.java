package fr.univcotedazur.skimaster.pass.connectors.externaldto;

import java.util.List;

public record GateSyncDTO(List<Long> authorizedCardIds) {
}
