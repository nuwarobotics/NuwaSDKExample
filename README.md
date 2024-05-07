# `NuwaSDKExample`
The SDK Example to control robot product of nuwarobotics.

NuwaSDK and NuwaSDKExample allow use on every language\market.

# `Develop environment of sample code`
* Android Studio : android-studio-ide-201.6858069
* Android Minimum SDK : API 28 - Android 9.0(Pie)
* Language : Java
* Project setting : check Use legacy android.support library

# `Nuwa SDK`
Newest Nuwa SDK：2.1.0.08

# `Support Robot Product`
Robot Generation 1 
* Kebbi(凱比) : Taiwan
* Danny(小丹) : China

Robot Generation 2
* Kebbi Air : Taiwan、China、Japan

# `TTS Capability`
The TTS language capability is difference between each market robot software.

Please reference following support list.
* Taiwan Market : Locale.CHINESE\Locale.ENGLISH
* Chinese Market : Locale.CHINESE\Locale.ENGLISH
* Japan Market : Locale.JAPANESE\Locale.CHINESE\Locale.ENGLISH
* Worldwide Market : Locale.ENGLISH

# `Nuwa Website`
* NuwaRobotics Website (https://www.nuwarobotics.com/)
* NuwaRobotics Developer Website (https://dss.nuwarobotics.com/)
* Nuwa SDK JavaDoc (https://developer-docs.nuwarobotics.com/sdk/javadoc/reference/packages.html)
* Nuwa Public Motion Preview (https://developer-docs.nuwarobotics.com/sdk/kebbi_motion_preview/showPic.html)

# `Start to Use`

* Please get NuwaSDK aar from [developer website](https://dss.nuwarobotics.com/) and modify app [build.gradle](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/build.gradle)
    
```
dependencies {
    //NOTICE : Please declare filetree if you create your own Android Project
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    //TODO : Please download newest NuwaSDK from Nuwa Developer Website https://dss.nuwarobotics.com/
    //Step 1 : Copy aar to project lib folder : NuwaSDKExample\app\libs
    //Step 2 : Replace below NuwaSDK file name
    implementation(name: 'NuwaSDK-2021-07-08_1058_2.1.0.08_e21fe7', ext: 'aar')
    //Please also include relative aar
    implementation "com.google.code.gson:gson:2.3.1"
}
repositories {
    flatDir {
        dirs 'libs'
    }
}
```

# `NuwaSDK example`
* Robot Motion Control
    - Query Motion List Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/motion/demo/QueryMotionActivity.java)
    - Motion Play Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/motion/demo/PlayMotionActivity.java)
    - Motion Play/Pause/Resume Control Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/motion/demo/ControlMotionActivity.java)
    - Motion Play with window view control Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/motion/demo/WindowControlWithMotionActivity.java)
* Robot Motor Control
    - Motor control Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/motor/MotorControlActivity.java)
    - Movement control Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/motor/MovementControlActivity.java)
* Other Sensor Example
    - LED Control Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/led/LEDExampleActivity.java)
    - Sensor Detect Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/sensor/SensorExampleActivity.java)
* ASR/TTS
    - Wakeup Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/voice/WakeupActivity.java)
    - Local command Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/voice/LocalcmdActivity.java)
    - Cloud ASR Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/voice/CloudASRActivity.java)
    - Local command and Cloud ASR Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/voice/LocalcmdAndCloudASRActivity.java)
    - TTS Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/voice/TTSActivity.java)
* Face Control [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/activity/FaceControlExampleActivity.java) 
    - Face Show/Hide Example
    - Face touch event callback example
* System Control
    - disablePowerKey [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/activity/DisablePowerkeyExampleActivity.java)
* Advanced
    - Motion with TTS Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/motion/MotionTtsExampleActivity.java)
    - Launch Nuwa add family member [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/activity/startNuwaFaceRecognitionActivity.java)
    - Launch Nuwa Video call, and call specific member [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/activity/VideoCall.java)
    

# `Q & A`
Q : How to solve 「Unable to find method 'org.gradle.api.artifacts.result.ComponentSelectionReason.getDescription()Ljava/lang/String;'」problem ?

A : Please try to modify following configuration
* NuwaSDKExample/gradle/wrapper/gradle-wrapper.properties
    - distributionUrl=https\://services.gradle.org/distributions/gradle-6.8.3-bin.zip
* NuwaSDKExample/build.gradle
    - classpath 'com.android.tools.build:gradle:4.0.1'

