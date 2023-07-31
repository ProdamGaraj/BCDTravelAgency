package сore.services;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import сore.models.Filter;
import сore.models.Resort;
import сore.repository.ResortRepository;

@Service
public class ResortService {
    @Autowired
    private ResortRepository repository;
    public Iterable<Resort> get_all_resorts() {
        return repository.findAll();
    }

    public Resort get_by_id(long id) {
        return repository.find_first(id);
    }

    public Resort get_next(Long id) {
        return repository.find_first(++id);
    } //генерацию сообщения (текста + медиа)
    public Resort get_prev(Long id) {
        return repository.find_first(--id);
    }
    //TODO: clean up this shit
    public Resort get_first(){
        return repository.find_first(Long.parseLong("0"));
    }

    public Resort get_last(){
        return repository.find_first(Long.parseLong("-1"));
    }
    public void add_resort_to_filter(Resort resort, Filter filter) {
        filter.resort = resort;
    }
}
