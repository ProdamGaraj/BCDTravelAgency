package bcd.solution.dvgKiprBot.core.models;;

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
public class HotelFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    public String name;

    public  String description;

    @Nullable
    public Boolean free;
}
