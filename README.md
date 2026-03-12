# MyWeatherKt

A weather application for Android — a Kotlin + Jetpack Compose + MVVM rewrite of [MyWeatherJv](https://github.com/YatsekTest/MyWeatherJv).

## Features

- **Current weather** — search by city name, displays temperature, description, min temperature, humidity, pressure, and wind speed.
- **Hourly forecast** — up to 40 forecast entries (3-hour intervals, 5-day window) from the OpenWeatherMap forecast API, showing time, temperature, weather condition, and wind speed.
- **Daily forecast** — 5-day forecast grouped by day, showing min/max temperatures, dominant weather condition, and average wind speed.
- **Location-based weather** — on first launch requests location permission and automatically loads weather for the device's current GPS coordinates.
- **Bottom navigation** — three tabs: current weather, hourly forecast, daily forecast.
- **City persistence** — the last searched city is saved and pre-filled on next launch (SharedPreferences).

## Tech stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| Architecture | MVVM (ViewModel + StateFlow) |
| UI | Jetpack Compose + Material 3 |
| Networking | Retrofit 2 + Gson converter |
| Async | Kotlin Coroutines |
| State | `StateFlow` / `collectAsState` |
| Persistence | SharedPreferences |

**Minimum dependencies** — no Hilt, no Room, no Navigation Compose beyond simple tab switching. Only what is necessary for the feature set.

## SDK

| Parameter | Value |
|---|---|
| `compileSdk` | 34 |
| `targetSdk` | 34 |
| `minSdk` | 21 (raised from 19 — Jetpack Compose requires API 21+) |

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/YatsekTest/MyWeatherKt.git
cd MyWeatherKt
```

### 2. Get an OpenWeatherMap API key

1. Register at [openweathermap.org](https://openweathermap.org/api).
2. Go to **API keys** in your account dashboard.
3. Copy your API key.

### 3. Create `local.properties`

In the project root (next to `settings.gradle.kts`) create a file called `local.properties`:

```properties
# Android SDK path — Android Studio usually sets this automatically
sdk.dir=/path/to/your/android/sdk

# Your OpenWeatherMap API key
WEATHER_API_KEY=your_api_key_here
```

> **Note:** `local.properties` is listed in `.gitignore` and will never be committed.

### 4. Build & run

Open the project in **Android Studio** (Hedgehog or newer recommended) and press **Run**, or build from the command line:

```bash
./gradlew assembleDebug
```

The APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

## Project structure

```
app/src/main/
├── java/yatsekk/example/com/myweatherkt/
│   ├── data/
│   │   ├── api/
│   │   │   ├── ApiService.kt          # Retrofit interface
│   │   │   ├── RetrofitClient.kt      # Retrofit singleton
│   │   │   ├── model/WeatherDto.kt    # Current weather response models
│   │   │   └── model/ForecastDto.kt   # Forecast response models
│   │   └── repository/
│   │       └── WeatherRepository.kt   # Data source abstraction
│   ├── ui/
│   │   ├── screen/
│   │   │   ├── CurrentWeatherScreen.kt
│   │   │   ├── HourlyWeatherScreen.kt
│   │   │   └── DailyWeatherScreen.kt
│   │   ├── theme/
│   │   │   ├── Color.kt
│   │   │   ├── Theme.kt
│   │   │   └── Type.kt
│   │   └── viewmodel/
│   │       └── WeatherViewModel.kt
│   ├── util/
│   │   └── CityPreferences.kt         # SharedPreferences wrapper
│   └── MainActivity.kt
└── res/
    ├── values/strings.xml
    └── values/themes.xml
```

## Original Java version

See [MyWeatherJv](https://github.com/YatsekTest/MyWeatherJv) for the original Java implementation this project is based on.
