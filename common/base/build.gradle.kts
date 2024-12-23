plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Build.compileSdk
    buildToolsVersion = Build.buildTools

    defaultConfig {
        minSdk = Build.minSdk
        targetSdk = Build.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }
    }

    //统一规范资源名称前缀，防止多个 module 之间资源冲突
//    resourcePrefix "base_"

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    testImplementation(Deps.Test.junit)
    androidTestImplementation(Deps.Test.android_junit)
    androidTestImplementation(Deps.Test.runner)
    androidTestImplementation(Deps.Test.espresso)

    compileOnly(Deps.core_ktx)

    compileOnly(Deps.kotlinx_coroutines_core)
    compileOnly(Deps.kotlinx_coroutines_android)
    compileOnly(Deps.Lifecycle.viewmodel_ktx)

    //lifecycle
    kapt(Deps.Lifecycle.compiler)
    //room
    api(Deps.Room.runtime)
    api(Deps.Room.ktx)
    kapt(Deps.Room.compiler)
    //hilt
    implementation(Deps.Hilt.android)
    kapt(Deps.Hilt.android_compiler)
    implementation(Deps.Hilt.viewmodel)
    kapt(Deps.Hilt.compiler)

    api(Deps.ARouter.api)
    kapt(Deps.ARouter.compiler)

    api(project(":common:retrofit"))
    api(project(":common:res"))

    api(Deps.appcompat)
    api(Deps.timber)
    api(Deps.logger)

    api(Deps.glide)
    kapt(Deps.glide_compiler)

    api(Deps.recyclerview)
    //recyclerview adapter
    api(Deps.multitype)

    //immersionbar
    api(Deps.Immersionbar.base)
    api(Deps.Immersionbar.ktx)

    api(Deps.eventbus)

    compileOnly(Deps.utilcodex)

    coreLibraryDesugaring(Deps.desugar_jdk_libs)
}