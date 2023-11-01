plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
}

android {
    namespace = "cc.xiaobaicz.adapter"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

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

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "cc.xiaobaicz.adapter"
            artifactId = "coroutine-call-adapter"
            version = "2.9.3"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}