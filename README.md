Spring Boot Starter Reactive gRPC
=================================
Spring Boot 2.0 starter with Reactive gRPC (https://github.com/salesforce/reactive-grpc)

### How to use?

* Add dependency in your pom.xml
```xml
 <dependency>
        <groupId>org.mvnsearch.spring.boot</groupId>
        <artifactId>reactive-grpc-spring-boot-starter</artifactId>
        <version>1.0.0-SNAPSHOT</version>
 </dependency>
```

* add protobuf-maven-plugin with Reactor-gRPC plugin support

```xml
<build>
        <extensions>
            <extension>
                <groupId>kr.motd.maven</groupId>
                <artifactId>os-maven-plugin</artifactId>
                <version>1.6.1</version>
            </extension>
        </extensions>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.0</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                    <parameters>true</parameters>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.xolstice.maven.plugins</groupId>
                <artifactId>protobuf-maven-plugin</artifactId>
                <version>0.6.1</version>
                <configuration>
                    <protocArtifact>com.google.protobuf:protoc:${protobuf-java.version}:exe:${os.detected.classifier}
                    </protocArtifact>
                    <pluginId>grpc-java</pluginId>
                    <pluginArtifact>io.grpc:protoc-gen-grpc-java:${grpc.version}:exe:${os.detected.classifier}
                    </pluginArtifact>
                    <protocPlugins>
                        <protocPlugin>
                            <id>reactor-grpc</id>
                            <groupId>com.salesforce.servicelibs</groupId>
                            <artifactId>reactor-grpc</artifactId>
                            <version>${reactive-grpc.version}</version>
                            <mainClass>com.salesforce.reactorgrpc.ReactorGrpcGenerator</mainClass>
                        </protocPlugin>
                    </protocPlugins>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>test-compile</goal>
                            <goal>test-compile-custom</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
</build>
```

* Implement Reactive Service with @ReactiveGrpcService support

```java
@Service
@ReactiveGrpcService
public class ReactiveGreeterImpl extends ReactorGreeterGrpc.GreeterImplBase {
    @Override
    public Mono<HelloReply> sayHello(Mono<HelloRequest> request) {
        return request.map(helloRequest -> HelloReply.newBuilder().setMessage("Hello " + helloRequest.getName()).build());
    }

    @Override
    public Mono<HelloReply> sayHelloAgain(Mono<HelloRequest> request) {
        return request.map(helloRequest -> HelloReply.newBuilder().setMessage("Hello Again " + helloRequest.getName()).build());
    }
}
```

* Start you Spring application and use evans to test service

```
$ evans src/test/proto/greeter.proto
```

### Reactive gRPC configuration
You don't need to setup anything for Reactive gRPC service.
If you want to change listen port(default is 50051), please change as following:

```properties
grpc.reactive.port=50051
```

### FAQ

#### What's difference with gRPC Spring Boot Starter

gRPC has starters to integration with Spring Boot, for example:

* https://github.com/yidongnan/grpc-spring-boot-starter
* https://github.com/LogNet/grpc-spring-boot-starter

The main difference is code style. If you like Reactive style, please choose Reactive gRPC. If you think native style is fine, please use starters above.

* gRPC native style:
```
public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
  HelloReply reply = HelloReply.newBuilder().setMessage("Hello ==> " + req.getName()).build();
  responseObserver.onNext(reply);
  responseObserver.onCompleted();
}
```

* gRPC reactive style:
```
public Mono<HelloReply> sayHello(Mono<HelloRequest> request) {
  return request.map(helloRequest -> HelloReply.newBuilder().setMessage("Hello " + helloRequest.getName()).build());
}
```

### Todo

* Actuator endpoint to output proto sources

### References

* Reactive gRPC: https://github.com/salesforce/reactive-grpc
* Project Reactor: https://projectreactor.io/
* Evans: expressive universal gRPC client https://github.com/ktr0731/evans

