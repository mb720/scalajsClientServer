package server

import scalatags.Text.all._
import scalatags.Text.tags2.title

object Page {

  // The application name as defined in build.sbt in lowercase
  private val appName = "clientserverapp"
  // Calls the main method of our app. https://www.scala-js.org/tutorial/basic/#automatically-creating-a-launcher
  private val launcher = "/" + appName + "-launcher.js"
  // The client code we've written in Scala, translated to JavaScript
  private val app = "/" + appName + "-fastopt.js"

  val skeleton =
    html(
      head(
        title("Scala.js client server example"),
        meta(
          charset := "utf-8"
        ),

        script(src := app),

        // Alternatively, we can use WebJars: https://www.scala-js.org/tutorial/basic/#adding-javascript-libraries
        link(
          rel := "stylesheet",
          href := "https://cdn.jsdelivr.net/pure/0.6.0/pure-min.css"
        )
      ),
      body(
        script(src := launcher),
        div(id := "contents")
      )
    )
}
