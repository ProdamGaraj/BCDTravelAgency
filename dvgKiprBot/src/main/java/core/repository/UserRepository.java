package core.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import core.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
