import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.myassignment"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myassignment"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments.putAll(mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                ))
            }
        }

        buildConfigField("String", "BASE_URL", "\"https://api.restful-api.dev/\"")

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

    applicationVariants.all{
        outputs.all {
            val formattedDate = getDate()
            (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl).outputFileName = "my_assignment_${formattedDate}_$versionName.apk"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        buildConfig = true

        viewBinding = true

    }

    packagingOptions {
        resources.excludes.add("META-INF/DEPENDENCIES")
    }
}

fun Date.formatToCustomString(): String {
    val dateFormat = SimpleDateFormat("ddMMMyyyy_HH-mm")
    return dateFormat.format(this)
}
fun getDate(): String {
    val date = Date()
    return date.formatToCustomString()
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("io.insert-koin:koin-core:3.1.5") // For core functionality
    implementation("io.insert-koin:koin-android:3.1.5")

    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-auth")
    implementation("androidx.credentials:credentials")
    implementation("androidx.credentials:credentials-play-services-auth")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.20.0")
    implementation("com.google.android.gms:play-services-auth:20.4.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    val moshi = "com.squareup.moshi:moshi-kotlin:1.15.0"
    val moshiCodeGen = "com.squareup.moshi:moshi-kotlin-codegen:1.15.0"
    implementation(moshi)
    kapt(moshiCodeGen)

    implementation("androidx.room:room-runtime:2.3.0")
    implementation("androidx.room:room-ktx:2.3.0")
    kapt("androidx.room:room-compiler:2.3.0")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.0")

    implementation("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")

    implementation("com.github.dhaval2404:imagepicker:2.1")
    implementation("com.karumi:dexter:6.2.3")
    implementation("com.github.hadiyarajesh:flower:1.0.0")
    implementation("com.zplesac:connectionbuddy:2.0.1@aar")

    implementation("io.coil-kt:coil:2.5.0")


}