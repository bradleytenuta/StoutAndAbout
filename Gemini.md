# Stout & About - Project Overview

Stout & About is a Kotlin-based Android application that uses Mapbox to create a location-aware experience for discovering and tracking visits to pubs, similar to the mechanics of Pokémon GO.

## Core Concept
The app provides a real-time, 3D map interface that tracks the user's GPS location. Pubs are represented as points of interest on the map. When a user physically enters a pub's location, the app automatically records the visit.

## Key Features
- **Immersive Map UI:** A full-screen Mapbox map is the primary interface.
- **Third-Person Camera:** The camera follows the user in a 3rd person perspective, always positioned behind the user's avatar.
- **Restricted Navigation:** Users cannot manually pan or zoom the map with touch gestures; movement is driven strictly by the user's real-world GPS coordinates.
- **Custom User Representation:** Instead of a standard location pin, the app uses a 3D model to represent the user on the map.
- **Pub Locations:** Pub boundaries are defined in a GeoJSON file and rendered as polygons on the Mapbox map to visualize pub areas.
- **Automatic Visit Detection:** The app utilizes precise GPS coordinates to detect when a user's location overlaps with a pub's coordinates.

## Technical Stack
- **Language:** Kotlin (v2.2.10)
- **UI Framework:** Jetpack Compose (BOM v2026.02.01)
- **Map Engine:** Mapbox Maps SDK for Android (v11.29.0)
- **Android Version:** API 35 (Android 15)
- **Build System:** Gradle with AGP v9.3.1
- **Location Services:** High-accuracy GPS tracking
- **3D Rendering:** Mapbox 3D model support

## Documentation
- **Mapbox Android SDK Guides:** https://docs.mapbox.com/android/maps/guides/

## Key Library Versions
- `androidx-core-ktx`: 1.10.1
- `androidx-lifecycle-runtime-ktx`: 2.6.1
- `androidx-activity-compose`: 1.8.0
- `mapbox-maps`: 11.29.0

## Project Structure
- `app/`: Main Android application module.
- `MainActivity.kt`: Entry point displaying the full-screen map.
- `ui/theme/`: Compose theme and styling.

## Mascot
The app's mascot is **Mr Stout**, a personified pint of stout. He is represented in the following assets:
- `ic_launcher.webp`: The main application icon.
- `mr_stout_talking.png`: A character asset used for in-app notifications and speech bubble toasts.

## Context for AI Agent
When assisting with this project, prioritize:
1. **Official Documentation:** Always refer to and follow the [Mapbox Android SDK Guides](https://docs.mapbox.com/android/maps/guides/) and all its sub-pages as the primary source of truth for implementation.
2. **Thematic Consistency:** Always use the custom theme and colors defined in [Color.kt](file:///E:/Code/AndroidStudioProjects/StoutAndAbout/app/src/main/java/com/bradleytenuta/stoutandabout/ui/theme/Color.kt) (e.g., `RubberHoseBlack`, `RubberHoseWhite`, `RubberHoseParchment`) when creating or updating UI components to maintain the "Rubber Hose" aesthetic.
3. **Mapbox Integration:** Focus on 3D model rendering, camera positioning (follow mode), and drawing custom markers.
4. **GeoJSON Integration:** Rendering pub boundaries using GeoJSON data to draw polygons and managing these layers within the Mapbox map.
5. **Location Logic:** High-accuracy location updates and geofencing logic for pub entry detection based on the defined polygon areas.
6. **Compose Integration:** How Mapbox's `MapView` is hosted and managed within a Jetpack Compose `Scaffold`.
7. **Interaction Constraints:** Ensuring manual map gestures are disabled to maintain the "follow" experience.
