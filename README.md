# MobileDataUsageExercise
Android exercise, fetch open API data and show it in list and detail pages.

## Task
1. fetch data from [Singapore government open data](https://data.gov.sg/dataset/mobile-data-usage)
    * the mobile data usage data, url pattern is `https://data.gov.sg/api/action/datastore_search?resource_id=a807b7ab-6cad-4aa6-87d0-e283a7353a0f&limit=5&offset=6`
2. show every year mobile data usage volume in a list
    * the raw data is count by every quarter of year, like 2020-Q1
    * show a dropdown image to show that at least one quarter volume is less than the last quarter
3. (optional) use data cache

### Requirement
* well designed
* updated Android suite: kotlin, jetpack
* well tested support by test framework

## What will the app looks like
It will be two pages app, list and detail screen.   

* List page: show every year mobile data usage volume, can switch between normal style and card style.  
* Detail page: show one year usage, with the quarter data usage volume

### structure
![framework](https://github.com/freshleaf/MobileDataUsageExercise/raw/master/doc/framework.jpeg)

### source code package

* MainActivity
* .volumelist     : list page
* .volumedetails  : details page
* .data           : pojo define, fetch remote data and store in local sqlite
* .util           : constant define, tools etc

#### file tree
```
.
├── AndroidManifest.xml
├── java
│   └── com
│       └── yuman
│           └── anotherexercise
│               ├── MainActivity.kt
│               ├── ServiceLocator.kt
│               ├── VolumeApplication.kt
│               ├── data
│               │   ├── IVolumeRepository.kt
│               │   ├── QuarterVolumeItem.kt
│               │   ├── VolumeRepository.kt
│               │   ├── YearVolumeItem.kt
│               │   ├── local
│               │   │   ├── VolumeDao.kt
│               │   │   └── VolumeDatabase.kt
│               │   └── remote
│               │       ├── DataUsageResponse.kt
│               │       └── RemoteDataSource.kt
│               ├── util
│               │   ├── FetchDataStatus.kt
│               │   ├── StaticDefine.kt
│               │   └── VolleySingleton.kt
│               ├── volumedetails
│               │   └── VolumeDetailsFragment.kt
│               └── volumelist
│                   ├── VolumeListAdatper.kt
│                   ├── VolumeListFragment.kt
│                   └── VolumeListViewModel.kt
└── res
    ├── drawable
    │   ├── ic_launcher_background.xml
    │   └── ic_trending_down.xml
    ├── drawable-v24
    │   └── ic_launcher_foreground.xml
    ├── layout
    │   ├── activity_main.xml
    │   ├── fragment_volume_details.xml
    │   ├── fragment_volume_list.xml
    │   ├── switch_item.xml
    │   ├── volume_cell_card.xml
    │   └── volume_cell_normal.xml
    ├── menu
    │   └── main_menu.xml
    ├── mipmap-anydpi-v26
    │   ├── ic_launcher.xml
    │   └── ic_launcher_round.xml
    ├── mipmap-hdpi
    │   ├── ic_launcher.png
    │   └── ic_launcher_round.png
    ├── mipmap-mdpi
    │   ├── ic_launcher.png
    │   └── ic_launcher_round.png
    ├── mipmap-xhdpi
    │   ├── ic_launcher.png
    │   └── ic_launcher_round.png
    ├── mipmap-xxhdpi
    │   ├── ic_launcher.png
    │   └── ic_launcher_round.png
    ├── mipmap-xxxhdpi
    │   ├── ic_launcher.png
    │   └── ic_launcher_round.png
    ├── navigation
    │   └── nav_graph.xml
    └── values
        ├── colors.xml
        ├── strings.xml
        └── styles.xml
```

### Unit Test
Restruct the project to support unit test, such as constructor injection to add test double, service locator to bypass singleton creation both in release and test environment.

1. to run test in console  

```bash
./gradlew testDebugUnitTest --continue
./gradlew connectedDebugAndroidTest --continue
```
the report will be in folder: `./app/build/reports/`

2. to get test coverage report

```bash
./gradlew testDebugUnitTestCoverage
```

(need the device or emulator is connected)  
the report will be in folder: `./app/build/reports/`

### 3rd part library used
Compair with [Our Android 2020 development stack](https://blog.karumi.com/our-android-2020-development-stack/), which list the common but modern libraries.

#### Static analysis utilities & build tools
- [ ] Ktlint
- [ ] Android Lint
- [ ] Lin
- [ ] Ribbonizer
- [ ] Play publisher

> Not used stand-alone lint tools above, just used Android Studio integrated one： `./gradlew lint`

#### Networking
- [ ] Retrofit
- [ ] OkHTTP
- [x] Gson

> For the purpose of practice, chose using Google's Volley without OkHttp. Actually Retrofit+OkHttp is common choice for me.

#### Persistence
- [x] Room
- [ ] SQLDelight

#### Permissions
- [ ] Dexter

> No need in this project

#### User interface
- [x] Data Binding
- [x] Lifecycle Observer
- [x] Appcompat
- [x] ConstraintLayout
- [ ] Glide
- [ ] Renderers
- [ ] Calligraphy
- [ ] Material Components
- [ ] Snacky
- [ ] Lottie

> some of them are no need in this project

#### Core components and other useful libs
- [x] Kotlin
- [x] Coroutines
- [ ] Timber
- [ ] Libphonenumber
- [ ] OneSignal
- [ ] Kodein
- [ ] ThreeTenABP
- [ ] Crashlytics
- [ ] Arrow
- [ ] Leakcanary

> emmm, crash reporter, performance tools or even logger tool should be included

#### Testing
- [x] JUnit
- [ ] Shot
- [ ] Kotlin Snapshot
- [ ] Kotlin Test
- [x] Mockito
- [ ] MockWebServer
- [x] Robolectric
- [x] Espresso
- [ ] Barista
- [x] Jacoco
- [ ] Bitrise

#### Jetpack lib not mentioned
- [x] LiveData
- [x] Navigation
- [x] ViewModel
- [ ] WorkManager
- [ ] CameraX

## NOTE
When doing instrumentation tests in Android Studio, <strike>it may fail with message below</strike>

```
java.lang.UnsupportedOperationException: Failed to create a Robolectric sandbox: Android SDK 29 requires Java 9 (have Java 8)
```

one solution is to set runtime JRE environment to Java 9 in Android Studio:  
Run -> Edit Configurations... -> in popup window, select the Android JUnit test configuration, specify the JRE to new folder

Fixed by changing "targetSdkVersion 29" to "targetSdkVersion 28", it will avoid the problem above.  
The reason is it will change to use older Robolectric lib, and it will not throw out exception just a warning.  

## TODO
- [x] refine build script
- [x] test coverage report
- [ ] dev ops integration
- [ ] more test: server connection mock, integration test UI check, monkey test etc
- [ ] play with: paging, viewpage2 etc
- [ ] maybe: i18n, separate landscape view, controller style

#### what is done
* coding, the app is functionally workable
* unit test and simple function test

mainly did everything in Android Studio IDE environment, so tested in limited environment
