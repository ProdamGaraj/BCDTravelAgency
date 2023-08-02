package сore.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    public Iterable<HotelFeature> features;
    public Resort resort;
    public String media;

    public enum stars {
        nullstar,
        onestar,
        twostar,
        threestar,
        fourstar,
        fivestar
    }
    public enum food {
        BB,
        HB,
        FB,
        AllInclusive
    }
}
