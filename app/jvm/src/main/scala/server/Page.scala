package server

import scalatags.Text.all._

object Page{
  val boot =
    "client.Client().main()"
  val skeleton =
    html(
      head(
        // The application name as defined in build.sbt in lowercase
        script(src:="/clientserverapp-fastopt.js"),
        link(
          rel:="stylesheet",
          href:="https://cdnjs.cloudflare.com/ajax/libs/pure/0.5.0/pure-min.css"
        )
      ),
      body(
        onload:=boot,
        div(id:="contents")
      )
    )
}
