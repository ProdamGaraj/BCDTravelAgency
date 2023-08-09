package bcd.solution.dvgKiprBot.core.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public String name;
    @Nullable
    public String description;
    @Enumerated(EnumType.STRING)
    public ActivityType activityType;
    @Nullable
    public Boolean free;
    public String media;

    public String toString() {
        return this.name + "\n\n"
                + this.description + "\n\n"
                + (Boolean.TRUE.equals(this.free) ? "Бесплатно" : "Платно");
    }

}