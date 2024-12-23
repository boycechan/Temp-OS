plugins {
    id("com.android.library")
}

group = "com.github.philjay"

android {
    compileSdk = Build.compileSdk
    buildToolsVersion = Build.buildTools
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
    testOptions {
        unitTests.isReturnDefaultValues = true // this prevents "not mocked" error
    }
}

dependencies {
    implementation(Deps.annotation)
    testImplementation(Deps.Test.junit)
}

val sourcesJar = tasks.register<Jar>("sourcesJar") {
    from(android.sourceSets["main"].java.srcDirs)
    archiveClassifier.set("sources")
}

val javadoc = tasks.register<Javadoc>("javadoc") {
    options.encoding("UTF-8")
    isFailOnError = false
    source = android.sourceSets["main"].java.getSourceFiles()
    classpath += project.files(android.bootClasspath + File.pathSeparator)
}

val javadocJar = tasks.register<Jar>("javadocJar") {
    dependsOn(javadoc)
    archiveClassifier.set("javadoc")
    from(javadoc.get().destinationDir)
}

artifacts {
    archives(sourcesJar)
    archives(javadocJar)
}
