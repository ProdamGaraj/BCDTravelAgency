package сore.repository.TemporaryRepos;

import сore.models.Activity;
import сore.models.ActivityType;

import java.util.ArrayList;
import java.util.List;

public class ActivityRepo {
    public List<Activity> activityList() {

        List<Activity> activities = new ArrayList<>();
        activities.add(new Activity(1L, "зал", "тренажерный", ActivityType.YOUTH, true));
        activities.add(new Activity(2L, "теннис", "настольный", ActivityType.YOUTH, true));
        activities.add(new Activity(3L, "бильярд", "бильярд", ActivityType.ELITE, false));
        activities.add(new Activity(4L, "музыка", "живая", ActivityType.FANCY, true));

        return  activities;
    }
}
