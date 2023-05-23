//package org.example.ui;
//
//import org.example.domain.DTO.BaseResponse;
//import org.example.domain.model.Transaction;
//import org.example.domain.model.card.Card;
//import org.example.domain.model.card.CardType;
//
//import java.sql.SQLException;
//import java.util.*;
//
//import static org.example.ui.util.BeanUtil.*;
//
//public class UserUi {
//    public static void userWindow(UUID id){
//        while (true){
//            String choice = choose();
//            switch (choice){
//                case "1"-> addCard(id);
//                case "2"-> findCard(id);
//                case "3"-> fillCard(id);
//                case "4"-> doTransaction(id);
//                case "5"-> getMyTransactions(id);
//                case "0"-> {return;}
//            }
//        }
//    }
//
//    private static void getMyTransactions(UUID id) {
//        while (true){
//            System.out.println("""
//                1. DEBIT
//                2. CREDIT
//               """);
//            String choice = scanStr.nextLine();
//            try {
//                switch (choice){
//                    case "1"->{
//                        List<Transaction> debitTrans = cardService.getMyTransactions(id, 1);
//                        if(debitTrans.isEmpty()){
//                            System.out.println("There is no any debit transactions");
//                            return;
//                        }
//                        for (Transaction transaction : debitTrans) {
//                            System.out.printf("""
//                                    sender id: %s, transfer amount: %s, date: %s
//                                    """
//                            , transaction.getSenderId(), transaction.getAmount(), transaction.getCreatedDate());
//                        }
//                        return;
//                    }
//                    case "2"->{
//                        List<Transaction> creditTrans = cardService.getMyTransactions(id, 0);
//                        if(creditTrans.isEmpty()){
//                            System.out.println("There is no any credit transactions");
//                            return;
//                        }
//                        for (Transaction transaction : creditTrans) {
//                            System.out.printf("""
//                                    receiver id: %s, transfer amount: %s, date: %s
//                                    """
//                                    , transaction.getReceiverId(), transaction.getAmount(), transaction.getCreatedDate());
//                        }
//                        return;
//                    }
//                }
//                System.out.println();
//                System.out.println();
//            } catch (SQLException e) {
//                System.out.println(e.getMessage());
//            }
//
//        }
//    }
//
//    private static void doTransaction(UUID id) {
//        List<Card> cards;
//        try {
//            cards = cardService.getMyCards(id);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            return;
//        }
//        if(cards.isEmpty()){
//            System.out.println("You do not have any card yet");
//            return;
//        }
//
//        for (int i = 0; i < cards.size(); i++) {
//            Card card = cards.get(i);
//            System.out.printf("""
//                    %s. %s card number, type: %s, %s balance, expire date %s
//                    """, (i+1),
//                    card.getNumber(), card.getType(), card.getBalance(), card.getExpiryDate());
//        }
//        try {
//            System.out.println("\nChoose card by its number");
//            int index = scanInt.nextInt();
//            Card card = cards.get(index - 1);
//
//            chooseReceiverCards(card.getId());
//
//        }catch (IndexOutOfBoundsException | InputMismatchException e){
//            System.out.println("Wrong button");
//        }
//    }
//
//    private static void chooseReceiverCards(UUID cardId) {
//
//        try {
//            List<Card> cards = cardService.getReceiverCards(cardId);
//            if (cards.isEmpty()){
//                System.out.println("there is no receiver cards");
//                return;
//            }
//
//            for (int i = 0; i < cards.size(); i++) {
//                Card card = cards.get(i);
//                System.out.printf("""
//                    %s. %s card number, type: %s, %s balance, expire date %s""", (i+1),
//                        card.getNumber(), card.getType(), card.getBalance(), card.getExpiryDate());
//            }
//            try {
//                System.out.println("Choose card by its number");
//                int index = scanInt.nextInt();
//                Card card = cards.get(index - 1);
//
//                System.out.println("Write amount");
//                double amount = scanInt.nextDouble();
//
//                BaseResponse fillingResponse = cardService.doTransaction(cardId, card.getId(), amount);
//                System.out.println(fillingResponse.getMessage());
//
//            }catch (IndexOutOfBoundsException | InputMismatchException e){
//                System.out.println("Wrong button");
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private static void fillCard(UUID id) {
//        List<Card> cards;
//        try {
//            cards = cardService.getMyCards(id);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            return;
//        }
//        if(cards.isEmpty()){
//            System.out.println("You do not have any card yet");
//            return;
//        }
//        for (int i = 0; i < cards.size(); i++) {
//            Card card = cards.get(i);
//            System.out.printf("""
//                    %s. %s card number, type: %s, %s balance, expire date %s""", (i+1),
//                    card.getNumber(), card.getType(), card.getBalance(), card.getExpiryDate());
//        }
//        try {
//            System.out.println("Choose card by its number");
//            int index = scanInt.nextInt();
//            Card card = cards.get(index - 1);
//
//            System.out.println("Write amount");
//            double amount = scanInt.nextDouble();
//
//            BaseResponse fillingResponse = cardService.fillCard(amount, card);
//            System.out.println(fillingResponse.getMessage());
//
//        }catch (IndexOutOfBoundsException | InputMismatchException e){
//            System.out.println("Wrong button");
//        }
//    }
//
//    private static void findCard(UUID id) {
//        List<Card> cards;
//        try {
//            cards = cardService.getMyCards(id);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            return;
//        }
//        if(cards.isEmpty()){
//            System.out.println("You do not have any card yet");
//            return;
//        }
//        for (Card card : cards) {
//            System.out.printf("""
//                   card number: %s, type: %s, %s balance, expire date %s
//                    """,
//                    card.getNumber(), card.getType(), card.getBalance(), card.getExpiryDate());
//        }
//        System.out.println();
//    }
//
//    private static void addCard(UUID id) {
//        System.out.println("Write card number");
//        String cardNumber = scanStr.nextLine();
//
//        ArrayList<CardType> cardTypes = cardService.getCardTypes();
//
//        for (int i = 0; i < cardTypes.size(); i++) {
//            System.out.println((i+1) + ". " + cardTypes.get(i));
//        }
//        try {
//            System.out.println("Choose card type by its number");
//            int index = scanInt.nextInt();
//            CardType cardType = cardTypes.get(index - 1);
//
//            Card card = new Card()
//                    .setNumber(cardNumber)
//                    .setType(cardType);
////                    .setOwnerId(id);
//
//            BaseResponse<Card> cardResponse;
//            try {
//                cardResponse = cardService.add(card);
//                System.out.println(cardResponse.getMessage());
//
//            } catch (SQLException e) {
//                System.out.println(e.getMessage());
//            }
//
//        }catch (IndexOutOfBoundsException | InputMismatchException e){
//            System.out.println("Wrong button");
//        }
//    }
//
//
//    private static String choose(){
//        System.out.println("""
//                1. Add card
//                2. Find my cards
//                3. Fill my card
//                4. Make transaction
//                5. Show my transactions
//                0. Back
//                Choose one:""");
//        return scanStr.nextLine();
//    }
//}
