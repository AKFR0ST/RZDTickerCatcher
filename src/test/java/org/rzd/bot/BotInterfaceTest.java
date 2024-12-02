package org.rzd.bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rzd.config.ApplicationConfig;
import org.rzd.model.ApplicationOptions;
import org.rzd.model.Train;
import org.rzd.server.CatchersServer;
import org.rzd.services.LoaderTrains;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatusCode;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
class BotInterfaceTest {
    AnnotationConfigApplicationContext context;
    BotInterface botInterface;
    CatchersServer catchersServerMock;
    ApplicationOptions applicationOptionsMock;
    MessageSender messageSenderMock;
    MessageReceiver messageReceiverMock;
    LoaderTrains loaderTrainsMock;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        messageSenderMock = mock(MessageSender.class);
        messageReceiverMock = mock(MessageReceiver.class);
        loaderTrainsMock = mock(LoaderTrains.class);
        catchersServerMock = mock(CatchersServer.class);
        applicationOptionsMock = mock(ApplicationOptions.class);

        botInterface = new BotInterfaceImpl(catchersServerMock, applicationOptionsMock, messageSenderMock, messageReceiverMock, loaderTrainsMock);

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
        when(catchersServerMock.activeCatchers()).thenReturn("1");
        when(messageSenderMock.sendMessage(any(), any())).thenReturn(HttpStatusCode.valueOf(200));
        when(messageReceiverMock.getTextMessage()).thenReturn("1");
        String response = botInterface.killCatcher(1L);
        Assert.assertEquals(response, "200 OK");
    }

    @Test
    void newCatcher() {
        when(catchersServerMock.newCatcher(any(), any())).thenReturn(1L);
        Train t1 = new Train("1", "00:00", "00:01", new ArrayList<>());
        ArrayList<Train> trains = new ArrayList<>();
        trains.add(t1);
        when(loaderTrainsMock.getTrainList(any())).thenReturn(trains);
        List<String> stationList = new ArrayList<>();
        stationList.add("Station 2000000");
        when(loaderTrainsMock.getStationList(any())).thenReturn(stationList);
        when(messageSenderMock.sendMessage(any(), any())).thenReturn(HttpStatusCode.valueOf(200));
        when(messageReceiverMock.getTextMessage()).thenReturn("1");
        Assert.assertEquals(botInterface.newCatcher(0L), 1L);
    }

    @Test
    void start() {
    }
}