import java.util.Locale.ROOT

scalaVersion in ThisBuild := "2.11.8"

// Set the version for all projects in this build
version in ThisBuild := "0.0.2"

lazy val root = project.in(file(".")).
  aggregate(client, server).
  settings(
    name := "Client-server root"
  )

// Page.scala uses this name. If you change it here, do `sbt clean` and change it in Page.scala as well
val appName = "clientServerApp"

// We can run this project by calling appJS/run and appJVM/run. Type `projects` in sbt to see the name of all projects
lazy val app = crossProject.in(file("./app")).
  settings(
    name := appName,
    libraryDependencies ++= Seq(
      // The triple % gets the library in two versions: One for running on the JVM and one for running on a JavaScript engine like V8
      "com.lihaoyi" %%% "scalatags" % "0.6.0",
      "com.lihaoyi" %%% "upickle" % "0.4.1",
      "com.lihaoyi" %%% "autowire" % "0.2.5"
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

    persistLauncher in Compile := true,
    persistLauncher in Test := false
  )


// We need to define the subprojects. Note that the names of these vals do not affect how you run the subprojects:
// It will be `<nameOfCrossProject>JS/run` and `<nameOfCrossProject>JVM/run`, irrespective of how these vals are named
lazy val client = app.js

/*
  Setting 'persistLauncher' to true generates a small JavaScript launcher that calls the main method in the client.
  See also: https://www.scala-js.org/tutorial/basic/#automatically-creating-a-launcher
*/
val launcher = "/" + appName.toLowerCase(ROOT) + "-launcher.js"
lazy val server = app.jvm.settings(
  // Add the compiled JavaScript to the server's resources since the server sends the JavaScript to the client
  (resources in Compile) += (fastOptJS in(client, Compile)).value.data,
  // The server sends the launcher script to the client
  (resources in Compile) += new File((crossTarget in client).value + launcher)
)

