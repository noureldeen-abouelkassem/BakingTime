package com.example.android.baking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.android.baking.adapter.RecipesAdapter;
import com.example.android.baking.apiCalls.APICalls;
import com.example.android.baking.model.PostResponse;
import com.example.android.baking.model.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.rv_recipes)
    RecyclerView rv_recipes;
    public static List<PostResponse> postResponses = new ArrayList<>();
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        RetrofitClient retrofitClient = new RetrofitClient();
        APICalls apiCalls = retrofitClient.getRetrofit().create(APICalls.class);
        Observable<List<PostResponse>> postResponseObservable = apiCalls
                .getRecipes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
        postResponseObservable.subscribe(new Observer<List<PostResponse>>() {

            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(List<PostResponse> postResponse) {
                postResponses.addAll(postResponse);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                intializeRecipe(postResponses);
            }
        });
    }

    private void intializeRecipe(List<PostResponse> postResponses) {
        RecipesAdapter recipesAdapter = new RecipesAdapter(postResponses, new RecipesAdapter.RecipeClick() {
            @Override
            public void OnClick(int position) {
                Intent intent = new Intent(MainActivity.this, RecipeDetails.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        }, MainActivity.this);
        rv_recipes.setHasFixedSize(true);
        if (getResources().getBoolean(R.bool.isTablet)) {
            rv_recipes.setLayoutManager(gridLayoutManager);
        } else {
            rv_recipes.setLayoutManager(linearLayoutManager);
        }
        rv_recipes.setAdapter(recipesAdapter);
    }
}
