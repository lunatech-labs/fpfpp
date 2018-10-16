import sbtcrossproject.CrossPlugin.autoImport.{ CrossType, crossProject }

lazy val commonSettings = Seq(
  scalaVersion := "2.12.7"
)

lazy val server = (project in file("server"))
  .enablePlugins(SbtWeb)
  .settings(commonSettings)
  .settings(
    scalaJSProjects := Seq(client),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    // triggers scalaJSPipeline when using compile or continuous compilation
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % "10.1.1",
      "com.typesafe.akka" %% "akka-actor" % "2.5.17",
      "com.typesafe.akka" %% "akka-stream" % "2.5.17",
      //to link scalajs output to HTML templating engines
      "com.vmunier" %% "scalajs-scripts" % "1.1.2",
      //provides JSON (un)marshalling support
      "de.heikoseeberger" %% "akka-http-circe" % "1.21.0",
      "org.scalaj" %% "scalaj-http" % "2.4.1",
      "com.typesafe.play" %% "play-json" % "2.6.10",
    ),
    WebKeys.packagePrefix in Assets := "public/",
    managedClasspath in Runtime += (packageBin in Assets).value,
  )
  .dependsOn(sharedJvm)


lazy val client = (project in file("client"))
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .settings(commonSettings)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    dependencyOverrides ++= Seq(
      "org.webjars.npm" % "js-tokens" % "3.0.2",
    ),
    libraryDependencies ++= Seq(
      //provides a statically typed interface to the DOM such that it can be called from Scala
      "org.scala-js" %%% "scalajs-dom" % "0.9.6",
      // react integration with scalajs
      "com.github.japgolly.scalajs-react" %%% "core" % "1.3.1",
      "com.github.japgolly.scalajs-react" %%% "extra" % "1.3.1",
      // type-safe CSS
      "com.github.japgolly.scalacss" %%% "core" % "0.5.5",
      // react integration
      "com.github.japgolly.scalacss" %%% "ext-react" % "0.5.5",
    ),
    // Js libraries
    jsDependencies ++= Seq(
      "org.webjars.npm" % "react" % "16.5.1"
        /        "umd/react.development.js"
        minified "umd/react.production.min.js"
        commonJSName "React",

      "org.webjars.npm" % "react-dom" % "16.5.1"
        /         "umd/react-dom.development.js"
        minified  "umd/react-dom.production.min.js"
        dependsOn "umd/react.development.js"
        commonJSName "ReactDOM",

      "org.webjars.npm" % "react-dom" % "16.5.1"
        /         "umd/react-dom-server.browser.development.js"
        minified  "umd/react-dom-server.browser.production.min.js"
        dependsOn "umd/react-dom.development.js"
        commonJSName "ReactDOMServer"
    )
  )
  .dependsOn(sharedJs)


lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  // cross plateform, use in Js and Jvs
  .crossType(CrossType.Pure)
  .in(file("shared"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      // json parsing
      "io.circe" %%% "circe-core"    % "0.9.3",
      "io.circe" %%% "circe-generic" % "0.9.3",
      "io.circe" %%% "circe-parser"  % "0.9.3",
    )
  )

onLoad in Global := (onLoad in Global).value andThen { s: State =>
  "project server" :: s
}
