package com.example.android.baking.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.baking.R;
import com.example.android.baking.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientItemHolder> {
    private final List<Ingredient> ingredients;

    public IngredientAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public IngredientItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IngredientItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_ingredient_item, parent, false));
    }

    @Override
    public void onBindViewHolder(IngredientItemHolder holder, int position) {
        if (holder != null) {
            holder.bindDataToRecipeStepHolder(ingredients.get(position).getIngredient(), ingredients.get(position).getMeasure(), ingredients.get(position).getQuantity());
        }
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class IngredientItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ingredientName)
        TextView tv_ingredientName;
        @BindView(R.id.tv_ingredientMeasure)
        TextView tv_ingredientMeasure;
        @BindView(R.id.tv_ingredientQuantity)
        TextView tv_ingredientQuantity;

        public IngredientItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindDataToRecipeStepHolder(String ingredientName, String ingredientMeasure, Float ingredientQuantity) {
            tv_ingredientName.setText(ingredientName);
            tv_ingredientMeasure.setText(ingredientMeasure);
            tv_ingredientQuantity.setText(ingredientQuantity.toString());
        }
    }
}
