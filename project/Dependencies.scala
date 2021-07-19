import sbt._

object Dependencies {

    object Versions {
        val cats         = "2.6.1"
        val catsEffect   = "2.5.1"
        val circe        = "0.13.0"
        val doobie       = "0.13.4"
        val derevo       = "0.12.5"
        val fs2          = "2.5.6"
        val fuuid        = "0.6.0"
        val http4s       = "0.21.23"
        val log4cats     = "1.3.1"
        val mtlCore      = "0.7.1"
        val opentracing  = "3.1.0"
        val pureconfig   = "0.15.0"
        val quillV       = "3.7.1"
        val refined      = "0.9.25"
        val rho          = "0.21.0"
        val slf4j        = "1.7.30"
        val sttp         = "2.2.9"
        val sttpModel    = "1.4.7"

        val scalaCheck       = "1.15.4"
        val scalaMock        = "5.1.0"
        val scalaTest        = "3.2.9"
        val scalaTestCheck   = "3.2.2.0"
        val weaver           = "0.6.3"

        // Runtime
        val logback        = "1.2.3"
        val logbackEncoder = "6.6"

        // Test
        val flyway = "7.9.1"

        //Compiler
        val KindProjectorVersion = "0.13.0"
    }

    def library(group: String, artifact: String, version: String)               = group %% artifact % version
    def libraryNoScalaVersion(group: String, artifact: String, version: String) = group  % artifact % version

    lazy val chrisdavenport = library("io.chrisdavenport", _, _)
    lazy val circe          = library("io.circe", _, _)
    lazy val derevo         = library("tf.tofu", _, _)
    lazy val doobie         = library("org.tpolecat", _, _)
    lazy val fs2            = library("co.fs2", _, _)
    lazy val gatling        = libraryNoScalaVersion("io.gatling", _, _)
    lazy val http4s         = library("org.http4s", _, _)
    lazy val monocle        = library("com.github.julien-truffaut", _, _)
    lazy val opentracing    = library("com.colisweb", _, _)
    lazy val pureconfig     = library("com.github.pureconfig", _, _)
    lazy val quill          = library("io.getquill", _, _)
    lazy val refined        = library("eu.timepit", _, _)
    lazy val sttp           = library("com.softwaremill.sttp.client", _, _)
    lazy val tapir          = library("com.softwaremill.sttp.tapir", _, _)
    lazy val typelevel      = library("org.typelevel", _, _)
    lazy val weaver         = library("com.disneystreaming", _, _)

    lazy val catsCore           = typelevel("cats-core", Versions.cats)
    lazy val catsEffect         = typelevel("cats-effect", Versions.catsEffect)
    lazy val catsMtl            = typelevel("cats-mtl-core", Versions.mtlCore)
    lazy val catsFree           = typelevel("cats-free", Versions.cats)
    lazy val catsKernel         = typelevel("cats-kernel", Versions.cats)
    lazy val circeCore          = circe("circe-core", Versions.circe)
    lazy val circeFs2           = circe("circe-fs2", Versions.circe)
    lazy val circeGeneric       = circe("circe-generic", Versions.circe)
    lazy val circeOptics        = circe("circe-optics", Versions.circe)
    lazy val circeParser        = circe("circe-parser", Versions.circe)
    lazy val circeRefined       = circe("circe-refined", Versions.circe)
    lazy val circeShapes        = circe("circe-shapes", Versions.circe)
    lazy val doobieCore         = doobie("doobie-core", Versions.doobie)
    lazy val doobieFree         = doobie("doobie-free", Versions.doobie)
    lazy val doobieHikari       = doobie("doobie-hikari", Versions.doobie)
    lazy val doobiePostgres     = doobie("doobie-postgres", Versions.doobie)
    lazy val doobieQuill        = doobie("doobie-quill", Versions.doobie)
    lazy val flyway             = "org.flywaydb"     % "flyway-core"   % Versions.flyway
    lazy val fs2Core            = fs2("fs2-core", Versions.fs2)
    lazy val fuuidCirce         = chrisdavenport("fuuid-circe", Versions.fuuid)
    lazy val fuuidCore          = chrisdavenport("fuuid", Versions.fuuid)
    lazy val fuuidDoobie        = chrisdavenport("fuuid-doobie", Versions.fuuid)
    lazy val http4sBlazeServer  = http4s("http4s-blaze-server", Versions.http4s)
    lazy val http4sEmberServer  = http4s("http4s-ember-server", Versions.http4s)
    lazy val http4sCirce        = http4s("http4s-circe", Versions.http4s)
    lazy val http4sCore         = http4s("http4s-core", Versions.http4s)
    lazy val http4sDSL          = http4s("http4s-dsl", Versions.http4s)
    lazy val http4sServer       = http4s("http4s-server", Versions.http4s)
    lazy val log4catsCore       = typelevel("log4cats-core", Versions.log4cats)
    lazy val log4catsSl4j       = typelevel("log4cats-slf4j", Versions.log4cats)
    lazy val magnolia           = library("com.propensive", "magnolia", "0.17.0")
    lazy val opentracingAmqp    = opentracing("scala-opentracing-amqp", Versions.opentracing)
    lazy val opentracingClient  = opentracing("scala-opentracing-http4s-client-blaze", Versions.opentracing)
    lazy val opentracingCore    = opentracing("scala-opentracing-core", Versions.opentracing)
    lazy val opentracingContext = opentracing("scala-opentracing-context", Versions.opentracing)
    lazy val opentracingServer  = opentracing("scala-opentracing-http4s-server-tapir", Versions.opentracing)
    lazy val pureconfigCore     = pureconfig("pureconfig-core", Versions.pureconfig)
    lazy val pureconfigGeneric  = pureconfig("pureconfig-generic", Versions.pureconfig)
    lazy val quillCore          = quill("quill-core", Versions.quillV)
    lazy val quillCorePortable  = quill("quill-core-portable", Versions.quillV)
    lazy val quillJdbc          = quill("quill-jdbc", Versions.quillV)
    lazy val quillSql           = quill("quill-sql", Versions.quillV)
    lazy val quillSqlPortable   = quill("quill-sql-portable", Versions.quillV)
    lazy val refinedCats        = refined("refined-cats", Versions.refined)
    lazy val refinedCore        = refined("refined", Versions.refined)
    lazy val refinedPureConfig  = refined("refined-pureconfig", Versions.refined)
    lazy val rhoCore            = http4s("rho-core", Versions.rho)
    lazy val rhoSwagger         = http4s("rho-swagger", Versions.rho)
    lazy val rhoSwaggerUI       = http4s("rho-swagger-ui", Versions.rho)
    lazy val shapeless          = library("com.chuusai", "shapeless", "2.3.7")
    lazy val slf4jApi           = libraryNoScalaVersion("org.slf4j", "slf4j-api", Versions.slf4j)
    lazy val sttpCats           = sttp("cats", Versions.sttp)
    lazy val sttpCatsBackend    = sttp("async-http-client-backend-cats", Versions.sttp)
    lazy val sttpCirce          = sttp("circe", Versions.sttp)
    lazy val sttpCore           = sttp("core", Versions.sttp)
    lazy val sttpFs2Backend     = sttp("async-http-client-backend-fs2", Versions.sttp)
    lazy val sttpModel          = library("com.softwaremill.sttp.model", "core", Versions.sttpModel)

