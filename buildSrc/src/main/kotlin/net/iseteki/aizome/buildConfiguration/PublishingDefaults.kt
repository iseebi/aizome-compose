package net.iseteki.aizome.buildConfiguration

import org.gradle.api.publish.maven.MavenPom

fun MavenPom.defaultPom(libName: String) {
    name.set(libName)
    description.set("Customizable Styled-String(AnnotatedString) Generator/Formatter")
    url.set("https://github.com/iseebi/aizome-compose")

    licenses {
        licenses {
            name.set("Apache License, Version 2.0")
            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
        }
    }
    developers {
        it.developer { dev ->
            dev.id.set("iseebi")
            name.set("Nobuhiro Ito")
        }
    }
    scm {
        it.connection.set("scm:git:https://github.com/iseebi/aizome-compose.git")
        it.developerConnection.set("scm:git:ssh://git@github.com:iseebi/aizome-compose.git")
        url.set("https://github.com/iseebi/aizome")
    }
}
