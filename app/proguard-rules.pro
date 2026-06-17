# Keep kotlinx.serialization generated serializers.
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keepclassmembers class com.ainotetaker.app.model.** {
    *** Companion;
}
-keepclasseswithmembers class com.ainotetaker.app.model.** {
    kotlinx.serialization.KSerializer serializer(...);
}
