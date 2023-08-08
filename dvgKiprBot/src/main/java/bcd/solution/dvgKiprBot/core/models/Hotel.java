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
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    public String name;
    public String description;
    @OneToMany
    public List<HotelFeature> features;
    @ManyToOne
    public Resort resort;
    @Enumerated(EnumType.STRING)
    public Stars stars;
    @ElementCollection(targetClass = Food.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    public List<Food> food;
    public String media;
}
