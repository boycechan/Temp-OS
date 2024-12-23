plugins {
    id("com.android.library")
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
}

dependencies {

    testImplementation(Deps.Test.junit)
    androidTestImplementation(Deps.Test.android_junit)
    androidTestImplementation(Deps.Test.runner)
    androidTestImplementation(Deps.Test.espresso)

    implementation(Deps.appcompat)
    implementation(Deps.material)

    compileOnly(Deps.core_ktx)
}