package сore.repository.crud;

import org.springframework.data.repository.Repository;

import java.io.Serializable;
public interface CrudRepository<T, ID extends Serializable>
        extends Repository<T, ID> {
    <S extends T> S save (S entity);
    T find_first(ID primaryKey);
    Iterable<T> findAll();
    void delete (T entity);
    boolean exists(ID primaryKey);

}
