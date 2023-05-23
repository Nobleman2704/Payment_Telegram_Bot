package org.example.lemon_bot;

import org.example.domain.DTO.BaseResponse;
import org.example.domain.model.card.Card;
import org.example.domain.model.card.CardType;
import org.example.domain.model.user.User;
import org.example.domain.model.user.UserState;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.sql.SQLException;

import static org.example.ui.util.BeanUtil.cardService;
import static org.example.ui.util.BeanUtil.userService;

public class LemonService {
    private final MarkupService markupService = MarkupService.getInstance();
    private LemonService(){}
    private static LemonService lemonService;
    public static LemonService getInstance(){
        if(lemonService == null){
            return lemonService = new LemonService();
        }
        return lemonService;
    }

    public boolean isExist(Long userId) {
        return userService.isExists(userId);
    }

    public void addUser(Long userId, String userName) {
        User user = new User();
        user.setName(userName);
        user.setId(userId);
        try {
            userService.add(user);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public UserState getUserState(Long userId) {
        try {
            return userService.getUserState(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ReplyKeyboardMarkup shareContact() {
        return markupService.shareContact();
    }

    public void addPhoneNumber(Long userId, String phoneNumber) {
        try {
            userService.addPhoneNumber(userId, phoneNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ReplyKeyboardMarkup showMenu() {
        return markupService.showMenu();
    }

    public void setStatus(Long userId, UserState state) {
        try {
            userService.setStatus(userId, state);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public UserState navigateMenu(String request) {
        UserState userState = UserState.REGISTERED;
        switch (request){
            case "➕ Add Card \uD83D\uDCB3" -> userState = UserState.ADD_CARD;
            case "\uD83D\uDD0E Get Your cards \uD83D\uDCB3"->userState = UserState.GET_MY_CARDS;
            case "\uD83E\uDD11 Fill Card \uD83D\uDCB3"->userState = UserState.FILL_MY_CARDS;
            case "\uD83D\uDCB1 Make transaction \uD83D\uDCB3"->userState = UserState.MAKE_TRANS;
            case "\uD83D\uDDD2️ Get Transactions \uD83D\uDCB3"->userState = UserState.GET_TRANS_TYPE;
        }
        return userState;
    }

    public InlineKeyboardMarkup getCardTypes() {
        return markupService.getCardTypes();
    }

    public String getCards(Long userId, int i)  {
        try {
            return cardService.getMyCards(userId, i);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public InlineKeyboardMarkup getTransType() {
        return markupService.getTransTypes();
    }

    public String saveUserIdAndCardNumber(Long userId, String cardNumber) {
        try {
            return cardService.saveUserIdAndCardNumber(userId, cardNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addCard(Long userId, String number) {
        Card card = new Card();
        card.setOwnerId(userId);
        card.setType(CardType.valueOf(number));
        try {
            BaseResponse<Card> add = cardService.add(card);
            System.out.println(add.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String chooseCard(Long userId, String index, int status) {
        try {
            return cardService.chooseCard(userId, index, status);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String addBalance(Long userId, String amount) {
        try {
            return cardService.addBalance(userId, amount);
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public String sendMoney(Long userId, String money) {
        try {
            return cardService.sendMoney(userId, money);
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public String getTransInfo(Long userId, int status) {
        try {
            return cardService.getTransInfo(userId, status);
        } catch (SQLException e) {
            return e.getMessage();
        }
    }
}
