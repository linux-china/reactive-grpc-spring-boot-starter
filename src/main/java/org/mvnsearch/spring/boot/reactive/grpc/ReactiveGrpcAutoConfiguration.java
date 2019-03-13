package org.mvnsearch.spring.boot.reactive.grpc;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;

/**
 * reactive gRPC service auto configuration for Spring
 *
 * @author linux_china
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
@EnableConfigurationProperties(ReactiveGrpcProperties.class)
public class ReactiveGrpcAutoConfiguration {
    private Logger log = LoggerFactory.getLogger(ReactiveGrpcAutoConfiguration.class);
    @Autowired
    private ReactiveGrpcProperties properties;

    @Bean(destroyMethod = "shutdown")
    public Server reactiveGrpcServer(@Autowired @ReactiveGrpcService List<BindableService> BindableService) throws IOException {
        ServerBuilder<?> builder = ServerBuilder.forPort(properties.getPort());
        log.info("gRPC server started on: " + properties.getPort());
        for (io.grpc.BindableService bindableService : BindableService) {
            builder.addService(bindableService);
            log.info("Reactive gRPC service exposed: " + bindableService.getClass().getName());
        }
        return builder.build().start();
    }

}
