package сore.repository;
import org.springframework.stereotype.Repository;
import сore.models.Resort;
import сore.models.User;
import сore.repository.crud.CrudRepository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
