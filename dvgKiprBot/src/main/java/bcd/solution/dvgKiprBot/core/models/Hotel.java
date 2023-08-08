package bcd.solution.dvgKiprBot.core.models;;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @OneToOne
    public Resort resort;
    @Enumerated(EnumType.STRING)
    public StarType stars;
    @Enumerated(EnumType.STRING)
    @OneToMany
    public List<FoodType> food;
    public String media;
}
