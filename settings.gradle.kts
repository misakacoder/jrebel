rootProject.name = "jrebel"

include("jrebel-agent")
include("jrebel-plugin")

rootProject.children.forEach {
    it.buildFileName = "${it.name}.gradle.kts"
}
