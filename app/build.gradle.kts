plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.ruderarajput.ecommerce"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ruderarajput.ecommerce"
        minSdk = 24
        //noinspection EditedTargetSdkVersion
        targetSdk = 34
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
        viewBinding {
            enable=true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:22.2.0")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Glide Image
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    // Circle ImageView
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    // Rounded ImageView
    implementation ("com.makeramen:roundedimageview:2.3.0")
    // Material SearchBar
    implementation ("com.github.mancj:MaterialSearchBar:0.8.5")

    //slider dependencies
    implementation ("com.google.android.material:material:1.5.0")
    implementation ("me.relex:circleindicator:2.1.6")
    implementation ("org.imaginativeworld.whynotimagecarousel:whynotimagecarousel:2.1.0")

    //Volly Library
    implementation ("com.android.volley:volley:1.2.1")
    //lottie animation
    implementation ("com.airbnb.android:lottie:4.0.0")
    //tinycart
    implementation ("com.github.hishd:TinyCart:1.0.1")
    //webView advanced
    implementation ("com.github.delight-im:Android-AdvancedWebView:v3.2.1")
}