// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += Resolver.mavenLocal

libraryDependencies += "com.typesafe.play" % "fork-run_2.10" % "2.4.4"
libraryDependencies += "closeness" % "closeness-core" % "1.0"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.4")
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "4.0.0")
