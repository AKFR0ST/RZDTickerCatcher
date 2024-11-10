package org.rzd.core;

import org.springframework.stereotype.Component;

@Component("TicketCatcher")
public class TicketCatcher {
    //  Контекст
    //  Забирает параметры что нужно поймать - станции, дата, номер поезда(+много поездов), класс
    //  Вызвать лоадер поездов и пройтись по списку в поисках нужного

    public TicketCatcher() {

    }
}
