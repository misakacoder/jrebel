plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.14.1"
}

group = "com.misaka"
version = "2021.3"

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

dependencies {
    implementation(project(":jrebel-plugin"))
}

intellij {
    version.set("2021.3")
    type.set("IC")
    plugins.set(listOf("java"))
}

tasks.patchPluginXml {
    sinceBuild.set("213")
    untilBuild.set("232")
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