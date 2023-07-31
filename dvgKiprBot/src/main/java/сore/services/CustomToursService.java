package сore.services;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import сore.models.CustomTour;
import сore.models.StateMachine;
import сore.repository.CustomToursRepository;

@Service
public class CustomToursService {
    @Autowired
    private static CustomToursRepository repository;

    public Iterable<CustomTour> get_all_custom_tours() {
        return repository.findAll();
    }


    public CustomTour get_by_id(Long id) {
        return repository.find_first(id);
    }

    public CustomTour get_next(Long id) {
        return repository.find_first(++id);
    }
    public CustomTour get_prev(Long id) {
        return repository.find_first(--id);
    }
    //TODO: clean up this shit
    public CustomTour get_first(){
        return repository.find_first(Long.parseLong("0"));
    }

    public CustomTour get_last(){
        return repository.find_first(Long.parseLong("-1"));
    }

    public void add_custom_tour_to_filter(CustomTour customTour, StateMachine stateMachine) {
        stateMachine.customTour = customTour;
    }
}
