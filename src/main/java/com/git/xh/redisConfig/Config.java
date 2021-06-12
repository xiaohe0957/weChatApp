package com.git.xh.redisConfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author heqi
 */


@ConfigurationProperties(
        prefix = "spring.ids"
)
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Config {
    private String appId;
    private String secret;
    private String name;
    private String password;
}
