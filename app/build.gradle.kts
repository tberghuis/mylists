plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.koin)
  alias(libs.plugins.ksp)

  id("kotlin-parcelize")
  alias(libs.plugins.room)
}

android {
  namespace = "xyz.tberghuis.mylists"
  compileSdk {
    version = release(36) {
      minorApiLevel = 1
    }
  }

  defaultConfig {
    applicationId = "xyz.tberghuis.mylists"
    minSdk = 24
    targetSdk = 36
    versionCode = 9
    versionName = "1.4.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures {
    compose = true
  }
}

dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  testImplementation(libs.junit)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  debugImplementation(libs.androidx.compose.ui.tooling)


  implementation(libs.koin.android)
  implementation(libs.koin.androidx.compose)

  implementation(libs.room.runtime)
  implementation(libs.room.ktx)
  ksp(libs.room.compiler)

  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.compose.material.iconsExtended)

  implementation(libs.androidx.dataStore.preferences)


  implementation(files("libs/jsch-0.1.55.jar"))
  implementation(libs.androidx.compose.runtime.livedata)

  implementation("org.burnoutcrew.composereorderable:reorderable:0.7.4")
}

room {
  schemaDirectory("$projectDir/schemas")
}