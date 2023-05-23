package org.example.domain.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.example.domain.model.BaseModel;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class User {
    private Long id;
    private String name;
    private String phoneNumber;
    private LocalDateTime created;
    private LocalDateTime updated;
    private UserState userState;
    {
        userState = UserState.NEW;
    }
}
