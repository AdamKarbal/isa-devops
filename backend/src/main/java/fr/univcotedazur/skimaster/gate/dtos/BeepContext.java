package fr.univcotedazur.skimaster.gate.dtos;

import java.time.LocalDateTime;

public record BeepContext (

     Long cardId,
     Long gateId,
     LocalDateTime beepTime
)
{}
