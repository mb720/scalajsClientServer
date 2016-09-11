package shared

/**
  * This defines which methods the client can call at the server.
  * <p>
  * Created by Matthias Braun on 9/4/2016.
  */
trait ExampleApi {

  def list(path: String): Seq[FileData]

}
