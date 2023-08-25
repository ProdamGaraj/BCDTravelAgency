package bcd.solution.dvgKiprBot.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
public class CustomTour {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    public String name;
    @Nullable
    @Column(columnDefinition = "TEXT")
    public String description;
    @Nullable
    public String media;

    @Override
    public String toString() {
        return this.name + "\n\n" +
                this.description;
    }
}
