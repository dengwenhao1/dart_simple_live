import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("kotlin-android")
    // The Flutter Gradle Plugin must be applied after the Android and Kotlin Gradle plugins.
    id("dev.flutter.flutter-gradle-plugin")
}

android {
    namespace = "com.xycz.simple_live_tv"
    compileSdk = flutter.compileSdkVersion
    ndkVersion = flutter.ndkVersion

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    defaultConfig {
        // TODO: Specify your own unique Application ID (https://developer.android.com/studio/build/application-id.html).
        applicationId = "com.xycz.simple_live_tv"
        // You can update the following values to match your application needs.
        // For more information, see: https://flutter.dev/to/review-gradle-config.
        
        // 【关键修改1】强制改为 21，兼容 Android 6.0
        minSdk = 21 
        
        targetSdk = flutter.targetSdkVersion
        versionCode = flutter.versionCode
        versionName = flutter.versionName
    }

    // 【关键修改2】我删除了原来的 signingConfigs { create("release") ... } 代码块
    // 因为你没有密钥，保留那段代码会导致编译报错（空指针异常）。
    // 现在直接使用默认的 debug 签名配置，无需任何设置。

    buildTypes {
        release {
            // 【关键修改3】这里改成了 signingConfigs.getByName("debug")
            // 这样打包时会使用测试签名，没有密钥也能安装
            signingConfig = signingConfigs.getByName("debug")
            
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                // Default file with automatically generated optimization rules.
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

flutter {
    source = "../.."
}
