package com.zugazagoitia.knag.vault;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "knag.key")
@Data
public class KeyConfig {
    private String pub;
}
