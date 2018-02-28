package com.example.android.baking.apiCalls;

import com.example.android.baking.model.PostResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface APICalls {
    @GET("/topher/2017/May/59121517_baking/baking.json")
    Observable<List<PostResponse>> getRecipes();
}
