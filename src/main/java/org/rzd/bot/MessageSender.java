package org.rzd.bot;

import org.springframework.http.HttpStatusCode;

public interface MessageSender {
    HttpStatusCode sendMessage(Long chatId, String message);
}
