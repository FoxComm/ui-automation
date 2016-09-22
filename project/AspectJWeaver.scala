import sbt.Keys._
import sbt._

object AspectJWeaver {

  // Use `sbt show ajwAgent` to see which jar is used
  val ajwAgent: TaskKey[File] = taskKey[File]("AspectJWeaver agent jar location")

  // Use separate config to find jar without hardcoding path
  lazy val cfg: Configuration = config("aspectjweaver-agent").hide

  lazy val settings: Seq[Setting[_]] = Seq(
    ivyConfigurations += cfg,
    fork in Test      := true, // Crucial for javaOptions to be picked up!
    ajwAgent          := findAgentJar(update.value),
    javaOptions       += s"-javaagent:${ajwAgent.value}"
  )

  private def findAgentJar(report: UpdateReport) = {
    val filter = configurationFilter("aspectjweaver-agent") && artifactFilter(`type` = "jar")
    report.matching(filter).head
  }
}
