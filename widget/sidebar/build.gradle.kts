plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = Build.compileSdk

    defaultConfig {
        minSdk = Build.minSdk
        targetSdk = Build.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    compileOnly(Deps.core_ktx)
    implementation(Deps.appcompat)
    implementation(Deps.material)
    testImplementation(Deps.Test.junit)
    androidTestImplementation(Deps.Test.android_junit)
    androidTestImplementation(Deps.Test.runner)
    androidTestImplementation(Deps.Test.espresso)
}