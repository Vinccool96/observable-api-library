val kotlinVersion = "1.7.20"

plugins {
    kotlin("multiplatform") version "1.8.0"
}

group = "io.github.vinccool96.observable"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(BOTH) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
    }
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
                implementation("io.github.vinccool96.uncaught:uncaught-exception-handling:1.1")
                implementation("io.github.vinccool96.ref:weak-references:1.0")
                implementation("io.github.vinccool96.logging:logging:1.3")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {}
        }
        val jvmTest by getting
        val jsMain by getting {
            dependencies {}
        }
        val jsTest by getting
        val nativeMain by getting {
            dependencies {}
        }
        val nativeTest by getting
    }
}
