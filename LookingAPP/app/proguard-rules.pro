# Add project specific ProGuard rules here.
-keep class com.example.xhsapp.model.** { *; }
-keep class com.example.xhsapp.database.** { *; }
-keepattributes *Annotation*
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {<init>(...);}
# OkHttp
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
