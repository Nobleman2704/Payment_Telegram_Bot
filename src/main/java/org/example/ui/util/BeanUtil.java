package org.example.ui.util;

import org.example.service.card.CardService;
import org.example.service.card.CardServiceImpl;
import org.example.service.user.UserService;
import org.example.service.user.UserServiceImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BeanUtil {
    private static Connection connection;
    public static Scanner scanStr = new Scanner(System.in);
    public static Scanner scanInt = new Scanner(System.in);
    public static UserService userService = UserServiceImpl.getUserService();
    public static CardService cardService = CardServiceImpl.getInstance();

    public static Connection getConnection() {
        if(connection == null) {
            try {
                connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/lemon_payment", "postgres", "Asadbek"
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }
}
