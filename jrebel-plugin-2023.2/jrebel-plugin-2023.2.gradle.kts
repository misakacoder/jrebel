plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.14.1"
}

group = "com.misaka"
version = "2023.2"

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

dependencies {
    implementation(project(":jrebel-plugin"))
}

intellij {
    version.set("2023.2")
    type.set("IC")
    plugins.set(listOf("java"))
}

tasks.patchPluginXml {
    sinceBuild.set("232")
    untilBuild.set("999")
}

tasks.buildPlugin {
    archiveFileName.set("${project.name}.zip")
    doLast {
        copy {
            from("$buildDir/distributions")
            into("${rootDir}/release")
        }
    }
}