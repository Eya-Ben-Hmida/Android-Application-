package com.example.isi.Service;

import com.example.isi.Models.ListProcess;
import com.example.isi.Models.LoginResponse;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    @GET("user")
    Call<List<LoginResponse>> loginUser(@Header("Authorization") String auth);

    @GET("process-definition?latest=true&active=true&startableInTasklist=true&startablePermissionCheck=true&firstResult=0&maxResults=15")
    Call<List<ListProcess>> processUser(@Header("Authorization") String auth);

    @GET("process-definition/count")
    Call<Object> processcountUser(@Header("Authorization") String auth);
    @GET("task/count")
    Call<Object> taskcountUser(@Header("Authorization") String auth);

    @GET("task/count?assignee={id}")
    Call<Object> assignedtaskcount(@Header("Authorization") String auth);

    @GET("process-definition/{id}/form-variables")
    Call<Object> formUser(@Header("Authorization") String auth,@Path("id") String id);

    @GET("task/{id}/rendered-form?noCache=1652411310734&userId=agent&engineName=default")
    Call<Object> formTask(@Header("Authorization") String auth,@Path("id") String id,@Query("taskId") String id1);

    @POST("process-definition/{id}/submit-form")
    Call<Object> submitForm(@Header("Authorization") String auth,@Path("id") String id,@Body RequestBody noms);

    @GET("task?latest=true&active=true&startableInTasklist=true&startablePermissionCheck=true&firstResult=0")
    Call<List<ListProcess>> AssignedTasks(@Header("Authorization") String auth, @Query("assignee") String id);

    @GET("task?latest=true&active=true&startableInTasklist=true&startablePermissionCheck=true&firstResult=0")
    Call<List<ListProcess>> GroupAssignedTasks(@Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("task/{id}/claim")
    Call<Object> TaskClaim(@Header("Authorization") String auth,@Path("id") String id);
}