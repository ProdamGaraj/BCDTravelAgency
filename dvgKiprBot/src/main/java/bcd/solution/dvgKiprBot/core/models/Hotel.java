package bcd.solution.dvgKiprBot.core.models;

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
    @Column(columnDefinition = "TEXT")
    public String description;
    @ManyToMany(fetch = FetchType.EAGER)
    public List<HotelFeature> features;
    @ManyToOne(fetch = FetchType.EAGER)
    public Resort resort;
    @Enumerated(EnumType.STRING)
    public Stars stars;
    @ElementCollection(targetClass = Food.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    public List<Food> food;
    public String media;
    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    public boolean isDeleted = false;

    @Override
    public String toString() {
        StringBuilder features = new StringBuilder();
        for (HotelFeature feature : this.features) {
            features.append("- ").append(feature.name).append("\n");
        }
        StringBuilder foods = new StringBuilder();
        for (Food food : this.food) {
            foods.append("- ").append(food).append("\n");
        }
        return this.name + " " + this.stars.toString() + "\n\n" +
                "Относиться к курорту " + this.resort.name + "\n\n" +
                this.description + "\n\n" +
                "Особенноси:\n" +
                features + "\n\n" +
                "Доступные типы питания:\n" +
                foods;
    }
}
