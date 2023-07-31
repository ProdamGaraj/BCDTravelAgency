package сore.repository;

import org.springframework.stereotype.Repository;
import сore.models.CustomTour;
import сore.repository.crud.CrudRepository;

@Repository
public interface CustomToursRepository extends CrudRepository<CustomTour, Long> {
}
