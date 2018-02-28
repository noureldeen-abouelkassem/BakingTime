package com.example.android.baking;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.baking.adapter.IngredientAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IngredientFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ingredient_fragment,container,false);;
        ButterKnife.bind(this,view);
        populateUI(view);
        return view;
    }
    @BindView(R.id.rv_recipeIngredient)
    RecyclerView rv_recipeIngredient;
    int position;
    List<com.example.android.baking.model.Ingredient> ingredients = new ArrayList<>();
    IngredientAdapter ingredientAdapter;

    public void populateUI( View view){
        position = getActivity().getIntent().getIntExtra("position", 0);
        ingredients = MainActivity.postResponses.get(position).getIngredients();
        getActivity().setTitle(MainActivity.postResponses.get(position).getName());
        ingredientAdapter = new IngredientAdapter(ingredients);
        rv_recipeIngredient.setAdapter(ingredientAdapter);
        rv_recipeIngredient.setHasFixedSize(true);
        rv_recipeIngredient.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
    }
}
