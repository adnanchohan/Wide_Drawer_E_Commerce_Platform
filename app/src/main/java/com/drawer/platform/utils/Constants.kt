package com.drawer.platform.utils

object Constants {
    const val PREF_NAME = "drawer_wide_prefs"
    const val KEY_USER_ID = "user_id"
    const val KEY_USER_MODE = "user_mode"
    const val KEY_USER_NAME = "user_name"
    const val KEY_USER_EMAIL = "user_email"
    const val KEY_IS_LOGGED_IN = "is_logged_in"

    const val EXTRA_MODE = "extra_mode"
    const val EXTRA_PRODUCT_ID = "extra_product_id"
    const val EXTRA_STORE_ID = "extra_store_id"

    const val MODE_SELLER = "SELLER"
    const val MODE_BUYER = "BUYER"
    const val MODE_DELIVER = "DELIVER"

    const val IMAGES_DIR = "product_images"
    const val VIDEOS_DIR = "product_videos"
    const val PROFILE_IMAGES_DIR = "profile_images"
    const val STORE_BANNERS_DIR = "store_banners"

    val CATEGORIES = listOf(
        "All", "Electronics", "Fashion", "Food & Kitchen",
        "Home & Living", "Sports", "Beauty", "Books", "Toys", "Automotive", "Other"
    )

    const val AUTHORITY = "com.drawer.platform.provider"
}
