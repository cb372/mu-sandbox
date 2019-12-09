package moo

import foo.hello._

import higherkindness.mu.rpc.server._
import cats.syntax.functor._
import cats.effect._
import fs2._
import scala.concurrent.duration._

class HelloService(implicit timer: Timer[IO]) extends ProtoGreeter[IO] {

  def SayHelloProto(req: HelloRequest): IO[HelloResponse] =
    IO(HelloResponse(s"Hello ${req.arg1}", s"Hello ${req.arg2}", req.arg3.map(x => s"Hello $x")))

  def LotsOfRepliesProto(req: HelloRequest): Stream[IO, HelloResponse] =
    Stream.awakeEvery[IO](1.second).map(duration => HelloResponse("Hello again!", duration.toString, req.arg3))

  def LotsOfGreetingsProto(req: Stream[IO, HelloRequest]): IO[HelloResponse] =
    req.compile.toList.map(reqs => HelloResponse(s"Received ${reqs.size} requests", "hello", Nil))

  def BidiHelloProto(req: Stream[IO, HelloRequest]): Stream[IO, HelloResponse] =
    Stream.awakeEvery[IO](1.second).map(duration => HelloResponse("Hello again!", duration.toString, Nil))

}


object ServerMain extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {

    implicit val service: ProtoGreeter[IO] = new HelloService

    val run = for {
      grpcConfig <- ProtoGreeter.bindService[IO]
      server     <- GrpcServer.default[IO](8080, List(AddService(grpcConfig)))
      runServer  <- GrpcServer.server[IO](server)
    } yield runServer

    run.as(ExitCode.Success)
  }

}
