name := "closeness-web"
version := "1.0.0-SNAPSHOT"

//For the Guice DI works with routes
//routesGenerator := InjectedRoutesGenerator

// Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
EclipseKeys.preTasks := Seq(compile in Compile)

lazy val root = project.in(file(".")).enablePlugins(PlayScala)

fork in run := true

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }
libraryDependencies += "closeness" % "closeness-core" % "1.0"
libraryDependencies ++= Seq(cache)