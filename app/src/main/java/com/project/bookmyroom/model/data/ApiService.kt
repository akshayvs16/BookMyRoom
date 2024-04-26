package com.project.bookmyroom.model.data


import com.project.bookmyroom.viewmodel.BookedRoomsResponse
import com.project.bookmyroom.viewmodel.BookingItem
import com.project.bookmyroom.viewmodel.MyBookingUserRequest
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("user/register")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("user/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @POST("places/list")
    fun getPlacesByDistrict(@Body request: DistrictRequestforPlaces): Call<PlacesResponse>

    @POST("hotels/list")
    fun getHotelsByDistrict(@Body request: DistrictRequest): Call<HotelResponse>
    @POST("hotels/list")
    fun getHotelsByPlaces(@Body request: DistrictRequest): Call<HotelResponse>

    @POST("rooms/register")
    fun saveUserPayment(@Body request: PaymentRequest): Call<PaymentResponse>


    @POST("rooms/list")
    fun getBookedRooms(@Body request: MyBookingUserRequest): Call<BookedRoomsResponse>

}

