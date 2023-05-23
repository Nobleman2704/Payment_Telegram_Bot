package org.example.lemon_bot;

import org.example.domain.model.card.CardType;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.LinkedList;
import java.util.List;

public class MarkupService {
    private MarkupService(){}
    private static MarkupService markupService;
    public static MarkupService getInstance(){
        if (markupService == null){
            markupService = new MarkupService();
        }
        return markupService;
    }


    public ReplyKeyboardMarkup shareContact() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new LinkedList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setRequestContact(true);
        keyboardButton.setText("☎️ Please share you phone number to get registered ☎️");
        keyboardRow.add(keyboardButton);
        keyboardRows.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup showMenu() {
        ReplyKeyboardMarkup menu = new ReplyKeyboardMarkup();
        menu.setResizeKeyboard(true);
        KeyboardRow addCard = new KeyboardRow();
        addCard.add("➕ Add Card 💳");
        KeyboardRow getCards = new KeyboardRow();
        getCards.add("🔎 Get Your cards 💳");
        KeyboardRow fillCard = new KeyboardRow();
        fillCard.add("🤑 Fill Card 💳");
        KeyboardRow makeTrans = new KeyboardRow();
        makeTrans.add("💱 Make transaction 💳");
        KeyboardRow getTrans = new KeyboardRow();
        getTrans.add("🗒️ Get Transactions 💳");
        List<KeyboardRow> keyboardRows = List.of(
                addCard,
                getCards,
                fillCard,
                makeTrans,
                getTrans);
        menu.setKeyboard(keyboardRows);
        return menu;
    }

    public InlineKeyboardMarkup getCardTypes() {
        InlineKeyboardButton humoButton = new InlineKeyboardButton("Humo");
        humoButton.setCallbackData(CardType.HUMO.toString());
        InlineKeyboardButton uzCardButton = new InlineKeyboardButton("UzCard");
        uzCardButton.setCallbackData(CardType.UZCARD.toString());
        InlineKeyboardButton masterCardButton = new InlineKeyboardButton("MasterCard");
        masterCardButton.setCallbackData(CardType.MASTERCARD.toString());
        InlineKeyboardButton visaButton = new InlineKeyboardButton("Visa");
        visaButton.setCallbackData(CardType.VISA.toString());
        List<InlineKeyboardButton> inlineKeyboardButtons = List.of(
                humoButton, uzCardButton, masterCardButton, visaButton
        );
        List<List<InlineKeyboardButton>> buttons = List.of(inlineKeyboardButtons);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getTransTypes() {
        InlineKeyboardButton humoButton = new InlineKeyboardButton("Get debit");
        humoButton.setCallbackData("1");
        InlineKeyboardButton uzCardButton = new InlineKeyboardButton("Get Credit");
        uzCardButton.setCallbackData("2");
        List<InlineKeyboardButton> inlineKeyboardButtons = List.of(
                humoButton, uzCardButton
        );
        List<List<InlineKeyboardButton>> buttons = List.of(inlineKeyboardButtons);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }
}
