package fr.univcotedazur.skimaster.incident.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Incident {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private IncidentType type;

    @NotNull
    private IncidentTargetType targetType;

    private String targetCode;

    @NotNull
    private String description;

    @NotNull
    private LocalDateTime time;

    private boolean resolved;

    private Boolean closes;

    public Incident(IncidentType type, IncidentTargetType targetType, String targetCode, String description, LocalDateTime time, Boolean closes) {
        this.type = type;
        this.targetType = targetType;
        this.targetCode = targetCode;
        this.description = description;
        this.time = time;
        this.resolved = false;
        this.closes = closes;
    }

    public Incident(IncidentType type, IncidentTargetType targetType, String targetCode, String description, Boolean closes) {
        this(type, targetType, targetCode, description, LocalDateTime.now(), closes);
    }

    public Incident() {
    }

    public @NotNull IncidentType getType() {
        return type;
    }

    public @NotNull IncidentTargetType getTargetType() {
        return targetType;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public @NotNull String getDescription() {
        return description;
    }

    public @NotNull LocalDateTime getTime() {
        return time;
    }

    public @NotNull Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Incident)) return false;

        Incident incident = (Incident) o;

        return type == incident.getType() &&
                targetType == incident.getTargetType() &&
                Objects.equals(targetCode, incident.getTargetCode()) &&
                Objects.equals(time, incident.getTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, targetType, targetCode, time);
    }

    public void resolve(){
        this.resolved = true;
    }

    public boolean isResolved(){
        return this.resolved;
    }

    public Boolean isClosing(){
        return this.closes;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        if(this.isClosing()) sb.append("[FERMANT]");
        else sb.append("[NON FERMANT]");
        sb.append("Incident à ")
                .append(this.targetCode)
                .append(" : ")
                .append(this.type)
                .append(" - ")
                .append(this.description);
        return sb.toString();
    }
}
