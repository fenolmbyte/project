package ru.tinkoff.edu.java.parser.handler;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.tinkoff.edu.java.parser.data.LinkData;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LinkHandlerChainTest {
    private final LinkHandlerChain linkHandlerChain;

    LinkHandlerChainTest() {
        List<LinkHandler> handlers = Arrays.asList(
                new GitHubLinkHandler(),
                new StackOverflowLinkHandler()
        );
        linkHandlerChain = new LinkHandlerChain(handlers);
    }

    @ParameterizedTest
    @MethodSource({
            "getParametersWrongFormat",
            "getParametersWrongDomain",
            "getParametersCorrect"
    })
    void handle__incorrectLink_returnNull(String link, boolean correct) {
        // given

        // when
        LinkData data = linkHandlerChain.handle(link);

        // then
        assertEquals(data != null, correct);
    }

    private Stream<Arguments> getParametersCorrect() {
        return Stream.of(
                Arguments.of("https://github.com/fenolmbyte/project", true),
                Arguments.of("https://stackoverflow.com/questions/123123/wasd", true),
                Arguments.of("https://stackoverflow.com/questions/123123/123123", true)
        );
    }

    private Stream<Arguments> getParametersWrongFormat() {
        return Stream.of(
                Arguments.of("", false),
                Arguments.of("1", false),
                Arguments.of("github.com/", false),
                Arguments.of("https://github.com/", false),
                Arguments.of("https://github.com/fenolmbyte", false),
                Arguments.of("https://github.com/fenolmbyte/wasd/wasd", false),
                Arguments.of("https://stackoverflow.random/questions/wasd/wasd", false),
                Arguments.of("https://stackoverflow.random/questions/123123/wasd/wasd", false),
                Arguments.of("https://stackoverflow.random/asdsa/123123/wasd", false)
        );
    }

    private Stream<Arguments> getParametersWrongDomain() {
        return Stream.of(
                Arguments.of("https://github.random/fenolmbyte/project", false),
                Arguments.of("https://ru.wikipedia.org/questions/123123/wasd", false),
                Arguments.of("https://stackoverflow.random/questions/123123/wasd", false)
        );
    }
}