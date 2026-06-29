package io.backend.blogproject.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "custom")
@RequiredArgsConstructor
public class JwtProperties {
    private String appkey;

    private Validations validations;

    @Getter
    @Setter
    public static class Validations{
        private Long access;
        private Long refresh;
    }
}