    // Runtime
    lazy val logback        = "ch.qos.logback"       % "logback-classic"          % Versions.logback
    lazy val logbackEncoder = "net.logstash.logback" % "logstash-logback-encoder" % Versions.logbackEncoder

    // Test
    lazy val derevoScalaCheck  = derevo("derevo-scalacheck", Versions.derevo)
    lazy val doobieTest        = doobie("doobie-scalatest", Versions.doobie)
    lazy val refinedScalaCheck = refined("refined-scalacheck", Versions.refined)
    lazy val scalaCheck        = "org.scalacheck"          %% "scalacheck"        % Versions.scalaCheck
    lazy val scalaMock         = "org.scalamock"           %% "scalamock"         % Versions.scalaMock
    lazy val scalaTest         = "org.scalatest"           %% "scalatest"         % Versions.scalaTest
    lazy val scalaTestCheck    = "org.scalatestplus"       %% "scalacheck-1-14"   % Versions.scalaTestCheck
    lazy val weaverCats        = weaver("weaver-cats", Versions.weaver)
    lazy val weaverFramework   = weaver("weaver-framework", Versions.weaver)
    lazy val weaverScalaCheck  = weaver("weaver-scalacheck", Versions.weaver)

    // Logical grouping for dependencies
    lazy val catsDeps         = Seq(catsCore, catsEffect, catsFree, catsKernel, catsMtl)
    lazy val circeDeps        = Seq(circeCore, circeFs2, circeGeneric, circeOptics, circeRefined, circeShapes)
    lazy val doobieDeps       = Seq(doobieCore, doobieFree, doobieHikari, doobiePostgres, doobieQuill)
    lazy val fuuidDeps        = Seq(fuuidCirce, fuuidCore, fuuidDoobie)
    lazy val http4sServerDeps = Seq(http4sEmberServer, http4sBlazeServer, http4sCirce, http4sCore, http4sDSL, http4sServer)
    lazy val loggingDeps      = Seq(log4catsCore, log4catsSl4j, logback, logbackEncoder, slf4jApi)

    lazy val opentracingDeps  = Seq(opentracingAmqp, opentracingClient, opentracingContext, opentracingCore, opentracingServer)
    lazy val pureconfigDeps = Seq(pureconfigCore, pureconfigGeneric, refinedPureConfig)
    lazy val quillDeps      = Seq(quillCore, quillCorePortable, quillJdbc, quillSql, quillSqlPortable)
    lazy val refinedDeps    = Seq(refinedCats, refinedCore)
    lazy val rhoDeps        = Seq(rhoCore, rhoSwagger, rhoSwaggerUI)
    // lazy val scalaPbDeps    = Seq(scalaPbLenses, scalaPbRunTime, scalaPbRunTimeProtobuf)

    lazy val sttpDeps =
        Seq(sttpCats, sttpCatsBackend, sttpCirce, sttpCore, sttpFs2Backend)

    lazy val weaverDeps = Seq(weaverCats, weaverFramework, weaverScalaCheck)


    lazy val uadetector          = "net.sf.uadetector"           % "uadetector-resources"  % "2014.10"

    val apiDeps: Seq[ModuleID] = catsDeps ++
        circeDeps ++
        doobieDeps ++
        fuuidDeps ++
        Seq(http4sCore, http4sCirce, http4sDSL) ++
        loggingDeps ++
        Seq(magnolia) ++
        opentracingDeps ++
        quillDeps ++
        // scalaPbDeps ++
        refinedDeps ++
        Seq(sttpModel) ++
        Seq(uadetector) ++
        rhoDeps

    val serverDeps: Seq[ModuleID] = catsDeps ++
        Seq(doobieCore) ++
        Seq(flyway) ++
        Seq(fs2Core) ++
        http4sServerDeps ++
        loggingDeps ++
        opentracingDeps ++
        pureconfigDeps

    val testDeps =
        (Seq(
        derevoScalaCheck,
        doobieTest,
        refinedScalaCheck,
        scalaCheck,
        scalaMock,
        scalaTest,
        scalaTestCheck
        ) ++
        sttpDeps ++
        weaverDeps)
        .map(_ % "test")
    
}