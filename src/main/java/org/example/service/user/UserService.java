package org.example.service.user;

import org.example.domain.DTO.BaseResponse;
import org.example.domain.model.user.User;
import org.example.domain.model.user.UserState;
import org.example.service.BaseService;

import java.sql.SQLException;
import java.util.UUID;

public interface UserService extends BaseService<User> {
//    BaseResponse<UUID> signIn(String phoneNumber, String password);

    boolean isExists(Long userId);

    UserState getUserState(Long userId) throws SQLException;

    void addPhoneNumber(Long userId, String phoneNumber) throws SQLException;

    void setStatus(Long userId, UserState registered) throws SQLException;

}
