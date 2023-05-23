package org.example.repository.card;

import org.example.domain.model.Transaction;
import org.example.domain.model.card.Card;
import org.example.domain.model.card.CardType;
import org.example.ui.util.BeanUtil;

import java.sql.*;
import java.util.*;

public class CardRepositoryImpl implements CardRepository{

    private final Connection connection = BeanUtil.getConnection();
    private static CardRepositoryImpl cardRepository;
    public static CardRepositoryImpl getInstance(){
        if(cardRepository == null){
            cardRepository = new CardRepositoryImpl();
        }
        return cardRepository;
    }

    @Override
    public int save(Card card) throws SQLException {
        PreparedStatement getCardNumber = connection.prepareStatement(GET_CARD_NUMBER);
        getCardNumber.setLong(1, card.getOwnerId());
        getCardNumber.execute();
        ResultSet resultSet = getCardNumber.getResultSet();
        String carNumber = null;
        if(resultSet.next()){
            carNumber = resultSet.getString(1);
        }
        PreparedStatement save = connection.prepareStatement(INSERT);
        save.setString(1, carNumber);
        save.setString(2, card.getType().toString());
        save.setString(3, card.getOwnerId().toString());
        save.execute();
        return 0;
    }

    public String doTransaction(UUID senderId, UUID receiverId, double amount) throws SQLException {
        PreparedStatement trans = connection.prepareStatement(TRANSACTION);
        trans.setString(1, senderId.toString());
        trans.setString(2, receiverId.toString());
        trans.setDouble(3, amount);
        trans.execute();
        ResultSet resultSet = trans.getResultSet();
        if(resultSet.next()){
            return resultSet.getString(1);
        }
        else return "empty";
    }

    public List<Card> getReceiverCards(UUID cardId) throws SQLException {
        List<Card> cards = new LinkedList<>();
        PreparedStatement getCards = connection.prepareStatement(GET_RECEIVER_CARDS);
        getCards.setString(1, cardId.toString());
        getCards.execute();
        ResultSet resultSet = getCards.getResultSet();
        while (resultSet.next()){
            cards.add(mapToCard(resultSet));
        }
        return cards;
    }


    public List<Card> findAll(Long userId) throws SQLException {
        PreparedStatement findCards = connection.prepareStatement(FIND_MY_CARDS);
        findCards.setLong(1, userId);
        findCards.execute();
        ResultSet result = findCards.getResultSet();
        ArrayList<Card> cards = new ArrayList<>();
        while (result.next()){
            cards.add(mapToCard(result));
        }
        return cards;
    }

    @Override
    public boolean removeById(UUID id) {
        return false;
    }

    @Override
    public boolean remove(Card card) {
        return false;
    }

    @Override
    public boolean update(Card card) throws SQLException {
        PreparedStatement update = connection.prepareCall(UPDATE);
        update.setString(1, card.getId().toString());
        update.setDouble(2, card.getBalance());
        update.execute();
        return true;
    }


    private Card mapToCard(ResultSet myCards) throws SQLException {
        Card card = new Card();
        System.out.println("Mapptin to card");
        card.setId(UUID.fromString(myCards.getString("id")));
        card.setNumber(myCards.getString("numbers"));
        card.setType(CardType.valueOf(myCards.getString("type")));
        card.setOwnerId(myCards.getLong("owner_id"));
        card.setBalance(myCards.getDouble("balance"));
        card.setCreatedDate(myCards.getTimestamp("created").toLocalDateTime());
        card.setUpdatedDate(myCards.getTimestamp("updated").toLocalDateTime());
        card.setExpiryDate(myCards.getDate("expire_date").toLocalDate());
        return card;
    }

    public List<Transaction> getDebit(Long id) throws SQLException {
        List<Transaction> transactions = new LinkedList<>();
        PreparedStatement debit = connection.prepareStatement(DEBIT);
        debit.setString(1, id.toString());
        debit.execute();
        ResultSet result = debit.getResultSet();

        while (result.next()){
            transactions.add(mapToTransactions(result));
        }
        return transactions;
    }

