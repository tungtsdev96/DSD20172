package com.hust.edu.dsd.api;

import com.hust.edu.dsd.model.Trees;
import com.hust.edu.dsd.model.WaterAndTree;
import com.hust.edu.dsd.model.WaterStation;
import com.hust.edu.dsd.model.staff.HistoryWork;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by tungts on 3/13/2018.
 */

public interface ApiHust {

    @FormUrlEncoded
    @POST("/api/staff/login")
    Call<ResponseBody> login(@Field("username") String user, @Field("password") String pass);

    @GET("/api/tree/get-all")
    Call<ArrayList<Trees>> getAllTrees();

    @GET("/api/tree-and-water")
    Call<WaterAndTree> getWaterAndTree(@Query("x") int x,@Query("y") int y,@Query("max") int max);

    @Multipart
    @POST("/api/mobile/update-tree-state")
    Call<ResponseBody> updateTreeState(@Part MultipartBody.Part file,
                                       @Part("tree_id") String tree_id,
                                       @Part("tree_state") String tree_state,
                                       @Part("tree_description") String tree_description);

    @GET("/api/water-station/get-all")
    Call<ArrayList<WaterStation>> getAllWaterStation();

    @Multipart
    @POST("/api/history-detail/create")
    Call<ResponseBody> waterTree(@Part("tree_id") int tree_id,
                     @Part("staff_id") int staff_id,
                     @Part("volume") double volume);

    @GET("/api/nearest-water-station")
    Call<WaterStation> getNearest(@Query("x") int x,
                                  @Query("y") int y);

    @GET("/api/history-detail/get")
    Call<ArrayList<HistoryWork>> getHistoryWork(@Query("staff_id") int staff_id);
}
