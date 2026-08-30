# GeoJSON Loading and Global Data Store

This plan outlines the steps to load pub data from a GeoJSON file on app startup and store it in a globally accessible data store.

## Proposed Changes

### Data Layer

#### [NEW] [Pub.kt](file:///E:/Code/AndroidStudioProjects/StoutAndAbout/app/src/main/java/com/bradleytenuta/stoutandabout/data/Pub.kt)
Create a data class to represent a Pub. This will wrap a Mapbox `Feature` to provide type-safe access to common properties (like name) while retaining access to all GeoJSON properties.

#### [NEW] [PubDataStore.kt](file:///E:/Code/AndroidStudioProjects/StoutAndAbout/app/src/main/java/com/bradleytenuta/stoutandabout/data/PubDataStore.kt)
Implement a singleton object `PubDataStore` that will:
- Hold the list of `Pub` objects.
- Provide a method to initialize the store by reading `london-pubs.geojson` from assets.
- Use Mapbox's `FeatureCollection.fromJson()` for efficient parsing.

### Application Lifecycle

#### [NEW] [StoutAndAboutApplication.kt](file:///E:/Code/AndroidStudioProjects/StoutAndAbout/app/src/main/java/com/bradleytenuta/stoutandabout/StoutAndAboutApplication.kt)
Create a custom `Application` class to trigger the `PubDataStore` initialization as soon as the app starts.

#### [MODIFY] [AndroidManifest.xml](file:///E:/Code/AndroidStudioProjects/StoutAndAbout/app/src/main/AndroidManifest.xml)
Register the `StoutAndAboutApplication` class in the manifest.

## Verification Plan

### Automated Tests
- I will create a unit test `PubDataStoreTest` to verify that the GeoJSON is parsed correctly and the store contains the expected number of pubs.

### Manual Verification
- Log the number of pubs loaded during app startup to `Logcat`.
- Verify that the app still loads the map correctly.
