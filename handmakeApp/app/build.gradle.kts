plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.handmakeapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.handmakeapp"
        minSdk = 23

        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}



dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment:2.6.0")
    implementation("androidx.navigation:navigation-ui:2.6.0")

    implementation("androidx.activity:activity:1.8.0")
    implementation("com.google.firebase:firebase-auth:23.0.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")
    implementation("androidx.multidex:multidex:2.0.1")

    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.squareup.retrofit2:retrofit:2.1.0")
    implementation("com.squareup.retrofit2:converter-gson:2.1.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")


//    phung - picasso: tải, hiển thị hình ảnh bằng url
    implementation("com.squareup.picasso:picasso:2.5.2")
//    phung - Okhttp: kết nối -> tải, upload dữ liệu
    implementation("com.squareup.okhttp3:okhttp:4.9.1")

    // firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")

    implementation(files("libs/activation.jar"))
    implementation(files("libs/additionnal.jar"))
    implementation(files("libs/mail.jar"))
//  phung - thanh image slide
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("com.makeramen:roundedimageview:2.3.0")

}