package сore.services;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import сore.models.Filter;
import сore.models.Resort;
import сore.repository.ResortRepository;

import java.util.Optional;

@Service
public class ResortService {
    @Autowired
    private ResortRepository repository;

    public Iterable<Resort> get_all_resorts() {
        Iterable<Resort> list = repository.findAll();
        return list;
    }

    public Resort get_by_id(long id) {
        Resort temp = repository.findOne(id);
        return repository.findOne(id);
    }

    public void add_resort_to_filter(Resort resort, Filter filter) {
        filter.resort = resort.name;
    }
}
