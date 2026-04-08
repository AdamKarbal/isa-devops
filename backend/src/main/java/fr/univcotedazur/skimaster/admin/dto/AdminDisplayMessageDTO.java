package fr.univcotedazur.skimaster.admin.dto;

import jakarta.validation.constraints.NotBlank;

public record AdminDisplayMessageDTO(
        @NotBlank(message = "Le message ne peut pas etre vide.")
        String message,
        String displayCode
) {
}
