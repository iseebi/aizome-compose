package net.iseteki.aizome.example.multiplatform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform