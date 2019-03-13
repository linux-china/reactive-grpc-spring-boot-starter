Spring Boot Starter Reactive gRPC
=================================
Spring Boot 2.0 starter for Reactive gRPC (https://github.com/salesforce/reactive-grpc) to bring reactive style for grpc-java.

### How to use?

* Please clone the repository and install reactive-grpc-spring-boot-starter artifact to your local Maven repository. 

```
$ git clone git@github.com:linux-china/reactive-grpc-spring-boot-starter.git
$ cd reactive-grpc-spring-boot-starter
$ mvn -P release -DskipTests clean install
```

* Add dependency and repository in your pom.xml. Now reactive-grpc-spring-boot-starter hosted by jitpack
```xml
<project>
 ...
 <dependencies>
     <dependency>
        <groupId>com.github.linux-china</groupId>
        <artifactId>reactive-grpc-spring-boot-starter</artifactId>
        <version>1.0.0-SNAPSHOT</version>
     </dependency>
 </dependencies>
 
</project>
```

* Add protobuf-maven-plugin with Reactor-gRPC protocPlugin included. **Attention:** proto files should be in "src/main/proto" directory

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
                            <goal>compile</goal>
                            <goal>compile-custom</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
</build>
```

* Implement Reactive Service with @ReactiveGrpcService annotation.

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

* Start you Spring application and you will get following output on your console.

```
09:40:17.366 [INFO ] o.m.s.b.r.g.ReactiveGrpcAutoConfiguration - gRPC server started on: 50051
09:40:17.379 [INFO ] o.m.s.b.r.g.ReactiveGrpcAutoConfiguration - Reactive gRPC service exposed: org.mvnsearch.spring.boot.reactive.grpc.demo.ReactiveGreeterImpl

```

* Use Evans to test gRPC service

```
$ evans src/main/proto/greeter.proto
```

### Reactive gRPC configuration
You don't need to setup anything for Reactive gRPC service.
If you want to change listen port(default is 50051), please change as following in application.properties:

```properties
grpc.reactive.port=50051
```

### FAQ

#### What's difference with gRPC Spring Boot Starter

gRPC has different starters to integration with Spring Boot, for example:

* https://github.com/yidongnan/grpc-spring-boot-starter
* https://github.com/LogNet/grpc-spring-boot-starter

The main difference is code style in these starters. If you like Reactive style, please choose Reactive gRPC.
If you think native style fine, please use starters above.

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

#### Convert between DDD model(entity,value object) and Protobuf message

Please refer MapStruct to map DDD model and Protobuf message, and MapStruct 1.3 with Protobuf builder support.

#### Quick test

Please execute "mvn -DskipTests clean package" and run ReactiveGrpcDemoApplication

### Todo

* Publish artifact to Maven repository
* Actuator endpoint to output proto sources

### References

* Reactive gRPC: https://github.com/salesforce/reactive-grpc
* Project Reactor: https://projectreactor.io/
* Evans: expressive universal gRPC client https://github.com/ktr0731/evans
* MapStruct: http://mapstruct.org/
