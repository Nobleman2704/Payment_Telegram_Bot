package org.example.repository.user;

import org.example.domain.model.user.User;
import org.example.domain.model.user.UserState;
import org.example.ui.util.BeanUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryImpl implements UserRepository {
    private final Connection connection = BeanUtil.getConnection();
    private static UserRepositoryImpl instance;
    public static UserRepositoryImpl getInstance() {
        if(instance == null){
            instance = new UserRepositoryImpl();
        }
        return instance;
    }
    private UserRepositoryImpl() {
    }

    @Override
    public UserState getUserState(Long userId) throws SQLException {
        PreparedStatement state = connection.prepareStatement(USER_STATE);
        state.setLong(1, userId);
        state.execute();
        ResultSet resultSet = state.getResultSet();
        if(resultSet.next()){
            return UserState.valueOf(resultSet.getString(1));
        }
        return null;
    }

    @Override
    public void setStatus(Long userId, UserState state) throws SQLException {
        PreparedStatement statusUpdate = connection.prepareStatement(SET_STATUS);
        statusUpdate.setString(1, state.toString());
        statusUpdate.setLong(2, userId);
        statusUpdate.execute();
    }

    @Override
    public void addPhoneNumber(Long userId, String phoneNumber) throws SQLException {
        PreparedStatement addNumber = connection.prepareStatement(ADD_NUMBER);
        addNumber.setString(1, phoneNumber);
        addNumber.setLong(2, userId);
        setStatus(userId, UserState.REGISTERED);
        addNumber.execute();
    }

    @Override
    public int save(User user) throws SQLException {
        PreparedStatement save = connection.prepareStatement(INSERT);

        save.setLong(1, user.getId());
        save.setString(2, user.getName());
        save.setString(3, user.getUserState().toString());
        save.execute();
        return 0;
    }

    @Override
    public Optional<User> findById(Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID);
        preparedStatement.setLong(1, id);
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            User user = mapToUser(resultSet);
            return Optional.of(user);
        }
        return Optional.empty();
    }
    public Optional<User> findByPhoneNumber(String phoneNumber) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_PHONE_NUMBER);
        preparedStatement.setString(1, phoneNumber);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            User user = mapToUser(resultSet);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public boolean removeById(UUID id) {
        return false;
    }

    @Override
    public boolean remove(User user) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    private User mapToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("name"));
        user.setPhoneNumber(resultSet.getString("phone_number"));
        user.setUserState(UserState.valueOf(resultSet.getString("user_state")));
        user.setUserState(UserState.valueOf(resultSet.getString("user_state")));
        user.setCreated(resultSet.getTimestamp("created").toLocalDateTime());
        user.setUpdated(resultSet.getTimestamp("updated").toLocalDateTime());
        return user;
    }
}
