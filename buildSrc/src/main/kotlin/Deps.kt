/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/10/12 14:58
 * @desc Deps
 */
object Versions {

    const val gradle = "7.0.2"
    const val kotlin = "1.5.31"
    const val protobuf_gradle = "0.8.17"

    const val appcompat = "1.2.0"
    const val annotation = "1.1.0"
    const val coreKtx = "1.3.0"
    const val material = "1.2.1"
    const val constraintlayout = "2.0.4"
    const val junit = "4.13"
    const val androidExtJunit = "1.1.2"
    const val runner = "1.3.0"
    const val espresso = "3.3.0"
    const val swiperefreshlayout = "1.1.0"
    const val recyclerview = "1.1.0"

    //room和lifecycle的kotion-coroutines要保持版本一致
    const val kotlinxCoroutines = "1.4.1"
    const val viewmodelKtx = "2.3.1"
    const val lifecycle = "2.3.1"
    const val room = "2.3.0"
    const val hiltAndroid = "2.39.1"
    const val hilt = "1.0.0-alpha03"

    const val multidex = "2.0.1"

    const val retrofit = "2.9.0"
    const val okhttp = "4.9.0"
    const val gson = "2.8.6"

    const val timber = "4.7.1"
    const val logger = "2.2.0"

    const val glide = "4.11.0"

    const val leakcanary = "2.6"

    const val arouter = "1.5.2"
    const val arouterCompiler = "1.5.2"

    const val immersionbar = "3.0.0"

    const val multitype = "4.2.0"

    const val mpandroidchart = "v3.1.0"

    const val utilcodex = "1.31.0"

    const val googlemap = "18.0.2"
    const val googlelocation = "18.0.0"
    const val googlemaputils = "2.3.0"

    const val refresh = "2.0.1"

    const val qrcode = "1.3.7"
    const val work = "2.5.0"

    const val eventbus = "3.2.0"
    const val protobuf = "3.18.1"

    const val camerax_version = "1.0.0"

    const val libphonenumber = "8.12.31"

    const val swipeRecyclerView = "1.3.2"

    const val ucrop = "2.2.6"

    const val desugar_jdk_libs = "1.1.5"

    const val guava_android = "29.0-android"
}

object Plugin {
    const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
    const val protobuf_gradle = "com.google.protobuf:protobuf-gradle-plugin:${Versions.protobuf_gradle}"
    const val kotlin_gradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val hilt_android_gradle = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltAndroid}"
}

object Deps {

    const val material = "com.google.android.material:material:${Versions.material}"

    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"

    const val annotation = "androidx.annotation:annotation:${Versions.annotation}"

