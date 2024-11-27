package org.rzd.bot;

public interface MessageSender {
    void sendMessage(Long chatId, String message);
}
