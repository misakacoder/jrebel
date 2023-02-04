group = "com.misaka"
version = "1.0"

allprojects {
    repositories {
        maven {
            setUrl("https://maven.aliyun.com/nexus/content/groups/public/")
        }
        mavenCentral()
    }
}

gradle.buildFinished {
    delete(buildDir)
}