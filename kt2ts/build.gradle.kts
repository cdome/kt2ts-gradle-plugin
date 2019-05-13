import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        jcenter()
    }
    dependencies {
        classpath("com.github.jengelman.gradle.plugins:shadow:5.0.0")
    }
}
plugins {
    kotlin("jvm")
    `maven-publish`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.10.0"
}
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

group = "se.jensim.kt2ts"
version = "0.1.0-SNAPSHOT"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(gradleApi())
    compileOnly("com.github.ntrrgc:ts-generator:1.1.1")
    implementation("org.reflections:reflections:0.9.11")

    testImplementation(kotlin("test-junit"))
}

tasks.withType<Jar> {
    val deps = listOf("ts-generator")
        .joinToString("|", "^(", ")$") { "$it.*.jar" }
    configurations["compileClasspath"]
        .filter { it.name.matches(Regex(deps)) }
        .forEach { file: File ->
            from(zipTree(file.absoluteFile))
        }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

pluginBundle {
    description = """
Generate TypeScript Definitions from your existing code.
In case you have your backend written in kotlin or java and you dont want to write your Angular type definitions yourself.
            """
    website = "https://github.com/jensim/kt2ts-gradle-plugin"
    vcsUrl = "https://github.com/jensim/kt2ts-gradle-plugin"
    tags = listOf("kotlin", "typescript", "kt2ts", "kt2js", "generate", "definitions", "ts-generator")
}

gradlePlugin {
    plugins {
        create("kt2ts") {
            id = "se.jensim.kt2ts"
            version = "0.1.0-SNAPSHOT"
            displayName = "Kotlin2TypeScript"
            implementationClass = "se.jensim.gradle.plugin.kt2ts.Kt2TsPlugin"
        }
    }
}

publishing {
    repositories {
        /*maven {
            url = uri("../../consuming/maven-repo")
        }
        ivy {
            url = uri("../../consuming/ivy-repo")
        }*/
        mavenLocal()
    }
}
