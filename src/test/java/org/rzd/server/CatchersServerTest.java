package org.rzd.server;

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

class CatchersServerTest {
    static CatchersServerImpl catchersServer;
    static String formattedDate;


    @BeforeAll
    static void setUp() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ApplicationConfigTest.class);
        catchersServer = (CatchersServerImpl) applicationContext.getBean("CatchersServerImpl");
        catchersServer.start();
        LocalDateTime nextDate = LocalDateTime.now().plusDays(1L);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        formattedDate = nextDate.format(formatter);
    }

    @AfterAll
    static void tearDown() {
        catchersServer.stop();
    }

    @Test
    void start() {
        assertNotNull(catchersServer.catchers);
    }


    @Test
    void newCatcher() {
        TicketOptions t1 = new TicketOptions("2010001", "2000000", formattedDate, "0", 6L, 100L);
        Long id1 = catchersServer.newCatcher(t1, 519674552L);
        Catcher createdCatcher = null;
        for (Catcher c : catchersServer.catchers) {
            if (Objects.equals(c.getId(), id1)) {
                createdCatcher = c;
            }
        }
        assertNotNull(createdCatcher);
    }

    @Test
    void activeCatchers() {
        TicketOptions t1 = new TicketOptions("2010001", "2000000", formattedDate, "0", 6L, 100L);
        catchersServer.newCatcher(t1, 519674552L);
        assertEquals(catchersServer.activeCatchers(), """
                Active catchers:
                Catcher id :3 State: active 03.12.2024	0	2010001	->	2000000	6
                Catcher id :4 State: active 03.12.2024	0	2010001	->	2000000	6
                """);
    }

    @Test
    void killCatcherById() {
        TicketOptions t1 = new TicketOptions("2010001", "2000000", formattedDate, "0", 6L, 100L);
        Long id = catchersServer.newCatcher(t1, 519674552L);
        assertEquals(catchersServer.killCatcherById(id), 0);
        assertEquals(catchersServer.activeCatchers(), "Active catchers:\n");
    }


    @Test
    void allCatchers() {
        TicketOptions t1 = new TicketOptions("2010001", "2000000", formattedDate, "0", 6L, 100L);
        Long id = catchersServer.newCatcher(t1, 519674552L);
        assertEquals(catchersServer.allCatchers(), """
                Catchers:
                Catcher id:1 State: active 03.12.2024\t0\t2010001\t->\t2000000\t6
                """);
        assertEquals(catchersServer.killCatcherById(id), 0);
        assertEquals(catchersServer.activeCatchers(), "Active catchers:\n");

    }


    @Test
    void killAllCatchers() {
        TicketOptions t1 = new TicketOptions("2010001", "2000000", formattedDate, "0", 6L, 100L);
        catchersServer.newCatcher(t1, 519674552L);
        assertEquals(catchersServer.allCatchers(), """
                Catchers:
                Catcher id:1 State: active 03.12.2024\t0\t2010001\t->\t2000000\t6
                """);
        catchersServer.killAllCatchers();
        assertEquals(catchersServer.activeCatchers(), "Active catchers:\n");
    }

    @Test
    void stop() {
        catchersServer.stop();
        assertTrue(catchersServer.catchers.isEmpty());
    }
}