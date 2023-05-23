package org.example.repository.user;

import org.example.domain.model.user.User;
import org.example.domain.model.user.UserState;
import org.example.repository.BaseRepository;

import java.sql.SQLException;
import java.util.Optional;

public interface UserRepository extends BaseRepository<User> {
    Optional<User> findByPhoneNumber(String phoneNumber) throws SQLException;
    String INSERT = """
            insert into users(id, name, user_state)
                values(?, ?, ?);""";
    String FIND_BY_ID = """
            select * from users where id = ?;""";

    String FIND_BY_PHONE_NUMBER = "select * from users where phone_number = ?;";

    String USER_STATE = "select user_state from users where id = ?;";

    String ADD_NUMBER = "update users set phone_number = ? where id = ?;";

    String SET_STATUS = "update users set user_state = ? where id = ?;";

    Optional<User> findById(Long id) throws SQLException;

    UserState getUserState(Long userId) throws SQLException;

    void addPhoneNumber(Long userId, String phoneNumber) throws SQLException;

    void setStatus(Long userId, UserState state) throws SQLException;
}
