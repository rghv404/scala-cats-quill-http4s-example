resolvers += Classpaths.sbtPluginReleases
resolvers += "Typesafe Repository".at("https://repo.typesafe.com/typesafe/releases/")

addDependencyTreePlugin

addSbtPlugin("ch.epfl.scala"             % "sbt-bloop"                 % "1.4.8")
addSbtPlugin("ch.epfl.scala"             % "sbt-scalafix"              % "0.9.28")
addSbtPlugin("com.eed3si9n"              % "sbt-buildinfo"             % "0.10.0")
addSbtPlugin("com.github.cb372"          % "sbt-explicit-dependencies" % "0.2.16")
addSbtPlugin("com.github.sbt"            % "sbt-pgp"                   % "2.1.2")
addSbtPlugin("com.scalapenos"            % "sbt-prompt"                % "1.0.2")
addSbtPlugin("com.thesamet"              % "sbt-protoc"                % "1.0.4") // required for ScalaPB
addSbtPlugin("com.typesafe.sbt"          % "sbt-git"                   % "1.0.1")
addSbtPlugin("com.typesafe.sbt"          % "sbt-native-packager"       % "1.8.1")
addSbtPlugin("de.gccc.sbt"               % "sbt-jib"                   % "0.9.2")
addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat"              % "0.1.18")
addSbtPlugin("org.scalameta"             % "sbt-scalafmt"              % "2.4.2")
addSbtPlugin("org.scoverage"             % "sbt-scoverage"             % "1.8.1")

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.11.3" // required for ScalaPB