package org.example.repository;



import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public interface BaseRepository<T> {

    int save(T t) throws SQLException;

    boolean removeById(UUID id);

    boolean remove(T t);

    boolean update(T t) throws SQLException;
}
