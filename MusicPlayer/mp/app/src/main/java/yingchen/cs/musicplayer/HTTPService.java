package yingchen.cs.musicplayer;

import static com.firebase.client.utilities.HttpUtilities.HttpRequestType.GET;

/**
 * Created by yingchen on 10/31/16.
 */

public interface HTTPService {

    String BASE_URL = "https://plasma-system-145121.appspot.com/hello";
/*
    @GET("")
    Call<List<Repository>> publicRepositories(@Path("username") String username);


    class Factory {
        public static GithubService create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit.create(GithubService.class);
        }
    }
    */
}
