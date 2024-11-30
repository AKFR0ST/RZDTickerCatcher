package org.rzd.bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rzd.config.ApplicationConfigTest;
import org.rzd.model.ApplicationOptions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.Assert;

class MessageSenderTest {

    ApplicationContext context;
    ApplicationOptions options;
    MessageSender messageSender;


    @BeforeEach
    void init() {
        context = new AnnotationConfigApplicationContext(ApplicationConfigTest.class);
        options = context.getBean(ApplicationOptions.class);
        messageSender = context.getBean(MessageSender.class);
    }

    @Test
    void sendMessage() {
        Assert.assertEquals(messageSender.sendMessage(519674552L,"TEST_MESSAGE").toString(), "200 OK");
    }
}