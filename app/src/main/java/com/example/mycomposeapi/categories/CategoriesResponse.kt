package com.example.mycomposeapi.categories

data class CategoriesResponse(
    val `data`: List<Data>
)
// Data class to represent the item data
data class Data(
    val cancel_charge: Any,
    val cancel_time_threshold: Any,
    val city_id: Any,
    val commission: Any,
    val dynamicPricings: List<Any>,
    val extra_percent: Any,
    val fare_base: String,
    val fare_minimum: String,
    val fare_per_km: String,
    val fare_per_minute: String,
    val for_disabled: Any,
    val id: Int,
    val image: String, // URL for the image
    val name: String,
    val reservation_percent: String,
    val ride_type: String,
    val seating_capacity: Int,
    val tax: Double,
    val vehicle_type: String,
    val waiting_fare: Double
)

