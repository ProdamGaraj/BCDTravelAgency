package сore.services;

import org.springframework.beans.factory.annotation.Autowired;
import сore.models.CustomTour;
import сore.models.Filter;
import сore.repository.CustomToursRepository;


public class CustomToursService {
    @Autowired
    private static CustomToursRepository repository;
    public static Iterable<CustomTour> list = repository.findAll();

    public Iterable<CustomTour> get_all_custom_tours() {
        return repository.findAll();
    }


    public CustomTour get_by_id(long id) {
        return repository.findOne(id);
    }

    public void add_custom_tour_to_filter(CustomTour customTour, Filter filter) {
        filter.customTour = customTour;
    }
}
