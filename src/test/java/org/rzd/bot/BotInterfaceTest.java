package org.rzd.bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rzd.config.ApplicationConfig;
import org.rzd.config.ApplicationConfigTest;
import org.rzd.model.ApplicationOptions;
import org.rzd.model.TicketOptions;
import org.rzd.server.CatchersServer;
import org.rzd.server.CatchersServerImpl;
import org.rzd.server.CatchersServerMockImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatusCode;
import org.testng.Assert;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//@ActiveProfiles("dev")
class BotInterfaceTest {
    AnnotationConfigApplicationContext context;
    BotInterface botInterface;
    CatchersServer catchersServerMock;
    ApplicationOptions applicationOptionsMock;
    MessageSender messageSenderMock;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
//
        //        CatchersServer server = context.getBean(CatchersServer.class);
//        ApplicationOptions applicationOptions = context.getBean("getApplicationOptions", ApplicationOptions.class);
//        MessageSender sender = context.getBean("getMessageSender", MessageSender.class);


        catchersServerMock = mock(CatchersServerImpl.class);
        applicationOptionsMock = mock(ApplicationOptions.class);
        messageSenderMock = mock(MessageSender.class);

        botInterface = new BotInterfaceImpl(catchersServerMock,applicationOptionsMock, messageSenderMock);

    }

    @Test
    void allCatchers() {
        when(catchersServerMock.allCatchers()).thenReturn("200 OK");
        when(messageSenderMock.sendMessage(0L, "200 OK")).thenReturn(HttpStatusCode.valueOf(200));
        Assert.assertEquals(botInterface.allCatchers(0L), "200 OK");
    }

    @Test
    void activeCatchers() {
        when(catchersServerMock.activeCatchers()).thenReturn("200 OK");
        when(messageSenderMock.sendMessage(0L, "200 OK")).thenReturn(HttpStatusCode.valueOf(200));
        Assert.assertEquals(botInterface.activeCatchers(0L), "200 OK");
    }

    @Test
    void killCatcher() {
        when(catchersServerMock.killCatcherById(1L)).thenReturn(0);
        when(messageSenderMock.sendMessage(1L, "200 OK")).thenReturn(HttpStatusCode.valueOf(200));
//        when()
        //TODO Create Test
        Assert.assertEquals(botInterface.killCatcher(1L), "200 OK");
    }

    @Test
    void newCatcher() {
        when(catchersServerMock.newCatcher(new TicketOptions("0", "0", "0", "0", "0", 0L), 0L)).thenReturn("\"Catcher 0 successfully created\"");
        when(messageSenderMock.sendMessage(0L, "200 OK")).thenReturn(HttpStatusCode.valueOf(200));
        //TODO Create Test
        Assert.assertEquals(botInterface.newCatcher(0L), "\"Catcher 0 successfully created\"");
    }

    @Test
    void start() {
    }
}