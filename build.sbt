name := "Client-server example"

scalaVersion in ThisBuild := "2.11.8"

// Set the version for all projects in this build
version in ThisBuild := "0.0.1"

lazy val root = project.in(file(".")).
  aggregate(client, server).
  settings()

// We can run this project by calling appJS/run and appJVM/run. Type `projects` in sbt to see the name of all projects
lazy val app = crossProject.in(file(".")).
  settings(
    name := "myApp",
    libraryDependencies ++= Seq(
      // The triple % gets the library in two versions: One for running on the JVM and one for running on a JavaScript engine like V8
      "com.lihaoyi" %%% "scalatags" % "0.6.0",
      "com.lihaoyi" %%% "upickle" % "0.4.1"
    )).
  jvmSettings(
    libraryDependencies ++= Seq(
      // Skip Spray 1.3.3 since it causes high CPU load after the first request from the client
      "io.spray" %% "spray-can" % "1.3.2",
      "io.spray" %% "spray-routing" % "1.3.2",
      "com.typesafe.akka" %% "akka-actor" % "2.3.6"
    )
  ).
  jsSettings(

    // Use the faster Node.js instead of Rhino. Get Node.js from here: https://nodejs.org
    //scalaJSUseRhino in Global := false
  )

// We need to define the subprojects. Note that the names of these vals do not affect how you run the subprojects:
// It will be `<nameOfCrossProject>JS/run` and `<nameOfCrossProject>JVM/run`, irrespective of how these vals are named
lazy val client = app.js
lazy val server = app.jvm.settings(
  // Add the compiled JavaScript to the server's resources since the server sends the JavaScript to the client
  (resources in Compile) += (fastOptJS in (client, Compile)).value.data
)

