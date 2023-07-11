import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.zonglinpeng.litcode"
version = "1.0.0"

repositories {
  mavenCentral()
}

val vertxVersion = "4.1.2"
val junitJupiterVersion = "5.7.0"
val jacksonVersion = "2.12.4"
val lombokVersion = "1.18.20"

val mainVerticleName = "com.zonglinpeng.litcode.litcode.MainVerticle"
//val launcherClassName = "io.vertx.core.Launcher"
val launcherClassName = "com.zonglinpeng.litcode.litcode.Litcode"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencies {
  // vertx
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-web-client")
  implementation("io.vertx:vertx-health-check")
  implementation("io.vertx:vertx-web-openapi")
  implementation("io.vertx:vertx-auth-oauth2")
//  implementation("io.vertx:vertx-reactive-streams")
  implementation("io.vertx:vertx-jdbc-client")
  implementation("io.vertx:vertx-sql-client-templates")
//  implementation("io.vertx:vertx-web-sstore-cookie")
  implementation("io.vertx:vertx-auth-sql-client")
  implementation("io.vertx:vertx-web-validation")
  implementation("io.vertx:vertx-auth-jwt")
  implementation("io.vertx:vertx-web")
  implementation("io.vertx:vertx-mysql-client")
  implementation("io.vertx:vertx-web-api-contract")
  implementation("io.vertx:vertx-config")
  implementation("io.vertx:vertx-circuit-breaker")
  implementation("io.vertx:vertx-auth-jdbc")
  implementation("io.vertx:vertx-json-schema")
  implementation("io.reactivex.rxjava3:rxjava:3.0.13")
  implementation("io.vertx:vertx-rx-java3")
  // logger
  implementation("org.slf4j:slf4j-api:1.7.30")
  implementation("org.slf4j:slf4j-simple:1.7.30")

//  implementation("org.apache.logging.log4j:log4j-core:2.14.1")
//  implementation("org.apache.logging.log4j:log4j-api:2.14.1")

  // json
  compileOnly("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
  // lombok
  compileOnly("org.projectlombok:lombok:$lombokVersion")
  annotationProcessor("org.projectlombok:lombok:$lombokVersion")

  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
  testCompileOnly("org.projectlombok:lombok:$lombokVersion")
  testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(
      mapOf(
        "Main-Verticle" to mainVerticleName,
        "Main-Class" to launcherClassName
      )
    )
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf()
}
