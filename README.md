# Scala.js example with a [Spray](http://spray.io/) server

This is a slightly overhauled version of Li Haoyi's [client-server example](https://github.com/lihaoyi/hands-on-scala-js/tree/master/examples/crossBuilds/clientserver2) described in [Hands-on-Scala.js](http://www.lihaoyi.com/hands-on-scala-js/#IntegratingClient-Server)

## Get started

1. Install [SBT](http://www.scala-sbt.org/)
2. Start SBT by typing `sbt` in this project's root directory
3. Start the server: `sbt> ~re-start` (this will automatically restart the server after editing a file)
4. Point your browser to `localhost:8080`

## Miscellaneous hints

- You can shut down the server by hitting enter in the SBT console and typing `re-stop`. `re` stands for [sbt-revolver](https://github.com/spray/sbt-revolver).
- Don't forget to `reload` in the SBT console after making changes to `build.sbt`
- Instead of automatically restarting the server after changing a file with `~re-start`, you can manually restart the server in the SBT console using `;re-stop;reload;re-start`

