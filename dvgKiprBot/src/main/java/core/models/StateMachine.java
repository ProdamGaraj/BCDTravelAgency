package core.models;

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
public class StateMachine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    public User user;
    public Iterable<Activity> activities;
    public Resort resort;
    public Hotel hotel;
    public CustomTour customTour;
    public boolean authorized;
}