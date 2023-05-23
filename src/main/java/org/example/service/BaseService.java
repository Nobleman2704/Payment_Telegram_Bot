package org.example.service;

import org.example.domain.DTO.BaseResponse;

import java.sql.SQLException;

public interface BaseService<T> {
    BaseResponse<T> add(T element) throws SQLException;
}
