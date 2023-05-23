package org.example.service.card;

import org.example.domain.DTO.BaseResponse;
import org.example.domain.model.Transaction;
import org.example.domain.model.card.Card;
import org.example.domain.model.card.CardType;
import org.example.repository.card.CardRepositoryImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardServiceImpl implements CardService {

    private final CardRepositoryImpl cardRepository = CardRepositoryImpl.getInstance();

    @Override
    public String addBalance(Long userId, String amount) throws SQLException {
        try {
            int balance = Integer.parseInt(amount);
            if(balance<=0){
                return "Balance should be positive";
            }
            cardRepository.addBalance(userId, balance);
            return "Money has been added";
        }catch (NumberFormatException e){
            return "You should enter only number";
        }
    }

    @Override
    public String sendMoney(Long userId, String money) throws SQLException {
        String response;
        try {
            double amount = Double.parseDouble(money);
            if(amount <= 0){
                response = "Amount should be positive";
            }
            else {
                response = cardRepository.sendMoney(userId, amount);
            }
        }catch (NumberFormatException e){
            response = "You should enter only number";
        }
        return response;
    }

    @Override
    public String chooseCard(Long userId, String request, int status) throws SQLException {
        try {
            int index = Integer.parseInt(request);
            return cardRepository.chooseCard(userId, index, status);
        }catch (NumberFormatException e){
            return "You should only enter number";
        }
    }

    @Override
    public String getMyCards(Long userId, int i) throws SQLException {
        return cardRepository.getMyCards(userId, i);
    }

    @Override
    public String saveUserIdAndCardNumber(Long userId, String cardNumber) throws SQLException {
        if(cardNumber.matches("\\d{16}")){
            return cardRepository.saveUserIdAndCardNumber(userId, cardNumber);
        }
        return "Card number should be 16 digits";
    }

    private static CardServiceImpl cardService;
    private CardServiceImpl(){}
    public static CardServiceImpl getInstance(){
        if (cardService==null){
            cardService = new CardServiceImpl();
        }
        return cardService;
    }

    @Override
    public BaseResponse<Card> add(Card card) throws SQLException {
        cardRepository.save(card);
        return new BaseResponse<>();
    }

    @Override
    public ArrayList<CardType> getCardTypes() {
        return new ArrayList<>(List.of(
                CardType.VISA,
                CardType.MASTERCARD,
                CardType.HUMO,
                CardType.UZCARD));
    }



    @Override
    public BaseResponse fillCard(double amount, Card card) {
        if(amount <= 0){
            return new BaseResponse()
                    .setMessage("Amount should be positive");
        }
        card.setBalance(card.getBalance() + amount);
        try {
            cardRepository.update(card);
        } catch (SQLException e) {
            return new BaseResponse().setMessage(e.getMessage());
        }
        return new BaseResponse<>().setMessage("Card balance has been updated");
    }

    @Override
    public List<Card> getReceiverCards(UUID cardId) throws SQLException {
        return cardRepository.getReceiverCards(cardId);
    }

    @Override
    public BaseResponse doTransaction(UUID cardId, UUID id, double amount) throws SQLException {
        if (amount <= 0){
            return new BaseResponse<>().setMessage("Amount should be positive");
        }
        String response = cardRepository.doTransaction(cardId, id, amount);
        return new BaseResponse().setMessage(response);
    }

    @Override
    public String getTransInfo(Long userId, int status) throws SQLException {
        return cardRepository.getTransInfo(userId, status);
    }

    @Override
    public List<Transaction> getMyTransactions(Long id, int i) throws SQLException {
        if(i==1){
            return cardRepository.getDebit(id);
        }else {
            return cardRepository.getCredit(id);
        }
    }
}
