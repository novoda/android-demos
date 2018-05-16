package com.novoda.demo.movies.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Single;

public interface MoviesApi {

    String API_KEY = "ENTER_YOUR_API_KEY";

    @GET("movie/top_rated?api_key="+API_KEY)
    Call<MoviesResponse> topRated(@Query("page") int page);

    @GET("movie/{id}/videos?api_key="+API_KEY)
    Single<VideosResponse> videos(@Path("id") String movieId);
}
