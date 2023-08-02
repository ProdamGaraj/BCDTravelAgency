package core.repository;

import org.springframework.data.repository.CrudRepository;
import core.models.Hotel;

public interface HotelRepository extends CrudRepository<Hotel, Long> {
}
