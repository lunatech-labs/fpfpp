addSbtPlugin("com.vmunier" % "sbt-web-scalajs" % "1.0.8-0.6")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.25")

// allow to mix jvm and js project compilation
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "0.6.0")

// allow to trigger restart automatically when we have a change in modules
addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")

addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.12.0")
