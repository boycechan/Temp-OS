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
}

dependencies {

    testImplementation(Deps.Test.junit)
    androidTestImplementation(Deps.Test.android_junit)
    androidTestImplementation(Deps.Test.runner)
    androidTestImplementation(Deps.Test.espresso)

    compileOnly(Deps.core_ktx)

    //retrofit
    api(Deps.retrofit)
    api(Deps.gson)
    api(Deps.converter_gson)
    api(Deps.okhttp)

}