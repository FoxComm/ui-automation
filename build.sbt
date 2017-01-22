import java.io.File

name := "ui-tests"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.json" % "json" % "20160212",
  "org.seleniumhq.selenium" % "selenium-java" % "2.53.0",
  "com.codeborne" % "selenide" % "4.2",
  "org.apache.httpcomponents" % "httpclient" % "4.5.2",
  "com.squareup.okhttp" % "okhttp" % "2.7.5",
  "org.testng" % "testng" % "6.10",
  "org.aspectj" % "aspectjweaver" % "1.8.9" % AspectJWeaver.cfg,
  "ru.yandex.qatools.allure" % "allure-testng-adaptor" % "1.4.23",
  "org.apache.maven.plugins" % "maven-surefire-plugin" % "2.19.1",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)

javacOptions ++= Seq(
  "-encoding", "UTF-8"
)

libraryDependencies ~= { _.map(_.exclude("org.slf4j", "slf4j-jdk14")) }
libraryDependencies ~= { _.map(_.exclude("org.slf4j", "jcr-over-slf4j")) }
libraryDependencies ~= { _.map(_.exclude("org.slf4j", "slf4j-nop")) }

AspectJWeaver.settings

val cleanAllure = TaskKey[Unit]("Delete allure results and reports")

cleanAllure <<= Def.task().apply { task ⇒
  IO.delete(Seq(new File("target/allure-results"), new File("allure-report")))
  task
}.runBefore(test in Test)
