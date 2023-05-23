package org.example.ui;

import org.example.domain.DTO.BaseResponse;
import org.example.domain.model.user.User;
import org.example.domain.model.user.UserRole;

import java.sql.SQLException;
import java.util.UUID;

import static org.example.ui.util.BeanUtil.scanStr;
import static org.example.ui.util.BeanUtil.userService;

public class AuthUi {
    public static void signIn() {
        System.out.print("Enter phoneNumber: ");
        String phoneNumber = scanStr.nextLine();

        System.out.print("Enter password: ");
        String password = scanStr.nextLine();

//        BaseResponse<UUID> response = userService.signIn(phoneNumber, password);
//        System.out.println(response.getMessage());
//        if(response.getStatus() == 777){
//            UserUi.userWindow(response.getData());
//        }
    }
    public static void signUp() {
        System.out.print("Enter name: ");
        String name = scanStr.nextLine();

        System.out.print("Enter phoneNumber: ");
        String phoneNumber = scanStr.nextLine();

        System.out.print("Enter password: ");
        String password = scanStr.nextLine();

        User user = new User()
                .setName(name)
                .setPhoneNumber(phoneNumber);

        try {
            System.out.println(userService.add(user).getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
