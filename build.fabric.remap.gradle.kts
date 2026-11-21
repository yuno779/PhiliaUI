@file:Suppress("UnstableApiUsage", "MISSING_DEPENDENCY_SUPERCLASS_IN_TYPE_ARGUMENT")

import java.util.*

plugins {
    id("mod-plugin")
    id("maven-publish")
    id("net.fabricmc.fabric-loom-remap")
    id("com.replaymod.preprocess")
}

version = fullProjectVersion
group = modMavenGroup

// 仅 1.21.11 使用的 ModMenu 入口（供 MasaGadget 下拉框枚举）
sourceSets {
    main {
        java.srcDir("src/main/java")
    }
}

repositories {
    // Optional local mirror for environments where the Modrinth CDN is
    // unreliable. It is ignored by git and is not required for normal builds.
    val localMirror = rootProject.file(".m2repo")
    if (localMirror.isDirectory) {
        maven { url = uri(localMirror) }
    }
    fun strictMaven(url: String, vararg groups: String) = exclusiveContent {
        forRepository { maven(url) }
        filter {
            groups.forEach {
                includeGroupAndSubgroups(it)
                includeGroupAndSubgroups("$it.*")
            }
        }
    }
    strictMaven("https://maven.fabricmc.net")
    strictMaven("https://www.cursemaven.com", "curse.maven")
    strictMaven("https://api.modrinth.com/maven", "maven.modrinth")
    strictMaven("https://jitpack.io")
    strictMaven("https://maven.terraformersmc.com/releases/", "com.terraformersmc")
}

// https://github.com/FabricMC/fabric-loader/issues/783
configurations.all {
    resolutionStrategy {
        force("net.fabricmc:fabric-loader:$fabricLoaderVersion")
        force("maven.modrinth:malilib:${prop("malilib")}")
    }
    exclude(group = "com.github.sakura-ryoko", module = "malilib")
}

dependencies {
    minecraft("com.mojang:minecraft:$mcVersion")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    modImplementation("maven.modrinth:malilib:$malilib")
    modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:${prop("fabric_api")}")
    modCompileOnly("com.terraformersmc:modmenu:17.0.1")
    modLocalRuntime("com.terraformersmc:modmenu:17.0.1")
}

loom {
    runs {
        named("client") {
            client()
            runDirectory.set(file("../../run/client"))
            jvmArguments.add("-Dmixin.debug.export=true")
            jvmArguments.add("-Dmixin.debug.countInjections=false")
            jvmArguments.add("-Dmixin.env.remapRefMap=true")
            programArguments.addAll("--width", "1280")
            programArguments.addAll("--height", "720")
            programArguments.addAll("--username", "MaLiLibUITest")
            generateRunConfig.set(true)
        }
    }
}

tasks {
    register<Copy>("buildAndCollect") {
        group = "build"
        delete(project.layout.buildDirectory.dir("libs"))
        dependsOn("build")
        from(remapJar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.dir("libs"))
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = modId
            version = modVersion
        }
    }
    repositories {
        mavenLocal()
        maven {
            url = uri("$rootDir/publish")
        }
    }
}
