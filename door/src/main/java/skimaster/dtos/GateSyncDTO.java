package skimaster.dtos;
import java.util.List;

public record GateSyncDTO(List<Long> authorizedCardIds) {}
