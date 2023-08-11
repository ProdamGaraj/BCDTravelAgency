package bcd.solution.dvgKiprBot.core.models;;

import lombok.*;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    @OneToOne(fetch = FetchType.EAGER)
    public User user;
    @ManyToMany(fetch = FetchType.EAGER)
    public List<Activity> activities = new ArrayList<>();
    @ManyToOne(fetch = FetchType.EAGER)
    public Resort resort;
    @ManyToOne(fetch = FetchType.EAGER)
    public Hotel hotel;
    @ManyToOne(fetch = FetchType.EAGER)
    public CustomTour customTour;
    public boolean authorized;
    public boolean wait_password;
    public Integer auth_message_id;

}
