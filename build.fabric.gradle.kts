@file:Suppress("UnstableApiUsage")

plugins {
    id("mod-plugin")
    id("maven-publish")
    id("net.fabricmc.fabric-loom")
    id("com.replaymod.preprocess")
}

version = fullProjectVersion
group = modMavenGroup

base {
    archivesName.set("$modArchivesBaseName-$mcVersion")
}

// 固定使用与目标 Minecraft 版本匹配的 Java 工具链（26.x 为 JDK 25），
// 避免宿主机 JDK 版本差异导致的编译问题。
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion.majorVersion))
    }
}

repositories {
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
    strictMaven("https://api.modrinth.com/maven", "maven.modrinth")
    strictMaven("https://maven.terraformersmc.com/releases/", "com.terraformersmc")
}

// https://github.com/FabricMC/fabric-loader/issues/783
configurations.all {
    resolutionStrategy {
        force("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:$mcVersion")
    implementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    implementation("maven.modrinth:malilib:$malilib")
    // 仅用于 runClient 中打开 masa 系配置界面做视觉验证，并非本模组的运行前置
    implementation("maven.modrinth:litematica:$litematica")
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
        dependsOn("build")
        delete(project.layout.buildDirectory.dir("libs"))
        from(jar.map { it.archiveFile })
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
