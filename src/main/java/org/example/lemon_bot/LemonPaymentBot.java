package org.example.lemon_bot;

import org.example.domain.model.user.UserState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class LemonPaymentBot extends TelegramLongPollingBot {
    private final LemonService lemonService = LemonService.getInstance();
    static ExecutorService executorService = Executors.newCachedThreadPool();
    @Override
    public void onUpdateReceived(Update update) {
        executorService.execute(() -> {
            try {
                if (update.hasCallbackQuery()) {
                    CallbackQuery callbackQuery = update.getCallbackQuery();
                    Long userId = callbackQuery.getMessage().getChatId();
                    String data = callbackQuery.getData();


                    UserState userState = lemonService.getUserState(userId);
                    String response = null;

                    switch (userState) {
                        case ADD_CARD_TYPE -> {
                            response = "Card has successfully been added 😊";
                            lemonService.setStatus(userId, UserState.REGISTERED);
                            lemonService.addCard(userId, data);
                        }
                        case GET_TRANS -> {
                            response = lemonService.getTransInfo(userId, Integer.parseInt(data));
                            lemonService.setStatus(userId, UserState.REGISTERED);
                        }
                    }
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(userId);
                    sendMessage.setText(response);
                    execute(sendMessage);
                } else {
                    Message message = update.getMessage();
                    Long userId = message.getChatId();
                    String request = message.getText();

                    if (!lemonService.isExist(userId)) {
                        User user = message.getFrom();
                        String userName = user.getFirstName() + " " + user.getLastName();
                        lemonService.addUser(userId, userName);
                    }
                    UserState userState = lemonService.getUserState(userId);


                    ReplyKeyboard replyKeyboardMarkup = null;
                    String response = null;

                    switch (userState) {
                        case NEW -> {
                            if (message.hasContact()) {
                                lemonService.addPhoneNumber(userId, message.getContact().getPhoneNumber());
                                response = "You have been successfully registered 🙂";
                                replyKeyboardMarkup = lemonService.showMenu();
                            } else {
                                response = "please share your contact";
                                replyKeyboardMarkup = lemonService.shareContact();
                            }
                        }
                        case REGISTERED -> {
                            userState = lemonService.navigateMenu(request);
                            lemonService.setStatus(userId, userState);
                            switch (userState) {
                                case ADD_CARD -> {
                                    response = "Please enter card numbers (numbers should be exactly 16 digits)";
                                }
                                case GET_MY_CARDS -> {
                                    response = lemonService.getCards(userId, 0);
                                    lemonService.setStatus(userId, UserState.REGISTERED);
                                }
                                case FILL_MY_CARDS, MAKE_TRANS -> {
                                    if (userState.equals(UserState.FILL_MY_CARDS)) {
                                        lemonService.setStatus(userId, UserState.FILL_CARD);
                                    } else {
                                        lemonService.setStatus(userId, UserState.CHOOSE_SENDER_CARD);
                                    }
                                    response = lemonService.getCards(userId, 1);
                                }
                                case GET_TRANS_TYPE -> {
                                    response = "Choose one";
                                    replyKeyboardMarkup = lemonService.getTransType();
                                    lemonService.setStatus(userId, UserState.GET_TRANS);
                                }
                                default -> {
                                    response = "You are in Menu";
                                    replyKeyboardMarkup = lemonService.showMenu();
                                }
                            }
                        }
                        case FILL_CARD -> {
                            String status = lemonService.chooseCard(userId, request, 1);
                            if (status.equals("next")) {
                                response = "Please enter number to fill your card (number should be positive amount > 0)";
                                lemonService.setStatus(userId, UserState.ADD_BALANCE);
                            } else {
                                lemonService.setStatus(userId, UserState.REGISTERED);
                                response = status;
                            }
                        }
                        case ADD_BALANCE -> {
                            response = lemonService.addBalance(userId, request);
                            lemonService.setStatus(userId, UserState.REGISTERED);
                        }
                        case CHOOSE_SENDER_CARD -> {
                            String status = lemonService.chooseCard(userId, request, 2);
                            if (status.equals("next")) {
                                response = lemonService.getCards(userId, 3);
                                lemonService.setStatus(userId, UserState.CHOOSE_RECEIVER_CARD);
                            } else {
                                response = status;
                                lemonService.setStatus(userId, UserState.REGISTERED);
                            }
                        }
                        case CHOOSE_RECEIVER_CARD -> {
                            String status = lemonService.chooseCard(userId, request, 3);
                            if (status.equals("next")) {
                                response = "Please enter number to send money (number should be positive amount > 0)";
                                lemonService.setStatus(userId, UserState.SEND_MONEY);
                            } else {
                                response = status;
                                lemonService.setStatus(userId, UserState.REGISTERED);
                            }
                        }
                        case SEND_MONEY -> {
                            response = lemonService.sendMoney(userId, request);
                            lemonService.setStatus(userId, UserState.REGISTERED);
                        }
                        case MAKE_TRANS -> {
                            response = lemonService.getCards(userId, 2);
                        }
                        case ADD_CARD -> {
                            String status = lemonService.saveUserIdAndCardNumber(userId, request);
                            if (status.equals("next")) {
                                System.out.println("In next");
                                response = "Please choose a card type below";
                                replyKeyboardMarkup = lemonService.getCardTypes();
                                lemonService.setStatus(userId, UserState.ADD_CARD_TYPE);
                            } else {
                                System.out.println("in Not next");
                                lemonService.setStatus(userId, UserState.REGISTERED);
                                response = status;
                            }
                        }
                        case GET_TRANS, ADD_CARD_TYPE -> {
                            response = "Wrong button";
                            lemonService.setStatus(userId, UserState.REGISTERED);
                        }
                    }
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setReplyMarkup(replyKeyboardMarkup);
                    sendMessage.setChatId(userId);
                    sendMessage.setText(response);
                    execute(sendMessage);
                }
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public String getBotUsername() {
        return "https://t.me/asadbek2704_bot"   ;
    }

    @Override
    public String getBotToken() {
        return "5726241106:AAFcaYK-G1UvhAoROM_-SY_0-hSF5AscuvQ";
    }
}
