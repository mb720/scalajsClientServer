package client

import autowire._
import org.scalajs.dom
import org.scalajs.dom.document
import shared.{ExampleApi, FileData}
import upickle.default._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scalatags.JsDom.all._

import upickle.default.{Reader, Writer}
/**
  * This code is transpiled to JavaScript and runs in the browser.
  * It lets the user list files that are located on the server.
  */
object Client extends js.JSApp {

  /**
    * Using Autowire we can do AJAX calls to the server in a type-safe manner.
    */
  object Ajaxer extends autowire.Client[String, upickle.default.Reader, upickle.default.Writer] {
    override def doCall(req: Request) = {
      dom.ext.Ajax.post(
        url = "/ajax/" + req.path.mkString("/"),
        data = upickle.default.write(req.args)
      ).map(_.responseText)
    }

    // There must be a Reader[String] implicitly available
    def read[Result: Reader](string: String) = upickle.default.read[Result](string)

    // There must be a Writer[Result] implicitly available
    def write[Result: Writer](result: Result) = upickle.default.write(result)
  }

  def getCurrentDateAndTime = new js.Date().toUTCString()

  def main(): Unit = {
    println("The client says hi")
    val body = document.body

    body.appendChild(
      p(s"Page loaded on: $getCurrentDateAndTime").render
    )

    val inputBox = input.render
    val outputBox = ul.render
    def update() = {
      // First clear the old list
      outputBox.innerHTML = ""

      val fileList = Ajaxer[ExampleApi].list(inputBox.value).call()
      fileList.foreach { fileData =>
        for (FileData(name, size) <- fileData) {
          outputBox.appendChild(
            li(
              b(name), " - ", size, " bytes"
            ).render
          )
        }
      }
    }

    inputBox.onkeyup = (e: dom.Event) => update()
    update()
    body.appendChild(
      div(
        h1("File Search"),
        inputBox,
        outputBox
      ).render
    )
  }
}
