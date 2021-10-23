plugins {
    kotlin("jvm") version "1.6.0-RC"
    kotlin("plugin.serialization") version "1.5.31"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "dev.themeinerlp"
version = "1.0-SNAPSHOT"

repositories {
    maven(url = "https://repo.spongepowered.org/maven")
    maven(url = "https://jitpack.io")
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.github.Minestom:Minestom:-SNAPSHOT")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    implementation("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT")
    implementation("com.google.zxing:core:3.4.1")
    implementation("com.google.zxing:javase:3.4.1")

}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "dev.themeinerlp.designserver.Main"
        attributes["Multi-Release"] = true
    }
}
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}