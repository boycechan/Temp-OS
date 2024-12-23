plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = Build.compileSdk
    buildToolsVersion = Build.buildTools

    defaultConfig {
        minSdk = Build.minSdk
        targetSdk = Build.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    splits {
        abi {
            isEnable = true
            reset()
            include("armeabi", "armeabi-v7a", "arm64-v8a", "mips", "x86", "x86_64")
        }
    }
}

dependencies {

    testImplementation(Deps.Test.junit)
    androidTestImplementation(Deps.Test.android_junit)
    androidTestImplementation(Deps.Test.runner)
    androidTestImplementation(Deps.Test.espresso)

    implementation(Deps.appcompat)
    implementation(Deps.material)

    compileOnly(Deps.core_ktx)

    api(files("libs/AMap3DMap_7.9.1_AMapLocation_5.3.1_20210414.jar"))
    api(Deps.Google.map)
    api(Deps.Google.maputils)
    api(Deps.Google.location)

}