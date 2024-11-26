package org.rzd.bot;

public interface BotInterface {
    void allCatchers(Long chatId);
    void activeCatchers(Long chatId);
    void killCatcher(Long chatId);
    void newCatcher(Long chatId);
}
