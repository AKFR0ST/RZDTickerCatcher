package org.rzd.bot;

import org.springframework.stereotype.Component;

@Component("BotInterface")
public interface BotInterface {
    String allCatchers(Long chatId);

    String activeCatchers(Long chatId);

    String killCatcher(Long chatId);

    Long newCatcher(Long chatId);

    void start();
}
