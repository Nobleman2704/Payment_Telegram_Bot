package org.example.domain.model.card;

import lombok.*;
import lombok.experimental.Accessors;
import org.example.domain.model.BaseModel;

import java.time.LocalDate;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class Card extends BaseModel {
    private String number;
    private LocalDate expiryDate;
    private Double balance;
    private CardType type;
    private Long ownerId;
}
