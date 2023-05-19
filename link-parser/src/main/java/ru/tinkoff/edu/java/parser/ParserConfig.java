package ru.tinkoff.edu.java.parser;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.parser.handler.GitHubLinkHandler;
import ru.tinkoff.edu.java.parser.handler.LinkHandler;
import ru.tinkoff.edu.java.parser.handler.LinkHandlerChain;
import ru.tinkoff.edu.java.parser.handler.StackOverflowLinkHandler;

@Configuration
public class ParserConfig {
    @Bean
    public StackOverflowLinkHandler stackOverflowLinkHandler() {
        return new StackOverflowLinkHandler();
    }

    @Bean
    public GitHubLinkHandler gitHubLinkHandler() {
        return new GitHubLinkHandler();
    }

    public @Bean LinkHandlerChain linkHandlerChain(List<LinkHandler> handlers) {
        return new LinkHandlerChain(handlers);
    }
}
