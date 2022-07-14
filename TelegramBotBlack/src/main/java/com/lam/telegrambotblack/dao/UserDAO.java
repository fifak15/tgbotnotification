package com.lam.telegrambotblack.dao;

import com.lam.telegrambotblack.TelegramBotBlackApplication;
import com.lam.telegrambotblack.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM users2", new UserMapper());
    }

    public List<User> getUserByTelegramChatID(long telegramChatID) {
        return jdbcTemplate.query("SELECT * FROM users2 WHERE telegram_chat_id=?", new Long[]{telegramChatID}, new UserMapper());
    }

    public void createNewUser(long telegramChatID, long jiraID, String status) {
        jdbcTemplate.update("INSERT INTO users2 (telegram_chat_id, jira_id, status)\n" +
                "VALUES (?, ?, ?)", telegramChatID, jiraID, status);
    }

    public void updateStatus(Long telegramChatID, String userStatus) {
        jdbcTemplate.update("UPDATE users2\n" +
                "SET status=? WHERE telegram_chat_id=?", userStatus, telegramChatID);//  UPDATE tovar SET price=500 WHERE id=5
    }
}