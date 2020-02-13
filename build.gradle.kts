repositories {
    jcenter()
    maven(url= "https://dl.bintray.com/kotlin/ktor" )
    maven(url= "https://dl.bintray.com/kotlin/kotlinx" )
    maven(url= "https://dl.bintray.com/kotlin/kotlinx.html" )
    maven(url= "https://dl.bintray.com/kotlin/kotlin-js-wrappers" )
}

plugins {
    kotlin("multiplatform") version "1.3.61"
}

version = "0.1.0"

kotlin {
    targets {
        js {
            nodejs() {

            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
                implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
                implementation("org.jetbrains.kotlin:kotlin-test-common")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-js")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.3")
                implementation(npm("react"))
                implementation(npm("boardgame.io"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-js")
            }
        }
    }
}
