package org.rzd.model;

import org.rzd.services.TicketCatcher;
import org.springframework.context.ApplicationContext;

public class Catcher {
    private Long id;
    private String user;
    private boolean finished;
    private TicketOptions ticketOptions;
    private TicketCatcher ticketCatcher;

    public Catcher(Long id, TicketOptions ticketOptions, ApplicationContext context) {
        this.id = id;
        this.ticketOptions = ticketOptions;
        ticketCatcher = new TicketCatcher(context, ticketOptions);
        ticketCatcher.start();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public TicketOptions getTicketOptions() {
        return ticketOptions;
    }

    public void setTicketOptions(TicketOptions ticketOptions) {
        this.ticketOptions = ticketOptions;
    }

    public TicketCatcher getTicketCatcher() {
        return ticketCatcher;
    }

    public void setTicketCatcher(TicketCatcher ticketCatcher) {
        this.ticketCatcher = ticketCatcher;
    }
}
