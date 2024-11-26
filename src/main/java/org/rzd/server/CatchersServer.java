package org.rzd.server;

import org.rzd.model.TicketOptions;

public interface CatchersServer {
    void start();
    void stop();
    void newCatcher(TicketOptions ticketOptions, Long chatId);
    String activeCatchers();
    String allCatchers();
    int killCatcherById(Long id);
    void killAllCatchers();
}
