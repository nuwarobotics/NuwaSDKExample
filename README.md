# `NuwaSDKExample`
The SDK Example to control robot product of nuwarobotics.

NuwaSDK and NuwaSDKExample allow use on every language\market.

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
    implementation(name: 'NuwaSDK-2020-06-11_v2.1.0.01.d0e7018', ext: 'aar')
}
```

# `NuwaSDK example`
* Robot Motion Control
    - Query Motion List Example
    - Motion Play Example
    - Motion Play/Pause/Resume Control Example
    - Motion Play with window view control Example
* Robot Motor Control
    - Motor control Example
    - Movement control Example
* Other Sensor Example
    - LED Control Example
    - Sensor Detect Example
* ASR/TTS
    - Wakeup Example
    - Local command Example
    - Cloud ASR Example
    - Local command and Cloud ASR Example
    - TTS Example
* System Control
    - disablePowerKey
* Advanced
    - Motion with TTS Example


