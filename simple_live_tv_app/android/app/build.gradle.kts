import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dev.flutter.flutter-gradle-plugin")
}

android {
    namespace = "com.xycz.simple_live_tv"
    compileSdk = flutter.compileSdkVersion
    ndkVersion = flutter.ndkVersion

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    defaultConfig {
        applicationId = "com.xycz.simple_live_tv"
        
        // 保持 21，兼容 Android 6.0 (API 23)
        minSdk = 21 
        targetSdk = 33
        
        versionCode = flutter.versionCode
        versionName = flutter.versionName
        multiDexEnabled = true
    }

    signingConfigs {
        create("legacy") {
            // 使用默认 debug 签名即可，关键是下面的 V1 开关
            storeFile = file("${System.getenv("ANDROID_HOME")}/.android/debug.keystore")
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("debug")
            
            // 【关键】关闭混淆，防止新版 R8 编译器生成的代码电视看不懂
            isMinifyEnabled = false
            isShrinkResources = false
            
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            // 【关键】Android 6.0 必须开启 V1 签名
            isV1SigningEnabled = true
            isV2SigningEnabled = true
        }
    }
}

flutter {
    source = "../.."
}
