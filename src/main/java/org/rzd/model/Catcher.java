package org.rzd.model;

import org.rzd.services.TicketCatcher;
import org.springframework.context.ApplicationContext;

public class Catcher {
    private Long id;
    private Long chatId;
    private TicketOptions ticketOptions;
    private TicketCatcher ticketCatcher;

    public Catcher(Long id, Long chatId, TicketOptions ticketOptions, ApplicationContext context) {
        this.id = id;
        this.chatId = chatId;
        this.ticketOptions = ticketOptions;
        ticketCatcher = new TicketCatcher(context, ticketOptions, chatId);
        ticketCatcher.start();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
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
