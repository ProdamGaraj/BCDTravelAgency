package сore.services;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import сore.models.StateMachine;
import сore.models.Resort;
import сore.repository.ResortRepository;

import java.util.Optional;

@Service
public class ResortService {
    @Autowired
    private ResortRepository repository;
    public Iterable<Resort> get_all_resorts() {
        return repository.findAll();
    }

    public Optional<Resort> get_by_id(long id) {
        return repository.findById(id);
    }

    public Optional<Resort> get_next(Long id) {
        return repository.findById(++id);
    } //генерацию сообщения (текста + медиа)
    public Optional<Resort> get_prev(Long id) {
        return repository.findById(--id);
    }
    //TODO: clean up this shit
    public Optional<Resort> get_first(){
        return repository.findById(Long.parseLong("0"));
    }

    public Optional<Resort> get_last(){
        return repository.findById(Long.parseLong("-1"));
    }
    public void add_resort_to_filter(Resort resort, StateMachine stateMachine) {
        stateMachine.resort = resort;
    }
}
