# Instructions to generate an AAR using Android Studio

1. Open "Gradle Projects" view in Android Studio.
1. Open mctl-sample > mcontrol > Tasks > build.  Double click "build".
1. The `.aar` file should be under `mcontrol/build/outputs/aar`.

# Instructions on how to import the AAR

1. File > New > New Module
1. Click "Import .JAR/.AAR Package".
1. Follow through the instructions.
1. File > Project Structure > app > Dependencies > green plus on the right > click "Module Dependency" > click the dependency you want to import.