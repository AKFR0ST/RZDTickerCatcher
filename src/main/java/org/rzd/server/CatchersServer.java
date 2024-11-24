package org.rzd.server;

import org.rzd.model.TicketOptions;

public interface CatchersServer {
    public void start();
    public void stop();
    public void newCatcher(TicketOptions ticketOptions);
    public void activeCatchers();
    public void allCatchers();
    public void killCatcherById(Long id);
}
