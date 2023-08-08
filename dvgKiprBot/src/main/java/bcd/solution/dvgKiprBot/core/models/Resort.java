package bcd.solution.dvgKiprBot.core.models;;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
public class Resort {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    public String name;
    public String description;
    public String geo;
    public String media;
    @OneToMany
    public List<Activity> activities;
}
