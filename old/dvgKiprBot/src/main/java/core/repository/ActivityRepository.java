package core.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import core.models.Activity;


@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {
}
