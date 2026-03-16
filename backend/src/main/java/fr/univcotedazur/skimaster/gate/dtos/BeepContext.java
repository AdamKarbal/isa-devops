package fr.univcotedazur.skimaster.gate.dtos;

import java.time.LocalDateTime;

public record BeepContext (

     Long cardId,
     String gateId,
     LocalDateTime beepTime
)
{}
