<h1 align="center">Icecream API for Android</h1>

<p align="center">
    <a href="https://jitpack.io/#gumify/icecream"><img src="https://img.shields.io/jitpack/v/github/gumify/icecream?style=for-the-badge" alt="Release"></a>
    <a href="https://travis-ci.com/gumify/icecream"><img src="https://img.shields.io/travis/com/gumify/icecream/master?style=for-the-badge" alt="Build Status"></a>
    <a href="https://github.com/gumify/icecream/blob/master/LICENSE.txt"><img src="https://img.shields.io/github/license/gumify/icecream.svg?style=for-the-badge" alt="License"></a>
<!--     <img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/gumify/icecream?logo=GitHub&style=for-the-badge"> -->
    <img alt="GitHub repo size" src="https://img.shields.io/github/repo-size/gumify/icecream?logo=GitHub&style=for-the-badge">
    <a href="https://github.com/gumify/icecream/issues"><img alt="GitHub open issues" src="https://img.shields.io/github/issues/gumify/icecream?style=for-the-badge"></a>
</p>


### Getting Started

Add it in your root build.gradle at the end of repositories

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency

```gradle
implementation "com.github.gumify.icecream:$icecream_version"
```

Create an icecream instance

```kotlin
val icecream = icecream.getInstance()
```

### Using icecream to get wallpapers

Get all trending wallpapers

```kotlin
icecream.trendingWallpapers(page = 1) { wallpapers, error ->
    // Get HD image URL
    wallpapers[0].directUrl { url, e ->
        debug(url)
    }
}
```


Search wallpapers

```kotlin
icecream.searchWallpapers(query = "iron man", page = 1) { wallpapers, error ->
    debug(wallpapers)
}
```



### Using icecream to get ringtones

Get all trending ringtones

```kotlin
icecream.trendingRingtones(page = 1) { ringtones, error ->
    debug(ringtones)
}
```


Search ringtones

```kotlin
icecream.searchRingtones(query = "iron man", page = 1) { ringtones, error ->
    debug(ringtones)
}
```
