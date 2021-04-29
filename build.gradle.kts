plugins {
    kotlin("jvm") version "1.4.32"
}
allprojects {
    apply(plugin = "kotlin")

    repositories {
        jcenter()
        maven ("https://jitpack.io/")
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://gitlab.com/XjCyan1de/maven-repo/-/raw/master/")

    }

    dependencies {
        compileOnly("com.destroystokyo.paper", "paper-api", "1.16.5-R0.1-SNAPSHOT")
        compileOnly("org.yatopiamc:yatopia:1.16.4-R0.1-SNAPSHOT")
    }

    tasks {
        val kotlinOptions: org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions.() -> Unit = {
            jvmTarget = "15"
            freeCompilerArgs = listOf(
                "-Xjvm-default=all",
                "-Xopt-in=kotlin.RequiresOptIn"
            )
        }
        java {
            sourceCompatibility = JavaVersion.VERSION_15
            targetCompatibility = JavaVersion.VERSION_15
        }
        compileKotlin {
            kotlinOptions(kotlinOptions)
        }
        compileTestKotlin {
            kotlinOptions(kotlinOptions)
        }
    }
}