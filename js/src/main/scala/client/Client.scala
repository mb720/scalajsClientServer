package client

import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.{XMLHttpRequest, document}
import shared.FileData
import upickle.default._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scalatags.JsDom.all._

/**
  * This code is transpiled to JavaScript and runs in the browser.
  * It lets the user list files that are located on the server.
  */
object Client extends js.JSApp {

  def getCurrentDateAndTime = new js.Date().toUTCString()

  def main(): Unit = {
    println("The client says hi")
    val body = document.body

    body.appendChild(
      p(s"Compiled on: $getCurrentDateAndTime").render
    )

    val inputBox = input.render
    val outputBox = ul.render

    def update() = Ajax.post("/ajax/list", inputBox.value).foreach { xmlHttpRequest =>
      val fileData = deserializeFileData(xmlHttpRequest)
//      outputBox.innerHTML = ""
      for (FileData(name, size) <- fileData) {
        outputBox.appendChild(
          li(
            b(name), " - ", size, " bytes"
          ).render
        )
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

    def deserializeFileData(xhr: XMLHttpRequest): Seq[FileData] = {
      upickle.default.read[Seq[FileData]](xhr.responseText)
    }
  }
}
