plugins {
    java
    id("com.gradleup.shadow") version "9.3.1"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    implementation("org.bstats:bstats-bukkit:3.1.0")
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

    runServer {
        minecraftVersion("1.21.1")
        jvmArgs("-Dcom.mojang.eula.agree=true")
    }
}