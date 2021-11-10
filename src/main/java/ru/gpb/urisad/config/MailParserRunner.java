package ru.gpb.urisad.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.gpb.urisad.service.MailDownloader;

@Component
public class MailParserRunner implements ApplicationRunner {

    @Autowired
    private MailDownloader loader;

    @Autowired
    private MailParserProperties properties;

    @Override
    public void run(ApplicationArguments args) {
        loader.extract(properties);
    }
}
