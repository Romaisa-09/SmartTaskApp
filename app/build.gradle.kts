plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace   = "com.example.taskapp"
    compileSdk  = 35

    defaultConfig {
        applicationId = "com.example.taskapp"
        minSdk        = 24
        targetSdk     = 35
        versionCode   = 1
        versionName   = "1.0"
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // ** Prevent duplicate Kotlin stdlib modules **
    configurations.all {
        // Force everyone to use kotlin-stdlib 1.8.10
        resolutionStrategy.force("org.jetbrains.kotlin:kotlin-stdlib:1.8.10")

        // Drop any old jdk7/jdk8 variants
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk7")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata:2.6.1")

    implementation("androidx.room:room-runtime:2.5.2") {
        // also exclude stray stdlibs
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk7")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
    }
    annotationProcessor("androidx.room:room-compiler:2.5.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
