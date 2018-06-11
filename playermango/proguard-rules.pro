# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontwarn com.coden.**
-dontwarn com.nexstreaming.**
-dontwarn com.mdialog.android.**
-dontwarn com.google.ads.**
-dontwarn org.apache.commons.logging.**
-dontwarn org.apache.log4j.**
-dontwarn org.slf4j.**
-dontwarn org.joda.convert.FromString
-dontwarn org.joda.convert.ToString

# common
-ignorewarnings
-keep class * {
    public private *;
}

# visual on
-keep class com.discretix.** { *; }
-keep class com.visualon.** { *; }

# exoplayer
-keep class com.google.** { *; }

# sqlcipher
-keep class net.sqlcipher.** { *; }

# support
-keep class android.support.** { *; }

# suplunk
#-keep class com.splunk.** { *; }

# ncg
-keep class com.inka.ncg.** { *; }
-keep class com.inka.ncg2.** { *; }

#
#-keep public class *extends java.lang.annotation.Annotation {*;}
