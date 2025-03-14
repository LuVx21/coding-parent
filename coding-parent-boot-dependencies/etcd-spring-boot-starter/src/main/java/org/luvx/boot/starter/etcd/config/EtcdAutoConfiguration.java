package org.luvx.boot.starter.etcd.config;

import io.etcd.jetcd.Client;
import org.luvx.boot.starter.etcd.annotation.EnableEtcdConfig;
import org.luvx.boot.starter.etcd.core.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import jakarta.annotation.Resource;

@EnableEtcdConfig
@AutoConfiguration
@EnableConfigurationProperties(EtcdProperties.class)
@ConditionalOnProperty(name = Constants.CONFIG_PROPERTIES_ENABLED, havingValue = "true")
public class EtcdAutoConfiguration {
    @Resource
    private EtcdProperties properties;

    @Bean
    public Client etcdClient() {
        return Client.builder()
                .endpoints(properties.getEndpoints().toArray(new String[0]))
                .build();
    }
}
