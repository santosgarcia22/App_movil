plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.app_movil"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.app_movil"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    dependencies {
        implementation("com.google.mlkit:text-recognition:16.0.0")
        implementation( "com.android.volley:volley:1.2.1" )
        implementation("com.google.code.gson:gson:2.10.1")
        implementation( "com.google.code.gson:gson:2.10.1")
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation("com.squareup.okhttp3:okhttp:4.9.3")
        implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
        implementation("com.facebook.stetho:stetho-okhttp3:1.6.0")
        implementation ("com.google.code.gson:gson:2.10")

        // otras dependencias...
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)



}