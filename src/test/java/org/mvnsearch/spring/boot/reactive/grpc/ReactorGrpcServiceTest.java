package org.mvnsearch.spring.boot.reactive.grpc;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mvnsearch.spring.boot.reactive.grpc.demo.service.HelloReply;
import org.mvnsearch.spring.boot.reactive.grpc.demo.service.HelloRequest;
import org.mvnsearch.spring.boot.reactive.grpc.demo.service.ReactorGreeterGrpc;
import reactor.core.publisher.Mono;

/**
 * reactor service test
 *
 * @author linux_china
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReactorGrpcServiceTest {
    private ManagedChannel channel;
    private ReactorGreeterGrpc.ReactorGreeterStub greeter;

    @BeforeAll
    public void setUp() {
        channel = ManagedChannelBuilder.forAddress("127.0.0.1", 50051).usePlaintext().build();
        greeter = ReactorGreeterGrpc.newReactorStub(channel);
    }

    @AfterAll
    public void tearDown() {
        channel.shutdownNow();
    }

    /**
     * test for say hello
     */
    @Test
    public void testSayHello() throws Exception {
        Mono<HelloRequest> request = Mono.just(HelloRequest.newBuilder().setName("Jack").build());
        //rpc style
        greeter.sayHello(request)
                .map(HelloReply::getMessage)
                .subscribe(System.out::println);
        //reactive style
        request.as(greeter::sayHello)
                .map(HelloReply::getMessage)
                .subscribe(System.out::println);
        Thread.sleep(1000);
    }
}
