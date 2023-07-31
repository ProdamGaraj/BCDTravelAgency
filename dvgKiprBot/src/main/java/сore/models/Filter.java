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
public class Filter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Activity activity;
    public Resort resort;
    public Hotel hotel;
    public CustomTour customTour;
}
