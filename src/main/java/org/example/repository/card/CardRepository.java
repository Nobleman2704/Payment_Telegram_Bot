package org.example.repository.card;

import org.example.domain.model.card.Card;
import org.example.repository.BaseRepository;


public interface CardRepository extends BaseRepository<Card> {
    String INSERT = """
            insert into cards(numbers, type, owner_id)
            VALUES (?,?,?::bigint);""";
    String UPDATE = "call update_card(?::uuid, ?);";
    String TEMPORARILY_SAVE_CARD_NUMBER = "call add_userId_card_number(?::bigint, ? );";
    String FIND_MY_CARDS = "select * from cards where owner_id = ?::bigint;";
    String ADD_SENDER_CARD_ID = "call add_sender_in_trans_table(?::bigint, ?::uuid);";

    String ADD_RECEIVER_CARD_ID = "call add_receiver_in_trans_table(?::bigint, ?::uuid);";
    String GET_USERNAME = "select name from users where id = ?::bigint;";
    String TRANSACTION = "select transaction(?::uuid,?::uuid,?)";
    String FILL_BALANCE = "call fill_card_balance(?::bigint, ?::uuid);";
    String IS_USERID_CARD_NUMBER_EXISTED = "select is_userId_card_number_existed(?);";
    String GET_CARD_NUMBER = "select card_number from add_cards where user_id = ?::bigint;";
    String GET_RECEIVER_CARDS = "select * from cards where id<>?::uuid;";

    String GET_SENDER_CARD = "select sender_card_id from trans_table where user_id = ?::bigint;";

    String GET_RECEIVER_CARD = "select receiver_card_id from trans_table where user_id = ?::bigint;";
    String DEBIT = "select * from transactions where receiver_id = ?::bigint;";
    String CREDIT = "select * from transactions where sender_id = ?::bigint;";

    String GET_CARD_ID_FROM_FILL_BALANCE = "select card_id from fill_balance where user_id = ?::bigint;";

    String ADD_BALANCE = "update cards set balance = balance + ? where id = ?::uuid;";
}
