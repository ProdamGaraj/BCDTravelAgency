package сore.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import сore.models.Resort;


@Repository
public interface ResortRepository extends CrudRepository<Resort, Long> {
}
