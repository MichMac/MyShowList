package com.example.myshowlist;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterfaceMultiSearch {

    @GET("/3/search/multi")
    Call<MultiSearchResults> getMultiSearchResult(
            @Query("api_key") String apikey,
            @Query("language") String language,
            @Query("query") String query,
            @Query("page") int page,
            @Query("include_adult") boolean include_adult
    );



}
