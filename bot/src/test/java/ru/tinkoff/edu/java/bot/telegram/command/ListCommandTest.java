package ru.tinkoff.edu.java.bot.telegram.command;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tinkoff.edu.java.bot.dto.scrapper.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.scrapper.ListLinksResponse;
import ru.tinkoff.edu.java.bot.service.ScrapperWebService;

@ExtendWith(MockitoExtension.class)
class ListCommandTest {
    @Mock
    private ScrapperWebService service;

    @InjectMocks
    private ListCommand listCommand;

    @Test
    void handle__linksListEmpty_returnSpecialMessage() {
        // given
        when(service.getAllLinks(anyLong())).thenReturn(createListLinksResponse(0));

        // when
        SendMessage response = listCommand.handle(createListMessage());

        // then
        assertEquals(response.getText(), "You don't have tracked links yet, use /track <link> to track one");
    }

    @Test
    void handle__linksListNotEmpty_returnExpectedFormat() {
        // given
        int size = 4;
        when(service.getAllLinks(anyLong())).thenReturn(createListLinksResponse(size));

        // when
        SendMessage response = listCommand.handle(createListMessage());

        // then
        assertTrue(response.getText().startsWith("List of your current tracked links:"));
        assertEquals(response.getText().split("\r\n|\r|\n").length, size + 1);
    }

    private ListLinksResponse createListLinksResponse(int size) {
        List<LinkResponse> list = Stream.iterate(0L, i -> i + 1)
            .map(id -> new LinkResponse(id, URI.create("https://some.mock.link/")))
            .limit(size)
            .collect(Collectors.toList());
        return new ListLinksResponse(list, size);
    }

    private Message createListMessage() {
        Message message = new Message();
        message.setChat(new Chat(1L, "private"));
        message.setText("/list");
        return message;
    }
}
