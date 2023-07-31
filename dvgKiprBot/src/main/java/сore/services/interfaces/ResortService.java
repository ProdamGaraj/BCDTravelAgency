package сore.services.interfaces;

import сore.models.Filter;
import сore.models.Resort;

import java.util.Optional;

public interface ResortService {

    public Iterable<Resort> get_all_resorts();

    public Optional<Resort> get_by_id(long id);

    public void add_resort_to_filter(Resort resort, Filter filter);
}
