name := "ui-tests"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.json" % "json" % "20160212",
  "org.seleniumhq.selenium" % "selenium-java" % "2.53.0",
  "com.codeborne" % "selenide" % "3.6",
  "org.apache.httpcomponents" % "httpclient" % "4.5.2",
  "com.squareup.okhttp" % "okhttp" % "2.7.5",
  "org.testng" % "testng" % "6.9.10",
  "org.aspectj" % "aspectjweaver" % "1.8.6",
  "ru.yandex.qatools.allure" % "allure-testng-adaptor" % "1.4.23",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)

javacOptions ++= Seq(
  "-encoding", "UTF-8"
)
