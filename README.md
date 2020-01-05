# Project Dashboard
*Native Apps I - Eindproject Android*  
**By**: Waut Wyffels (HoGent Aalst)

# About
The purpose of this app it to be able to create and track progress on all your projects.
Add team members and some details to a project (like owner and contact details).
Additionally you can add your tasks to a project and track how much time you've spent on them.

Are you done working on a task or want to reassign it to someone else? No problem, that can all be done from within the app!

# Debugging and deployment
Want to debug the full application and backend, just follow these steps:

1. Clone the git repo to a local folder (duh 😉)

## Android

1. Open the Android folder inside Android Studio
2. Select the appropriate [Build Variant](#build-variants)
3. Run or deubg the selected configuration

### Build Variants
The project includes 4 build variants, which can be viewed in the 'Build Variants' toolbar inside Android Studio.
They can also be edited in the **App** Gradle module.

The app module contains 2 custom Debug variables:

```gradle
buildConfigField 'String', 'BASE_URL'
buildConfigField 'Boolean', 'IsEmulator'
```

`BASE_URL` defines the base url for the network (api) service

`IsEmulator` is self-explanatory: It defines if the app is running on an emulator  
**Note**: This is also used to check whether or not to allow *ALL* network certificates

### deviceDebug
This variant is meant to be run on an actual device, but can also run on the emulator.

It defines the variables as follows:
- `BASE_URL: 'https://projectdashboard.azurewebsites.net/api/'`
- `IsEmulator: false`

### emulatorDebug
⚠ This variant should strictly only be run on an emulator!

It defines the variables as follows:
- `BASE_URL: 'https://10.0.2.2:5001/api/'`
- `IsEmulator: true`

As this variant defines the base url to be 'localhost', the app will try to access the api on your localhost.
Since the localhost on an emulator is the same as your development device, it can access the local api, which is very good for debugging puposes.
Running this variant on a physical device will result in the app not finding the api, thus not allowing a first time sign-in...

**Note**: Since the app expects the api to run on localhost, you should also be running the backend part on your device [see Backend](#backend)

### deviceRelease & emulatorRelease
These variants always run with the real api base url, but still set the `IsEmulator` variant

## Backend
This is the Backend or API part of the app and is only needed when running the *emulatorDebug* Android variant.

Here's how to set it up and run it locally:
1. Open the solution (or csproj) file in Visual Studio (Double-click should suffice if VS is installed)
2. Right-click the **project** and select 'Manage User Secrets'
3. Paste the following snippit inside the file:

```json
{
  "Jwt": {
    "Key": "GelukkigGeenNetbeans",
    "Issuer": "projectdashboard.localhost"
  }
}
```

4. Run the project with or without debugging

## Existing Data
For testing purposes, you can also use the following account to view existing data:

```
Username: demo.user@realdolmen.be
Password: DemoPassword
```

This account has some teams & projects pre-made, so no need to create them yourself :)

## Architectures
**Android**
- Kotlin, KTX & Gradle
- MVVM
- Databinding
- Activities & Fragments
- Values (for translation, etc...)
- Room & Dao for local storage
- SharedPreferences (private mode) for user auth storage
- Retrofit for api services

**Backend**
- ASP Core 2.2
- Controllers, Models & DTO's
- EF Core for DB storage
- Repositories for data retrieval

## 3rd-Party Libraries
**Android**
- AndroidX
  - Core
  - AppCompat
  - ConstraintLayout
  - LegacySupport
  - Navigation
  - Lifecycle + Databinding
- KotlinX Coroutines
- RetroFit2 + OkHttp3
- Moshi
- Aidan Follestad's [Libs](https://af.codes/)
  - Material Dialogs
  - VValidator
  - Material CAB
- OAuth Android JWT
- Henning Dodenhof's [CircleImageView](https://github.com/hdodenhof/CircleImageView)
- AirBnb Lottie Android
- PrettyTime Android
- JUnit & MockK for testing

**Backend**
- Pomelo EFCore MySQL
- NSwag