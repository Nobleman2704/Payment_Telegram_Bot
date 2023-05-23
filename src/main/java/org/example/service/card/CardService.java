package org.example.service.card;

import org.example.domain.DTO.BaseResponse;
import org.example.domain.model.Transaction;
import org.example.domain.model.card.Card;
import org.example.domain.model.card.CardType;
import org.example.service.BaseService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface CardService extends BaseService<Card> {
    ArrayList<CardType> getCardTypes();
    String getMyCards(Long userId, int i) throws SQLException;

    BaseResponse fillCard(double amount, Card id1);

    List<Card> getReceiverCards(UUID cardId) throws SQLException;

    BaseResponse doTransaction(UUID cardId, UUID id, double amount) throws SQLException;

    List<Transaction> getMyTransactions(Long id, int i) throws SQLException;

    String saveUserIdAndCardNumber(Long userId, String cardNumber) throws SQLException;

    String chooseCard(Long userId, String index, int status) throws SQLException;

    String addBalance(Long userId, String amount) throws SQLException;

    String sendMoney(Long userId, String money) throws SQLException;

    String getTransInfo(Long userId, int status) throws SQLException;
}
