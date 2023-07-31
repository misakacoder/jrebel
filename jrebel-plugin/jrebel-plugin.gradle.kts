plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.14.1"
}

group = "com.misaka"
version = "1.0"

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

dependencies {
    implementation(fileTree("libs"))
}

intellij {
    version.set("2021.3")
    type.set("IC")
    plugins.set(listOf("java"))
}