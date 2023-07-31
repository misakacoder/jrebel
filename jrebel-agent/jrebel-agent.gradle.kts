plugins {
    id("java")
}

group = "com.misaka"
version = "1.0"

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

dependencies {
    implementation("org.javassist", "javassist", "3.29.0-GA")
    compileOnly("org.mybatis", "mybatis", "3.5.0")
}

tasks.jar {
    val manifest = hashMapOf<String, Any>()
    manifest["Premain-Class"] = "com.misaka.agent.JRebelAgent"
    manifest["Can-Redefine-Classes"] = true
    manifest {
        attributes(manifest)
    }
    from(configurations.runtimeClasspath.get().filter { it.name.endsWith(".jar") }.map { zipTree(it) })
    archiveFileName.set("${project.name}.jar")
}

tasks.jar {
    this.doLast {
        copy {
            from("$buildDir/libs")
            into("${rootDir}/jrebel-plugin/libs")
        }
    }
}