# Keep serializers
-keepclassmembers class * implements kotlinx.serialization.Serializable {
    public static ** Companion;
}

# Keep Ktor serialization providers
-keep class io.ktor.serialization.kotlinx.** { *; }
-keep class kotlinx.serialization.json.** { *; }