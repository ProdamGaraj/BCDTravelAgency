package bcd.solution.dvgKiprBot.core.models;;

import lombok.*;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.List;

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
    @OneToOne
    public User user;
    @ManyToMany
    public List<Activity> activities;
    @ManyToOne
    public Resort resort;
    @ManyToOne
    public Hotel hotel;
    @ManyToOne
    public CustomTour customTour;
    public boolean authorized;
    public boolean wait_password;
    public Integer auth_message_id;

}
