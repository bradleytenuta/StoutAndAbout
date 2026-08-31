package com.bradleytenuta.stoutandabout.domain

/**
 * Defines branding options for pubs.
 */
enum class Branding(
    val brandName: String,
    val iconPath: String
) {
    GREENE_KING(
        brandName = "Greene King",
        iconPath = "branding/greene-king.png"
    )
}
