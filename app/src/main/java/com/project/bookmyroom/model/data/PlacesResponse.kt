package com.project.bookmyroom.model.data



data class PlacesResponse(val message: String, val data: List<Places>)


data class SearchRequest(val keyword: String)

data class Places(
    val _id: String,
    val description: String,
    val address: String,
    val lat: String,
    val nearByRailStops: String,
    val nearByBusStops: String,
    val districtId: String,
    val image: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int,
    val name: String,
    val id: String
)


data class District(val name: String, val id: Int)

fun getDistrictNameById( id: Int): String? {
    return districts.firstOrNull { it.id == id }?.name
}

// Usage
val districts = listOf(
    District("Thiruvananthapuram", 0),
    District("Kollam", 1),
    District("Pathanamthitta", 2),
    District("Alappuzha", 3),
    District("Kottayam", 4),
    District("Idukki", 5),
    District("Ernakulam", 6),
    District("Thrissur", 7),
    District("Palakkad", 8),
    District("Malappuram", 9),
    District("Kozhikode", 10),
    District("Wayanad", 11),
    District("Kannur", 12),
    District("Kasaragod", 13)
)

val districtId = 13
val districtName = getDistrictNameById( districtId)

