package com.lam.telegrambotblack.dao;

import com.lam.telegrambotblack.models.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();

        user.setTelegramChatID(resultSet.getLong("telegram_chat_id"));
        user.setJIRA_ID(resultSet.getLong("jira_id"));
        user.setStatus(resultSet.getString("status"));
        return user;
    }
}
