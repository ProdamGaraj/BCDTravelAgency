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
public class Resort {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    public String name;
    public String description;
    public String geo;
    public String media;
    @ManyToMany(fetch = FetchType.EAGER)
    public List<Activity> activities;

    public String toString() {
        StringBuilder activity_list = new StringBuilder();
        for (Activity activity : this.activities) {
            activity_list.append("- ").append(activity.name).append("\n");
        }

        return this.name + "\n\n"
                + this.description + "\n\n"
                + this.geo + "\n\n"
                + "Доступные развлечения:\n"
                + activity_list;
    }
}
