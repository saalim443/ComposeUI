package com.example.mycomposeapi.login


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    val data: UserData
)

data class UserData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("profile_img")
    val profileImg: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("email_verified_at")
    val emailVerifiedAt: String,
    @SerializedName("mobile_verified_at")
    val mobileVerifiedAt: String?,
    @SerializedName("mobile")
    val mobile: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("verified")
    val verified: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("registered_vehicle_count")
    val registeredVehicleCount: Int,
    @SerializedName("active_vehicle_count")
    val activeVehicleCount: Int,
    @SerializedName("active_vehicle_doc_count")
    val activeVehicleDocCount: Int?,
    @SerializedName("country_flag")
    val countryFlag: String,
    @SerializedName("country_code")
    val countryCode: String,
    @SerializedName("ride_completed")
    val rideCompleted: Int,
    @SerializedName("language")
    val language: String?,
    @SerializedName("online")
    val online: Int,
    @SerializedName("previousCancelledRide")
    val previousCancelledRide: List<Any>, // Empty array in JSON, can be a list of any type
    @SerializedName("detail")
    val detail: UserDetail,
    @SerializedName("activeVehicle")
    val activeVehicle: Any? // `null` in JSON, can be of any type or specific type if known
)

data class UserDetail(
    @SerializedName("id")
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("birth_date")
    val birthDate: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("fcm_token")
    val fcmToken: String?,
    @SerializedName("user_type")
    val userType: String,
    @SerializedName("company_name")
    val companyName: String?,
    @SerializedName("google_id")
    val googleId: String?,
    @SerializedName("facebook_id")
    val facebookId: String?,
    @SerializedName("apple_id")
    val appleId: String?
)
