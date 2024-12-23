import java.util.Properties

pluginManagement {
    // 读取 Flutter SDK 路径
    val flutterSdkPath: String = run {
        val properties = Properties()
        file("local.properties").inputStream().use { properties.load(it) }
        val sdkPath = properties.getProperty("flutter.sdk")
        checkNotNull(sdkPath) { "flutter.sdk not set in local.properties" }
        sdkPath
    }

    // 包含 Flutter 工具的 Gradle 脚本
    includeBuild("$flutterSdkPath/packages/flutter_tools/gradle")

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// 引用外部 Gradle 配置文件
apply(from = "tg_flutter/flutter_settings.gradle")

// 包含的模块
include(
        ":common:base", ":common:res", ":common:retrofit", ":common:utils",
        ":widget:calendarview", ":widget:map", ":widget:wheelview",
        ":widget:pickerview", ":widget:mpchart", ":widget:sidebar"
)
include(":app")

// 项目名称
rootProject.name = "Deepblu-v2"
