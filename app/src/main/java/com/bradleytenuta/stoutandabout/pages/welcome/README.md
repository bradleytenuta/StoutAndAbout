# Welcome Section

The Welcome section handles the initial user onboarding flow for Stout & About. It is designed to ensure the user has granted the necessary location permissions and has selected their character before entering the main map experience.

## Components

### [WelcomeScreen.kt](file:///E:/Code/AndroidStudioProjects/StoutAndAbout/app/src/main/java/com/bradleytenuta/stoutandabout/pages/welcome/WelcomeScreen.kt)
The main coordinator for the onboarding flow. It uses a `HorizontalPager` to transition between the permission request and character selection.
- **Auto-advance**: Clicking the permission button on the first page automatically scrolls the user to the second page.
- **Validation**: If a user attempts to "Start Exploring" without having granted location permissions, the screen automatically scrolls them back to the first page.
- **Theming**: Forces the `RubberHoseParchment` background and `RubberHoseBlack` text to maintain brand consistency and readability.

### [LocationPermissionPage.kt](file:///E:/Code/AndroidStudioProjects/StoutAndAbout/app/src/main/java/com/bradleytenuta/stoutandabout/pages/welcome/LocationPermissionPage.kt)
The first page of the onboarding flow.
- Explains why the app needs location access.
- Provides a button to trigger the system permission dialog.
- **Visual Feedback**: Once permission is granted, the button text is replaced by a green checkmark icon.
- **Fallback Guidance**: Displays an italicized message advising the user to check settings if the system popup doesn't appear.

### [CharacterSelectionPage.kt](file:///E:/Code/AndroidStudioProjects/StoutAndAbout/app/src/main/java/com/bradleytenuta/stoutandabout/pages/welcome/CharacterSelectionPage.kt)
The second page of the onboarding flow.
- Displays a list of available `PuckModel` options.
- **Asset Loading**: Previews are loaded dynamically from the `assets` folder as PNG files.
- **Interaction**: Row opacity is reduced to 0.6 for unselected items and increased to 1.0 for the selected character.
- **State Enforcement**: The "Start Exploring" button remains disabled until a character selection is made.

## Logic Flow

1. **Boot Check**: `MainActivity` checks for location permissions on startup. If already granted, it skips this section entirely (`isWelcomeCompleted = true`).
2. **Permission Request**: New users land on the `LocationPermissionPage`.
3. **Character Choice**: Users swipe or are auto-scrolled to the `CharacterSelectionPage`.
4. **Completion**: Upon clicking "Start Exploring" with a selection and permission, the app transitions to the `MapScreen`.
