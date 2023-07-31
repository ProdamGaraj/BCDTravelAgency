package сore.repository;

import org.springframework.stereotype.Repository;
import сore.models.Activity;
import сore.repository.crud.CrudRepository;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {
}
