group = "com.misaka"
version = "1.0"

allprojects {
    repositories {
        maven {
            setUrl("https://maven.aliyun.com/nexus/content/groups/public")
        }
        maven {
            setUrl("https://maven.aliyun.com/repository/gradle-plugin")
        }
        mavenCentral()
    }
}

tasks.register("clean") {
    subprojects.forEach {
        dependsOn(":${it.name}:clean")
    }
    delete("${rootDir}/jrebel-plugin/libs", "${rootDir}/release")
}

tasks.register("release") {
    dependsOn("clean")
    dependsOn(":jrebel-agent:jar")
    dependsOn(":jrebel-plugin-2021.3:buildPlugin")
    dependsOn(":jrebel-plugin-2023.2:buildPlugin")
}