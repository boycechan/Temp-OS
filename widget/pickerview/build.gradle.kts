plugins {
    id("com.android.library")
}

android {
    compileSdk = Build.compileSdk

    defaultConfig {
        minSdk = Build.minSdk
        targetSdk = Build.targetSdk
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    lintOptions {
        isAbortOnError = false
    }
}

dependencies {
    api(project(":widget:wheelview"))
    implementation(Deps.appcompat)
}