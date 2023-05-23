package org.example.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
public abstract class BaseModel {
    protected UUID id;
    protected LocalDateTime createdDate;
    protected LocalDateTime updatedDate;
}
