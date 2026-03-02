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
    private String description;
    @NotNull
    private LocalDateTime time;

    public Incident(IncidentType type, String description, LocalDateTime time) {
        this.type = type;
        this.description = description;
        this.time = LocalDateTime.now();
    }

    public Incident() {

    }

    public @NotNull IncidentType getType() {
        return type;
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
    public boolean equals(Object o){
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Incident incident = (Incident) o;
        return time != null && Objects.equals(time, incident.time);
    }

    @Override public int hashCode(){
        return Objects.hash(time);
    }
}
