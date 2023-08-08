package bcd.solution.dvgKiprBot.core.repository;;

import bcd.solution.dvgKiprBot.core.models.Activity;
import bcd.solution.dvgKiprBot.core.models.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public class ActivityRepo{
    public List<Activity> activityList() {

        List<Activity> activities = new ArrayList<>();
        activities.add(new Activity(1L, "зал", "тренажерный", ActivityType.YOUTH, true, "pathToFile"));
        activities.add(new Activity(2L, "теннис", "настольный", ActivityType.YOUTH, true, "pathToFile"));
        activities.add(new Activity(3L, "бильярд", "бильярд", ActivityType.ELITE, false, "pathToFile"));
        activities.add(new Activity(4L, "музыка", "живая", ActivityType.FANCY, true, "pathToFile"));

        return  activities;
    }
}
