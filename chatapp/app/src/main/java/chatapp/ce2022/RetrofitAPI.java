package chatapp.ce2022;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public interface RetrofitAPI{


    @POST("/auth/login")
    Call<Login> login(@Body Login login);

    @FormUrlEncoded
    @POST("/auth/signin")
    Call<Post> postData(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @PUT("/user/{uid}")
    Call<UserInfo> changeData(@Header("Authorization") String token,
                              @Path("uid") String uid,
                              @Field("name") String name,
                              @Field("msg") String msg,
                              @Field("img") String img

    );


    @GET("/user/{uid}")
    Call<UserInfo> getData(@Header("Authorization") String token, @Path("uid") String uid );



};




