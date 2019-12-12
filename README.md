Messing around with Mu and protobuf.

To try the server:

```
sbt run
```

Using [grpcurl](https://github.com/fullstorydev/grpcurl) as a client:

```
grpcurl -plaintext -import-path src/main/resources -proto hello.proto -d '{ "arg1":"one", "arg2":"two", "arg3":["three"] }' localhost:8080 foo.ProtoGreeter.SayHelloProto
```

