package com.example.android.baking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.baking.adapter.IngredientAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Ingredient extends AppCompatActivity {
    @BindView(R.id.rv_recipeIngredient)
    RecyclerView rv_recipeIngredient;
    int position;
    List<com.example.android.baking.model.Ingredient> ingredients = new ArrayList<>();
    IngredientAdapter ingredientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        ButterKnife.bind(this);
        position = getIntent().getIntExtra("position", 0);
        setTitle(MainActivity.postResponses.get(position).getName());
        ingredients = MainActivity.postResponses.get(position).getIngredients();
        ingredientAdapter = new IngredientAdapter(ingredients);
        rv_recipeIngredient.setAdapter(ingredientAdapter);
        rv_recipeIngredient.setHasFixedSize(true);
        rv_recipeIngredient.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}
