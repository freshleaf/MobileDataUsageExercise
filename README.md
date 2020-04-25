# MobileDataUsageExercise - `Working in Progress`
Android exercise, fetch open API data and show it in list and detail pages.

## Task
1. fetch data from [Singapore government open data](https://data.gov.sg/dataset/mobile-data-usage)
    * the mobile data useage data, url pattern is `https://data.gov.sg/api/action/datastore_search?resource_id=a807b7ab-6cad-4aa6-87d0-e283a7353a0f&limit=5&offset=6`
2. show every year mobile data usage volume in a list
    * the raw data is count by every quarter of year, like 2020-Q1
    * show a dropdown image to show that at least one quarter volume is less than the last quarter
3. (optional) use data cache

### Requirement
* well designed
* updated Android suite: kotlin, jetpack
* well tested using test framework

## What will the app looks like
It will be two pages app, list and detail screen.   
List page can switch style between normal and card style.

some components will be:

* Controller
    * SwipeRefreshLayout, ListAdapter
    * ViewModel, SavedStateHandle
    * Navigation
* Network
    * Volley
    * Gson
* Test

## Progress Trace
App works with basic function.

TO-DO list:

* add test code
* add function: persistent storage using Room
* refine network API definition
* add function: detail page scroll using viewpager2


