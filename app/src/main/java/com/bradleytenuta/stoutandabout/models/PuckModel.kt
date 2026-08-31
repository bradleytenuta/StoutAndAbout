package com.bradleytenuta.stoutandabout.models

/**
 * Defines 3D models available for the user's location puck.
 */
enum class PuckModel(
    val displayName: String,
    val previewImagePath: String,
    val uri: String,
    val scale: List<Float>,
    val rotation: List<Float>
) {
    BEER_BOTTLE(
        displayName = "Beer Bottle",
        previewImagePath = "beer_bottle.png",
        uri = "asset://beer_bottle.glb",
        scale = listOf(70f, 70f, 70f),
        rotation = listOf(0f, 0f, 0f)
    )
}
