package org.luvx.boot.starter.etcd.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.luvx.boot.starter.etcd.core.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = Constants.ETCD)
public class EtcdProperties {
    private boolean      enabled;
    private List<String> endpoints;
    private String       username;
    private String       password;
}
