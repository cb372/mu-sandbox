scalaVersion := "2.12.8"
libraryDependencies ++= Seq(
  "io.higherkindness" %% "mu-rpc-fs2" % "0.19.1",
  "io.higherkindness" %% "mu-rpc-netty" % "0.19.1",
  "io.higherkindness" %% "mu-rpc-server" % "0.19.1"
)

import higherkindness.mu.rpc.idlgen.IdlGenPlugin.autoImport._

idlType := "proto"
//srcGenSerializationType := "Protobuf"
idlGenIdiomaticEndpoints := true
sourceGenerators in Compile += (srcGen in Compile).taskValue

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.patch)
