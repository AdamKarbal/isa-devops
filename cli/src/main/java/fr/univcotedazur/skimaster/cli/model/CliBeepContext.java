package fr.univcotedazur.skimaster.cli.model;

public class CliBeepContext {

    private Long cardId;
    private Long gateId;

    public CliBeepContext(Long cardId, Long gateId) {
        this.cardId = cardId;
        this.gateId = gateId;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Long getGateId() {
        return gateId;
    }
    public void setGateId(Long gateId) {
        this.gateId = gateId;
    }

    @Override
    public String toString() {
        return "CliBeepContext [cardId=" + cardId + ", gateId="+gateId+"]";
    }
}
