package bcd.solution.dvgKiprBot.core.repository;;

import bcd.solution.dvgKiprBot.core.models.CustomTour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface CustomTourRepo extends JpaRepository<CustomTour, Long> {
//    public List<CustomTour> customTourList() {
//
//        List<CustomTour> customTours = new ArrayList<>();
//        customTours.add(new CustomTour(1L, "Тур 1", "desc", "media_path"));
//        customTours.add(new CustomTour(2L, "Тур 2", "desc", "media_path"));
//        customTours.add(new CustomTour(3L, "Тур 3", "desc", "media_path"));
//        customTours.add(new CustomTour(4L, "Тур 4", "desc", "media_path"));
//
//        return customTours;
//    }
}
