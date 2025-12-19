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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    defaultConfig {
        applicationId = "com.xycz.simple_live_tv"
        
        // 【修改1】降级到 19，极大增加宽容度
        minSdk = 19 
        // 【修改2】降级 TargetSDK 到 29 (Android 10)，部分老旧电视对 Android 12+ 的 Target 支持极差
        targetSdk = 29
        
        versionCode = flutter.versionCode
        versionName = flutter.versionName
        
        // 【修改3】开启 MultiDex，防止因为 minSdk 低于 21 导致的崩溃（虽然后面禁用了混淆，加这个是为了保险）
        multiDexEnabled = true
    }

    signingConfigs {
        // 配置 debug 签名，强制开启 V1 和 V2
        getByName("debug") {
            isV1SigningEnabled = true  // 【关键】Android 6.0 必须要有 V1 签名
            isV2SigningEnabled = true
        }
    }

    buildTypes {
        release {
            // 使用 debug 签名
            signingConfig = signingConfigs.getByName("debug")
            
            // 【修改4】关闭混淆和资源压缩
            // 很多时候“解析错误”是因为 R8 编译器生成了 Android 6.0 看不懂的优化代码
            isMinifyEnabled = false
            isShrinkResources = false 
            
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

flutter {
    source = "../.."
}
