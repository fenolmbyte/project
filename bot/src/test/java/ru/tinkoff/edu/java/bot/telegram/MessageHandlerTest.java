package ru.tinkoff.edu.java.bot.telegram;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tinkoff.edu.java.bot.service.ScrapperWebService;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@TestPropertySource(locations = "classpath:application.yml")
class MessageHandlerTest {
    @MockBean
    private ScrapperWebService service;

    @Autowired
    private MessageHandler messageHandler;

    @Test
    void handle__unknownCommand_returnSpecialMessage() {
        // given

        // when
        SendMessage response = messageHandler.handle(createMessage("SomeCommandThadDoesNotExist"));

        // then
        assertEquals(response.getText(), "Unknown command, use /help to get list of commands");
    }

    private Message createMessage(String text) {
        Message message = new Message();
        message.setChat(new Chat(1L, "private"));
        message.setText(text);
        return message;
    }
}
