package сore.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import сore.models.Activity;


@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {
}
