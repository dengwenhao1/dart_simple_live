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
        
        // 【重要】minSdk 设为 21 (Android 5.0)。
        // 你的电视是 Android 6.0 (API 23)，完全满足这个要求。
        // 设置太低(19)会导致新库编译失败，设置太高(24)会导致解析错误。
        minSdk = 21 
        
        // TargetSdk 适当降低，保持兼容
        targetSdk = 33
        
        versionCode = flutter.versionCode
        versionName = flutter.versionName
        
        // 开启 MultiDex 避免方法数超限崩溃
        multiDexEnabled = true
    }

    signingConfigs {
        // 创建一个兼容老电视的签名配置
        create("legacy") {
            // 使用 debug 密钥（无需你自己提供密钥）
            storeFile = file("${System.getenv("ANDROID_HOME")}/.android/debug.keystore")
            // 如果本地没有 debug.keystore，Gradle 会自动创建，或者我们可以暂时不指定路径让它用默认的
            // 为了保险，我们直接用 debug 的默认配置，但强制开启 V1
        }
    }

    buildTypes {
        // 配置 release 包
        getByName("release") {
            // 使用 debug 签名配置，以便我们可以手动开启 V1 签名
            signingConfig = signingConfigs.getByName("debug")
            
            // 【核心修复】关闭代码混淆 (R8)
            // 新版 R8 压缩的代码经常让 Android 6.0 解析失败
            isMinifyEnabled = false
            isShrinkResources = false
            
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        
        // 针对 debug 签名进行特殊配置
        getByName("debug") {
            // 【绝对关键】强制开启 V1 签名 (Jar Signature)
            // Android 6.0 及以下必须要有 V1 签名，否则报“解析软件包时出现问题”
            isV1SigningEnabled = true
            isV2SigningEnabled = true
        }
    }
}

flutter {
    source = "../.."
}
