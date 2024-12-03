package org.rzd.server;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.*;
import org.rzd.config.ApplicationConfigTest;
import org.rzd.model.Catcher;
import org.rzd.model.TicketOptions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CatchersServerTest {
    static CatchersServerImpl catchersServer;
    static String formattedDate;
    static TicketOptions ticketOptions;


    @BeforeAll
    static void setUp() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ApplicationConfigTest.class);
//        catchersServer = (CatchersServerImpl) applicationContext.getBean("CatchersServerImpl");
        catchersServer = new CatchersServerImpl(applicationContext);

        catchersServer.start();
        LocalDateTime nextDate = LocalDateTime.now().plusDays(1L);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        formattedDate = nextDate.format(formatter);
        ticketOptions = new TicketOptions("2010001", "2000000", formattedDate, "0", 6L, 100L);
    }

    @AfterAll
    static void tearDown() {
        catchersServer.stop();
    }

    @Order(1)
    @Test
    void start() {
        assertNotNull(catchersServer.catchers);
    }

    @Order(2)
    @Test
    void newCatcher() {
        Long id1 = catchersServer.newCatcher(ticketOptions, 519674552L);
        Catcher createdCatcher = null;
        for (Catcher c : catchersServer.catchers) {
            if (Objects.equals(c.getId(), id1)) {
                createdCatcher = c;
            }
        }
        assertNotNull(createdCatcher);
    }

    @Order(3)
    @Test
    void allCatchers() {
        assertEquals(catchersServer.allCatchers(), "Catchers:\nCatcher id:1 State: active " + formattedDate + "\t0\t2010001\t->\t2000000\t6\n");
    }



    @Order(4)
    @Test
    void killCatcherById() {
        Long id = catchersServer.newCatcher(ticketOptions, 519674552L);
        assertEquals(catchersServer.killCatcherById(id), 0);
        assertEquals(catchersServer.activeCatchers(), "Active catchers:\nCatcher id:1 State: active "+formattedDate+"\t0\t2010001\t->\t2000000\t6\n");
    }

    @Order(5)
    @Test
    void activeCatchers() {
        catchersServer.newCatcher(ticketOptions, 519674552L);
        assertEquals(catchersServer.activeCatchers(), "Active catchers:\nCatcher id:1 State: active " + formattedDate + "\t0	2010001	->	2000000	6\nCatcher id:3 State: active "+formattedDate+"\t0\t2010001\t->\t2000000\t6\n");
    }


    @Order(6)
    @Test
    void killAllCatchers() {
        catchersServer.killAllCatchers();
        assertEquals(catchersServer.activeCatchers(), "Active catchers:\n");
    }

    @Order(7)
    @Test
    void stop() {
        catchersServer.stop();
        assertTrue(catchersServer.catchers.isEmpty());
    }
}