    const val swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swiperefreshlayout}"

    const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"

    const val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"

    const val multidex = "androidx.multidex:multidex:${Versions.multidex}"

    const val kotlin_stdlib_jdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val kotlin_stdlib_jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    const val core_ktx = "androidx.core:core-ktx:${Versions.coreKtx}"

    const val kotlinx_coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinxCoroutines}"
    const val kotlinx_coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinxCoroutines}"

    object Test {
        const val junit = "junit:junit:${Versions.junit}"
        const val android_junit = "androidx.const val ext:junit:${Versions.androidExtJunit}"
        const val runner = "androidx.test:runner:${Versions.runner}"
        const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    }

    object Lifecycle {
        const val viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.viewmodelKtx}"
        const val runtime = "androidx.lifecycle:lifecycle-runtime:${Versions.lifecycle}"
        const val extensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
        const val compiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"
        const val viewmodel_savedstate = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.lifecycle}"
    }

    object Room {
        const val runtime = "androidx.room:room-runtime:${Versions.room}"
        const val compiler = "androidx.room:room-compiler:${Versions.room}"
        const val rxjava2 = "androidx.room:room-rxjava2:${Versions.room}"
        const val ktx = "androidx.room:room-ktx:${Versions.room}"
    }

    object Hilt {
        const val android = "com.google.dagger:hilt-android:${Versions.hiltAndroid}"
        const val android_compiler = "com.google.dagger:hilt-android-compiler:${Versions.hiltAndroid}"
        const val viewmodel = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hilt}"
        const val compiler = "androidx.hilt:hilt-compiler:${Versions.hilt}"
    }

    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val converter_gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"

    const val gson = "com.google.code.gson:gson:${Versions.gson}"

    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"

    object ARouter {
        const val api = "com.alibaba:arouter-api:${Versions.arouter}"
        const val compiler = "com.alibaba:arouter-compiler:${Versions.arouterCompiler}"
    }

    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val logger = "com.orhanobut:logger:${Versions.logger}"

    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glide_compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

    const val leakcanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakcanary}"

    object Immersionbar {
        const val base = "com.gyf.immersionbar:immersionbar:${Versions.immersionbar}"// 基础依赖包，必须要依赖
        const val components = "com.gyf.immersionbar:immersionbar-components:${Versions.immersionbar}"// fragment快速实现（可选）
        const val ktx = "com.gyf.immersionbar:immersionbar-ktx:${Versions.immersionbar}" // kotlin扩展（可选）
    }

    const val multitype = "com.drakeet.multitype:multitype:${Versions.multitype}"

    //图表库
    const val mpandroidchart = "com.github.PhilJay:MPAndroidChart:${Versions.mpandroidchart}"

    const val utilcodex = "com.blankj:utilcodex:${Versions.utilcodex}"

    //二维码
    const val qrcode = "cn.bingoogolapple:bga-qrcode-zxing:${Versions.qrcode}"

    object Google {
        const val map = "com.google.android.gms:play-services-maps:${Versions.googlemap}"
        const val maputils = "com.google.maps.android:android-maps-utils:${Versions.googlemaputils}"
        const val location = "com.google.android.gms:play-services-location:${Versions.googlelocation}"
    }

    object Refresh {
        const val layout_kernel = "com.scwang.smart:refresh-layout-kernel:${Versions.refresh}"  //核心必须依赖
        const val header_classics = "com.scwang.smart:refresh-header-classics:${Versions.refresh}"  //经典刷新头
        const val header_radar = "com.scwang.smart:refresh-header-radar:${Versions.refresh}"    //雷达刷新头
        const val header_falsify = "com.scwang.smart:refresh-header-falsify:${Versions.refresh}"    //虚拟刷新头
        const val header_material = "com.scwang.smart:refresh-header-material:${Versions.refresh}"  //谷歌刷新头
        const val header_two_level = "com.scwang.smart:refresh-header-two-level:${Versions.refresh}"    //二级刷新头
        const val footer_ball = "com.scwang.smart:refresh-footer-ball:${Versions.refresh}"  //球脉冲加载
        const val footer_classics = "com.scwang.smart:refresh-footer-classics:${Versions.refresh}"  //经典加载
    }

    object Work {
        const val runtime_ktx = "androidx.work:work-runtime-ktx:${Versions.work}"
        const val testing = "androidx.work:work-testing:${Versions.work}"
    }

    const val eventbus = "org.greenrobot:eventbus:${Versions.eventbus}"
    const val protobuf = "com.google.protobuf:protobuf-java:${Versions.protobuf}"

    object Camera {
        const val camera2 = "androidx.camera:camera-camera2:${Versions.camerax_version}"
        const val lifecycle = "androidx.camera:camera-lifecycle:${Versions.camerax_version}"
        const val view = "androidx.camera:camera-view:1.0.0-alpha24"
    }

    const val libphonenumber = "com.googlecode.libphonenumber:libphonenumber:${Versions.libphonenumber}"

    const val swipeRecyclerView = "com.yanzhenjie.recyclerview:x:${Versions.swipeRecyclerView}"

    const val ucrop = "com.github.yalantis:ucrop:${Versions.ucrop}"

    const val desugar_jdk_libs = "com.android.tools:desugar_jdk_libs:${Versions.desugar_jdk_libs}"

    const val guava_android = "com.google.guava:guava:${Versions.guava_android}"

}