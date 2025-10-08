# Keep debugging info for stack traces.
-keepattributes SourceFile,LineNumberTable

# These rules for standard Android components are often handled automatically
# by the Android Gradle Plugin, but are safe to keep for compatibility.
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.view.View

# Keep all public classes and members of Android Automotive API.
-keep public class android.car.** { public *; }
