import com.scalapenos.sbt.prompt.SbtPrompt.autoImport._
import com.scalapenos.sbt.prompt._
import com.typesafe.sbt.packager.docker._
import Dependencies._

// Reload Sbt on changes to sbt or dependencies
Global / onChangedBuildSource := ReloadOnSourceChanges

promptTheme := com.scalapenos.sbt.prompt.PromptThemes.ScalapenosTheme

ThisBuild / scalaVersion := "2.13.6"
ThisBuild / startYear := Some(2021)
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "io.ssc"
ThisBuild / organizationName := "SecurityScorecard"
// Copy ./src/test/resources/application.conf to ./local/application.conf
// Then make any necessary changes if necessary and run locally using `sbt run`
ThisBuild / Compile / run / javaOptions += "-Dconfig.file=local/application.conf"
ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.5.0"
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
ThisBuild / resolvers += Resolver.bintrayRepo("colisweb", "maven")

// docker image build commands
// lazy val nixDockerSettings = List(
//   name := "data-export-api",
//   dockerCommands := Seq(
//     Cmd("FROM", "base-jre:latest"),
//     Cmd("COPY", "1/opt/docker/lib/*.jar", "/lib/"),
//     Cmd("COPY", "2/opt/docker/lib/*.jar", "/app.jar"),
//     Cmd("EXPOSE", "8080"),
//     ExecCmd("ENTRYPOINT", "java", "-cp", "/app.jar:/lib/*", "io.ssc.export.api.Main")
//   )
// )

lazy val baseSettings: Seq[Setting[_]] = Seq(
  scalacOptions ++= Seq(
    "-Wconf:cat=unused-imports:info",
    "-Wconf:cat=unused-locals:info",
    "-Wconf:cat=unused-params:info",
    "-Ywarn-macros:after",
    "-Ymacro-annotations"
  ),
  // see `cover` or `coverAll` command aliases for coverageMinimumStmtTotal values
  coverageFailOnMinimum := true,
//   coverageExcludedPackages := "<empty>;io.ssc.proto.*;io.ssc.fluxus.load;"
)

lazy val testSettings: Seq[Def.Setting[_]] = Seq(
  Test / parallelExecution := false,
  publish / skip := true,
  fork := true,
  testFrameworks += new TestFramework("weaver.framework.CatsEffect")
)

lazy val noPublish = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false,
  publish / skip := true
)

// sbt assembly settings
lazy val serverAssemblySettings = Seq(
  assembly / test := {},
  assembly / assemblyOption := (assembly / assemblyOption).value,
  assembly / assemblyMergeStrategy := {
    case PathList(ps @ _*) if ps.last.endsWith(".properties") => MergeStrategy.first
    case "module-info.class"                                  => MergeStrategy.first
    case PathList("META-INF", "maven", "org.webjars", "swagger-ui", "pom.properties") =>
      MergeStrategy.singleOrError
    case x => (assembly / assemblyMergeStrategy).value.apply(x)
  }
)

def subproject(project: Project, dependencies: Seq[ModuleID]) =
  project
    .enablePlugins()
    .settings(libraryDependencies ++= dependencies)
    .settings(subprojectSettings: _*)
    .settings(undeclaredCompileDependenciesFilter -= moduleFilter("org.scala-lang"))
    .settings(undeclaredCompileDependenciesFilter -= moduleFilter("org.scala-lang.modules"))
    .settings(addCompilerPlugin(("org.typelevel" %% "kind-projector" % Versions.KindProjectorVersion).cross(CrossVersion.full)))


lazy val subprojectSettings: Seq[Setting[_]] =
  baseSettings ++
    serverAssemblySettings ++
    testSettings

lazy val sparkApp = subproject(project in file("spark-app"), serverDeps)

lazy val api = subproject(project in file("api"), apiDeps)
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(BuildInfoPlugin)
  // .enablePlugins(DockerPlugin)
  .settings(libraryDependencies ++= testDeps)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "io.ssc.export"
  )
  .dependsOn(sparkApp)
  // .settings(nixDockerSettings: _*)

lazy val pollingApp = subproject(project in file("polling-app"), serverDeps)
  .dependsOn(api, sparkApp)

lazy val root = (project in file("."))
  .aggregate(api, sparkApp, pollingApp)
  .enablePlugins()
  .settings(
    name := "data-export-api",
    baseSettings,
    serverAssemblySettings
  )

val testCoverageMinimum = "95.0"

//addCompilerPlugin(("org.typelevel" %% "kind-projector" % "0.13.0").cross(CrossVersion.full))

//addCommandAlias("compileAll", ";test:compile ;it:compile")
//addCommandAlias(
//  "coverAll",
//  s";undeclaredCompileDependenciesTest ;clean ;coverageOn " +
//    s";set coverageMinimumStmtTotal := $testCoverageMinimum ;test ;coverageAggregate " +
//    s";set coverageMinimumStmtTotal := $integrationTestCoverageMinimum ;it:test ;coverageAggregate " +
//    s";coverageOff"
//)
//addCommandAlias("testAll", ";test ;it:test")
//
//addCommandAlias(
//  "cover",
//  s";undeclaredCompileDependenciesTest ;clean ;coverageOn ;set coverageMinimumStmtTotal := $testCoverageMinimum ;test ;coverageAggregate ;coverageOff"
//)
//addCommandAlias("format", ";scalafmtAll ;scalafmtSbt ;scalafixAll")
//addCommandAlias(
//  "formatCheck",
//  ";scalafmtCheck ;scalafmtSbtCheck ;scalafix --check ;test:scalafix --check ;it:scalafix --check"
//)
//
//// CI build
//addCommandAlias("build", ";clean ;test ;assembly")