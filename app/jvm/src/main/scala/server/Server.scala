package server

import akka.actor.ActorSystem
import shared.FileData
import spray.http.{HttpEntity, MediaTypes}
import spray.routing.SimpleRoutingApp

import scala.util.Properties

/**
  * A simple server running on localhost. It exposes an API for the client to list files residing on the server.
  */
object Server extends SimpleRoutingApp {

  /**
    * Starts the server.
    */
  def up(): Unit = {
    implicit val system = ActorSystem()
    val port = Properties.envOrElse("PORT", "8080").toInt
    val localhost = "0.0.0.0"
    startServer(localhost, port = port) {
      get {
        pathSingleSlash {
          complete {
            HttpEntity(
              MediaTypes.`text/html`,
              Page.skeleton.render
            )
          }
        } ~
          getFromResourceDirectory("")
      } ~
        post {
          path("ajax" / "list") {
            extract(_.request.entity.asString) { searchString =>
              complete {
                val searchResult = list(searchString)
                serialize(searchResult)
              }
            }
          }
        }
    }
  }

  def serialize(fileData: Seq[FileData]): String = {
    upickle.default.write(fileData)
  }

  def list(searchPath: String) = {
    val (dir, fileName) = searchPath.splitAt(searchPath.lastIndexOf("/") + 1)

    val filesOnServer = new java.io.File("./" + dir).listFiles()

    for {
      fileOnServer <- filesOnServer
      if fileOnServer.getName.startsWith(fileName)
    } yield FileData(fileOnServer.getName, fileOnServer.length())
  }
}
