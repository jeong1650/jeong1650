package com.example.kaon.ims;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    String API_URL = "http://125.132.52.182:55551/";
//
//    String API_URL = "http://192.168.1.149:8080/";

//    String API_URL = "http://192.168.1.195:8080/";

//      String API_URL = "http://125.132.52.171:44441/";

    @FormUrlEncoded
    @POST("mobile/project_mobile")
    Call<ResponseBody> postData(@FieldMap HashMap<String, String> notice);

    @FormUrlEncoded
    @POST("mobile/applicant")
    Call<ResponseBody>postinfo(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("mobile/fileload")
    Call<ResponseBody>postresume(@FieldMap HashMap<String,Integer> file);

    @FormUrlEncoded
    @POST("mobile/questionlist")
    Call<ResponseBody>posteval(@FieldMap HashMap<String,Integer> eval);

    @FormUrlEncoded
    @POST("interview/signcheck")
    Call<ResponseBody>postcheck(@FieldMap HashMap<String,String> checkpswd);

    @FormUrlEncoded
    @POST("interview/eval")
    Call<ResponseBody>postComplete(@FieldMap HashMap<String,Object> complete);

    @FormUrlEncoded
    @POST("interview/test")
    Call<ResponseBody>Senddata(@FieldMap HashMap<String,Object> senddata);
    @FormUrlEncoded
    @POST("mobile/login")
    Call<ResponseBody>Login(@FieldMap HashMap<String,Object> Login);
    @FormUrlEncoded
    @POST("mobile/schedulecall_mobile")
    Call<ResponseBody>Schedule(@FieldMap HashMap<String, String> Schedule);

    @FormUrlEncoded
    @POST("mobile/resumelist")
    Call<ResponseBody>waitlist(@FieldMap HashMap<String ,String> wait);

    @FormUrlEncoded
    @POST("push/insertInfo")
    Call<ResponseBody>requestpush(@FieldMap HashMap<String ,String> push);
}
