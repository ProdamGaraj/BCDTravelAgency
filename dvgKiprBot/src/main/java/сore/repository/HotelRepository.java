package сore.repository;

import org.springframework.data.repository.CrudRepository;
import сore.models.Hotel;

public interface HotelRepository extends CrudRepository<Hotel, Long> {
}
