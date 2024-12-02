package org.rzd.bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rzd.config.ApplicationConfigTest;
import org.rzd.model.ApplicationOptions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MessageReceiverTest {

    ApplicationContext applicationContext;
    ApplicationOptions applicationOptions;
    MessageReceiver messageReceiver;

    @BeforeEach
    void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(ApplicationConfigTest.class);
        applicationOptions = applicationContext.getBean(ApplicationOptions.class);
        messageReceiver = spy(applicationContext.getBean(MessageReceiver.class));
        when(messageReceiver.messageReceive()).thenReturn("""
                {
                    "ok": true,
                    "result": [
                        {
                            "update_id": 436873311,
                            "message": {
                                "message_id": 955,
                                "from": {
                                    "id": 519674552,
                                    "is_bot": false,
                                    "first_name": "Alexei",
                                    "last_name": "Kovalev",
                                    "username": "fr0sters",
                                    "language_code": "ru"
                                },
                                "chat": {
                                    "id": 519674552,
                                    "first_name": "Alexei",
                                    "last_name": "Kovalev",
                                    "username": "fr0sters",
                                    "type": "private"
                                },
                                "date": 1733161118,
                                "text": "text"
                            }
                        }
                    ]
                }
                """);
    }

    @Test
    void getTextMessage() {
        assertEquals("text", messageReceiver.getTextMessage());
    }
}