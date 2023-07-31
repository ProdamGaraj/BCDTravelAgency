package сore.repository;
import org.springframework.stereotype.Repository;
import сore.models.Resort;
import сore.repository.crud.CrudRepository;

@Repository
public interface ResortRepository extends CrudRepository<Resort, Long> {
}
