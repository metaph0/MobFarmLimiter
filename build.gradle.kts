plugins {
    `java-library`
    id("com.gradleup.shadow") version "9.4.1"
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    implementation("org.bstats:bstats-bukkit:3.2.1")
}

tasks {
    processResources {
        val props = mapOf(
            "version" to project.version,
            "description" to (project.description ?: "")
        )
        filesMatching(listOf("plugin.yml", "paper-plugin.yml")) { expand(props) }
    }

    jar {
        manifest { attributes["paperweight-mappings-namespace"] = "mojang" }
    }

    shadowJar {
        archiveFileName.set("${project.name}-${project.version}.jar")
        configurations = listOf(project.configurations.runtimeClasspath.get())
        dependencies { exclude { it.moduleGroup != "org.bstats" } }
        relocate("org.bstats", project.group.toString())
    }

    val version = "1.21.11"
    val jvmArgsExternal = listOf("-Dcom.mojang.eula.agree=true")

    val presetPlugins = runPaper.downloadPluginsSpec {
        url("https://cdn.modrinth.com/data/HYKaKraK/versions/ap8qHs7D/packetevents-spigot-2.12.1.jar")
        url("https://github.com/ViaVersion/ViaVersion/releases/download/5.9.0/ViaVersion-5.9.0.jar")
        url("https://github.com/ViaVersion/ViaBackwards/releases/download/5.9.0/ViaBackwards-5.9.0.jar")
    }


    runServer {
        minecraftVersion(version)
        runDirectory = rootDir.resolve("run/paper/$version")
        downloadPlugins { (from(presetPlugins)) }
        jvmArgs = jvmArgsExternal
    }

    runPaper.folia.registerTask {
        minecraftVersion(version)
        runDirectory = rootDir.resolve("run/folia/$version")
        downloadPlugins { (from(presetPlugins)) }
        jvmArgs = jvmArgsExternal
    }
}
