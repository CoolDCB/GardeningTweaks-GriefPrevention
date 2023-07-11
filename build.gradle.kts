plugins {
    java
    `kotlin-dsl`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version("7.1.2")
}

group = "me.dave"
version = "1.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("https://oss.sonatype.org/content/groups/public/") }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20-R0.1-SNAPSHOT")
    compileOnly("com.github.CoolDCB:GardeningTweaks:1.3")
    compileOnly("com.github.TechFortress:GriefPrevention:16.18")
}

java {
    configurations.shadow.get().dependencies.remove(dependencies.gradleApi())
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.shadowJar {
    minimize()
    configurations = listOf(project.configurations.shadow.get())
    val folder = System.getenv("pluginFolder_1-20")
    if (folder != null) destinationDirectory.set(file(folder))
    archiveFileName.set("${project.name}-${project.version}.jar")
}

// Handles version variables
tasks.processResources {
    expand(project.properties)

    inputs.property("version", rootProject.version)
    filesMatching("plugin.yml") {
        expand("version" to rootProject.version)
    }
}