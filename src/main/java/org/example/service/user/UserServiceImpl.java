package org.example.service.user;

import org.example.domain.DTO.BaseResponse;
import org.example.domain.model.user.User;
import org.example.domain.model.user.UserState;
import org.example.repository.user.UserRepository;
import org.example.repository.user.UserRepositoryImpl;

import java.sql.SQLException;


public class UserServiceImpl implements UserService{
    private final UserRepository userRepository = UserRepositoryImpl.getInstance();
    private static UserServiceImpl userService;
    public static UserServiceImpl getUserService(){
        if (userService==null){
            userService = new UserServiceImpl();
        }
        return userService;
    }
    private UserServiceImpl(){}
//    public BaseResponse<User> add(User user) {
//        int status;
//        String message;
//        String name = user.getName();
//        String phoneNumber = user.getPhoneNumber();
//        if(name == null || name.isBlank()){
//            status = 684;
//            message = "Valid name required";
//        }else if (phoneNumber == null || phoneNumber.isBlank()) {
//            status = 595;
//            message = "Valid phone number required";
//        }else {
//            try {
//                userRepository.save(user);
//                status = 888;
//                message = "user has successfully been added";
//            } catch (SQLException e) {
//                return new BaseResponse<>(e.getMessage(), 400, null);
//            }
//        }
//        return new BaseResponse<User>()
//                .setMessage(message)
//                .setStatus(status);
//    }

//    public BaseResponse<UUID> signIn(String phoneNumber, String password){
//        String message;
//        int status;
//        UUID userId;
//        if(phoneNumber == null || phoneNumber.isBlank()){
//            status = 879;
//            message = "Valid phone number required";
//        } else if (password == null || password.isBlank()) {
//            status = 997;
//            message = "Valid password number required";
//        } else {
//            try {
//                Optional<User> userBox = userRepository.findByPhoneNumber(phoneNumber);
//                if(userBox.isPresent()){
//                    User user = userBox.get();
//                    if(Objects.equals(user.getPassword(), password)){
//                        status = 777;
//                        message = "User has successfully signed in";
//                        userId = user.getId();
//                        return new BaseResponse<UUID>()
//                                .setData(userId)
//                                .setMessage(message)
//                                .setStatus(status);
//                    }
//                    else {
//                        status = 984;
//                        message = "User password did not match";
//                    }
//                }else {
//                    status = 467;
//                    message = "User not fount";
//                }
//            } catch (SQLException e) {
//                status = 988;
//                message = e.getMessage();
//            }
//        }
//
//        return new BaseResponse<UUID>()
//                .setStatus(status)
//                .setMessage(message);
//    }

    @Override
    public boolean isExists(Long id){
        try {
            return userRepository.findById(id).isPresent();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public void addPhoneNumber(Long userId, String phoneNumber) throws SQLException {
        userRepository.addPhoneNumber(userId, phoneNumber);
    }


    @Override
    public void setStatus(Long userId, UserState state) throws SQLException {
        userRepository.setStatus(userId, state);
    }

    @Override
    public UserState getUserState(Long userId) throws SQLException {
        return userRepository.getUserState(userId);
    }

    @Override
    public BaseResponse<User> add(User user) throws SQLException {
        userRepository.save(user);
        return new BaseResponse<>();
    }
}
