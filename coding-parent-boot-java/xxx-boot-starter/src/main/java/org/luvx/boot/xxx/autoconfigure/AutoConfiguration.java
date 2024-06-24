package org.luvx.boot.xxx.autoconfigure;

import org.luvx.boot.xxx.a.Meow;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(XxxProperties.class)
@ConditionalOnClass(Meow.class)
@ConditionalOnProperty(prefix = "meow", name = "enabled", matchIfMissing = true)
public class AutoConfiguration {

    private final XxxProperties xxxProperties;

    public AutoConfiguration(XxxProperties xxxProperties) {
        this.xxxProperties = xxxProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public Meow myFeature() {
        return new Meow(xxxProperties);
    }
}
