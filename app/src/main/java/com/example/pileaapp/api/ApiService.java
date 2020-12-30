package com.example.pileaapp.api;

import com.example.pileaapp.api.models.Category;
import com.example.pileaapp.api.models.Location;
import com.example.pileaapp.api.models.Login;
import com.example.pileaapp.api.models.Register;

import java.util.List;

import io.reactivex.Single;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
//    @Headers({ "Content-Type: application/json;charset=UTF-8"})
//    @GET("Category/{category_id}")
//    Single<Category> getCategoryData(@Path("category_id") int categoryID,
//                                     @Header("Authorization") String authToken,
//                                     @Header("ApiKey") String apiKey);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("Category")
    Single<List<Category>> getUserCategories(@Header("Authorization") String token,
                                 @Header("ApiKey") String apiKey,
                                 @Query("userId") String userId);


    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("Category")
    Single<Category> createCategory(@Header("Authorization") String token,
                                    @Header("ApiKey") String apiKey,
                                    @Query("userId") String userId,
                                    @Body Category categoryBody);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("Authenticate/register")
    Single<Register> register(@Header("ApiKey") String apiKey,
                                    @Body Register registerBody);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("Authenticate/login")
    Single<Login> register(@Header("ApiKey") String apiKey,
                           @Body Login loginBody);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("Location")
    Single<List<Location>> getUserLocations(@Header("Authorization") String token,
                                             @Header("ApiKey") String apiKey,
                                             @Query("userId") String userId);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("Location")
    Single<Location> createLocation(@Header("Authorization") String token,
                                    @Header("ApiKey") String apiKey,
                                    @Query("userId") String userId,
                                    @Body Location locationBody);
}
