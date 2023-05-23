package org.example.ui;

import org.example.ui.util.BeanUtil;

public class MainUI {
    public static void runMain(){
        while (true){
            String choice = choose();
            switch (choice){
                case "1" -> AuthUi.signIn();
                case "2" -> AuthUi.signUp();
                case "0" -> {return;}
            }
        }
    }
    private static String choose() {
        System.out.println("""
                1. Sign in
                2. Sign up
                0. Quit
                Choose one:""");
        return BeanUtil.scanStr.nextLine();
    }
}