    private Transaction mapToTransactions(ResultSet result) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setSenderId(result.getLong("sender_id"));
        transaction.setReceiverId(result.getLong("receiver_id"));
        transaction.setAmount(result.getDouble("amount"));
        transaction.setCreatedDate(result.getTimestamp("created").toLocalDateTime());
        return transaction;
    }

    public List<Transaction> getCredit(Long id) throws SQLException {
        List<Transaction> transactions = new LinkedList<>();
        PreparedStatement credit = connection.prepareStatement(CREDIT);
        credit.setString(1, id.toString());
        credit.execute();
        ResultSet result = credit.getResultSet();

        while (result.next()){
            transactions.add(mapToTransactions(result));
        }
        return transactions;
    }

    public String saveUserIdAndCardNumber(Long userId, String cardNumber) throws SQLException {
        PreparedStatement check = connection.prepareStatement(IS_USERID_CARD_NUMBER_EXISTED);
        check.setString(1, cardNumber);
        check.execute();
        ResultSet resultSet = check.getResultSet();
        boolean result = false;
        if(resultSet.next()){
            result = check.getResultSet().getBoolean(1);
        }
        if (result){
            return "This card number already exists in the base";
        }
        PreparedStatement addTempCardNum = connection.prepareStatement(TEMPORARILY_SAVE_CARD_NUMBER);
        addTempCardNum.setLong(1, userId);
        addTempCardNum.setString(2, cardNumber);
        addTempCardNum.execute();
        return "next";
    }
    public String getUserName(Long userId) throws SQLException {
        PreparedStatement username = connection.prepareStatement(GET_USERNAME);
        username.setLong(1, userId);
        username.execute();
        ResultSet resultSet = username.getResultSet();
        String name = null;
        if (resultSet.next()){
            name = resultSet.getString(1);
        }
        return name;
    }

    public String getMyCards(Long userId, int i) throws SQLException {
        List<Card> cards;
        StringBuilder stringBuilder = new StringBuilder();
        if(i == 0 || i == 1){
            cards = findAll(userId);
        }else {
            PreparedStatement senderCard = connection.prepareStatement(GET_SENDER_CARD);
            senderCard.setLong(1, userId);
            senderCard.execute();
            UUID senderId = null;
            ResultSet resultSet = senderCard.getResultSet();
            if (resultSet.next()){
                senderId = UUID.fromString(resultSet.getString(1));
            }
            cards = getReceiverCards(senderId);
        }
        if (cards.isEmpty()){
            return "Cards not found";
        }
        for (int j = 0; j < cards.size(); j++) {
            Card card = cards.get(j);
            String userName = getUserName(card.getOwnerId());
            String printCard =  (j+1) + ". 💳 Card number: " + card.getNumber() +
                    ",\n👨‍💼 Owner name: " + userName +
                    ",\n💵 Balance: " + card.getBalance() +
                    ",\n💳 Card type: " + card.getType() +
                    ",\n📅 Expiration date: " + card.getExpiryDate() +
                    ",\n📅 Created date: " + card.getCreatedDate() + "\n\n";
            stringBuilder.append(printCard);}

        if(i == 1 || i==3){
            stringBuilder.append("☝️Choose a card by its number☝️");
        }
        return stringBuilder.toString();
    }

    public String chooseCard(Long userId, int index, int status) throws SQLException {
        String response = null;
        try {
            if (status == 1){
                List<Card> cards = findAll(userId);
                Card card = cards.get(index - 1);
                UUID id = card.getId();
                PreparedStatement filling = connection.prepareStatement(FILL_BALANCE);
                filling.setLong(1, userId);
                filling.setString(2, id.toString());
                filling.execute();
                response = "next";
            } else if (status == 2) {
                List<Card> cards = findAll(userId);
                Card card = cards.get(index - 1);
                UUID id = card.getId();
                PreparedStatement addSenderCard = connection.prepareStatement(ADD_SENDER_CARD_ID);
                addSenderCard.setLong(1, userId);
                addSenderCard.setString(2, id.toString());
                addSenderCard.execute();
                response = "next";
            }else {
                PreparedStatement senderCard = connection.prepareStatement(GET_SENDER_CARD);
                senderCard.setLong(1, userId);
                senderCard.execute();
                UUID senderId = null;
                ResultSet resultSet = senderCard.getResultSet();
                if (resultSet.next()){
                    senderId = UUID.fromString(resultSet.getString(1));
                }
                List<Card> receiverCards = getReceiverCards(senderId);
                Card card = receiverCards.get(index - 1);
                UUID id = card.getId();
                PreparedStatement addReceiverCard = connection.prepareStatement(ADD_RECEIVER_CARD_ID);
                addReceiverCard.setLong(1, userId);
                addReceiverCard.setString(2, id.toString());
                addReceiverCard.execute();
                response = "next";
            }
        }catch (IndexOutOfBoundsException e){
            response = "You entered wrong number";
        }
        return response;
    }

    public void addBalance(Long userId, int balance) throws SQLException {
        PreparedStatement getCarId = connection.prepareStatement(GET_CARD_ID_FROM_FILL_BALANCE);
        getCarId.setLong(1, userId);
        getCarId.execute();
        ResultSet resultSet = getCarId.getResultSet();
        String cardId = null;
        if (resultSet.next()){
            cardId = resultSet.getString(1);
        }
        PreparedStatement addBalance = connection.prepareCall(ADD_BALANCE);
        addBalance.setDouble(1, balance);
        addBalance.setString(2, cardId);
        addBalance.execute();
    }

    public String sendMoney(Long userId, double amount) throws SQLException {
        PreparedStatement senderCard = connection.prepareStatement(GET_SENDER_CARD);
        senderCard.setLong(1, userId);
        senderCard.execute();
        UUID senderId = null;
        ResultSet resultSet = senderCard.getResultSet();
        if (resultSet.next()){
            senderId = UUID.fromString(resultSet.getString(1));
        }

        PreparedStatement receiverCard = connection.prepareStatement(GET_RECEIVER_CARD);
        receiverCard.setLong(1, userId);
        receiverCard.execute();
        UUID receiverId = null;
        ResultSet resultSet1 = receiverCard.getResultSet();
        if (resultSet1.next()){
            receiverId = UUID.fromString(resultSet1.getString(1));
        }
//

        return doTransaction(senderId, receiverId, amount);
    }

    public String getTransInfo(Long userId, int status) throws SQLException {
        StringBuilder result = new StringBuilder();
        List<Transaction> transactions;
        if(status == 1){
            transactions = getDebit(userId);
            if(transactions.isEmpty()){
                return "You do not have debit transactions";
            }
        }else {
            transactions = getCredit(userId);
            if(transactions.isEmpty()){
                return "You do not have credit transactions";
            }
        }
        for (Transaction transaction : transactions) {
            String senderName = getUserName(transaction.getSenderId());
            String receiverName = getUserName(transaction.getReceiverId());
            String add = "Sender name: " + senderName +
                    ",\n Receiver name: " + receiverName +
                    ",\n Transfer amount: " + transaction.getAmount() +
                    ",\n Date: " + transaction.getCreatedDate() + "\n";
            result.append(add);
        }
        return result.toString();
    }
}
