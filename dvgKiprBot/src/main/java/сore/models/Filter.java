package сore.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Filter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Activity activity;
    public Resort resort;
    public Hotel hotel;
    public CustomTour customTour;
}
