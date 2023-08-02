package core.services;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import core.models.CustomTour;
import core.repository.CustomToursRepository;

import java.util.Optional;

@Service
public class CustomToursService {
    @Autowired
    private static CustomToursRepository repository;

    public Iterable<CustomTour> get_all_custom_tours() {
        return repository.findAll();
    }


    public Optional<CustomTour> get_by_id(Long id) {
        return repository.findById(id);
    }

    public Optional<CustomTour> get_next(Long id) {
        return repository.findById(++id);}
    public Optional<CustomTour> get_prev(Long id) {
        return repository.findById(--id);
    }

    //TODO: clean up this shit
    public Optional<CustomTour> get_first(){
        return repository.findById(Long.parseLong("0"));
    }


    public Optional<CustomTour> get_last(){
        return repository.findById(Long.parseLong("-1"));
    }
}
