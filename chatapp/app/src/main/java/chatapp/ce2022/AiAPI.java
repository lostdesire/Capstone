package chatapp.ce2022;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface AiAPI {


    @GET("/{message}")
    Call<Meme> getMeme(@Path("message") String message);






};




