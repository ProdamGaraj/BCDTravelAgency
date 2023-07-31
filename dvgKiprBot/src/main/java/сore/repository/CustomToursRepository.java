package сore.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import сore.models.CustomTour;


@Repository
public interface CustomToursRepository extends CrudRepository<CustomTour, Long> {
}
