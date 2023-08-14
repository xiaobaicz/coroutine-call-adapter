plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

group="com.github.XiaoBaiCZ"

android {
    namespace = "cc.xiaobaicz.adapter"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        version = 230814

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    testImplementation(libs.junit)

    compileOnly(libs.retrofit)
    testImplementation(libs.retrofit)

    compileOnly(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.core)
}

afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release".
            create("release", MavenPublication::class.java) {
                // Applies the component for the release build variant.
                from(components["release"])
                // You can then customize attributes of the publication as shown below.
                groupId = "cc.xiaobaicz.adapter"
                artifactId = "CoroutineCallAdapter"
                version = "2.9.2"
            }
        }
    }
}