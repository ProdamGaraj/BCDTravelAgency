package core.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import core.models.CustomTour;


@Repository
public interface CustomToursRepository extends CrudRepository<CustomTour, Long> {
}
