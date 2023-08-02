package сore.repository.TemporaryRepos;

import сore.models.CustomTour;

import java.util.ArrayList;
import java.util.List;

public class CustomTourRepo {
    public List<CustomTour> customTourList() {

        List<CustomTour> customTours = new ArrayList<>();
        customTours.add(new CustomTour(1L, "Кастомный тур 1", "desc", "media_path"));
        customTours.add(new CustomTour(2L, "Кастомный тур 2", "desc", "media_path"));
        customTours.add(new CustomTour(3L, "Кастомный тур 3", "desc", "media_path"));
        customTours.add(new CustomTour(4L, "Кастомный тур 4", "desc", "media_path"));

        return customTours;
    }
}
