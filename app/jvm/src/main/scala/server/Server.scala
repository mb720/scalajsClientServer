package server

import java.io.File

import akka.actor.ActorSystem
import shared.{ExampleApi, FileData}
import spray.http.{HttpEntity, MediaTypes}
import spray.routing.SimpleRoutingApp
import upickle.default.{Reader, Writer}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, _}
import scala.concurrent.{Await, Future}
import scala.util.Properties

/**
  * A simple server running on localhost. It exposes an API for the client to list files residing on the server.
  */
object Server extends SimpleRoutingApp with ExampleApi {

  object Router extends autowire.Server[String, Reader, Writer] {

    // There must be a Reader[String] implicitly available
    def read[Result: Reader](string: String) = upickle.default.read[Result](string)

    // There must be a Writer[Result] implicitly available
    def write[Result: Writer](result: Result) = upickle.default.write(result)

    val routes = Router.route[ExampleApi](Server)
  }

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
          path("ajax" / Segments) { segments =>
            extract(_.request.entity.asString) { requestStr =>
              complete {
                handleRequest(segments, requestStr)
              }
            }
          }
        }
    }
  }

  /**
    * Handles a request sent from the client.
    *
    * @param segments
    *                contains the package, the class and the name of the API method to call
    * @param request the request from the client
    * @return an answer to the request
    */
  def handleRequest(segments: List[String], request: String) = {

    // The future answer to the request
    val futureAnswer: Future[String] =
    // We get the API method from the segments and call it with the request as the parameter
    Router.route[ExampleApi](Server)(autowire.Core.Request(segments, upickle.default.read[Map[String, String]](request)))

    Await.result(futureAnswer, Duration(5, SECONDS))
  }

  def list(searchPath: String): Seq[FileData] = {
    val (dir, fileName) = searchPath.splitAt(searchPath.lastIndexOf("/") + 1)

    // Lists files in a directory
    def listFiles(directory: String): Seq[File] = {
      val files = new java.io.File(directory).listFiles()
      if (files == null)
        Seq.empty
      else
        files
    }

    val filesOnServer = listFiles("./" + dir)

    // Turn each File into FileData
    for {
      fileOnServer <- filesOnServer
      if fileOnServer.getName.startsWith(fileName)
    } yield FileData(fileOnServer.getName, fileOnServer.length())
  }
}
