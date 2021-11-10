package ru.gpb.urisad.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties("mail-parser")
public class MailParserProperties {

    private String targetDir;
    private String filteredStrings;
    private String targetStrings;
    private String splitChar;
}
