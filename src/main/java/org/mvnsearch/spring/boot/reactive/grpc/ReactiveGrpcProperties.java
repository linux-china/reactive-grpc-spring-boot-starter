package org.mvnsearch.spring.boot.reactive.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * reactive grpc properties
 *
 * @author linux_china
 */
@ConfigurationProperties(
        prefix = "grpc.reactive"
)
public class ReactiveGrpcProperties {
    private Integer port = 50051;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
