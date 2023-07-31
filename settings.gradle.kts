rootProject.name = "jrebel"

include("jrebel-agent")
include("jrebel-plugin")
include("jrebel-plugin-2021.3")
include("jrebel-plugin-2023.2")

rootProject.children.forEach {
    it.buildFileName = "${it.name}.gradle.kts"
}
