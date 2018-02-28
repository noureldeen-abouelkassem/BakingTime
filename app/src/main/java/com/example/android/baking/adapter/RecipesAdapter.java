package com.example.android.baking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.baking.R;
import com.example.android.baking.model.PostResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeItemHolder> {
    private final List<PostResponse> postResponses;
    private Context context;
    private final RecipeClick recipeClick;

    public interface RecipeClick {
        void OnClick(int position);
    }

    public RecipesAdapter(List<PostResponse> postResponses, RecipeClick recipeClick, Context context) {
        this.postResponses = postResponses;
        this.recipeClick = recipeClick;
        this.context = context;
    }

    @Override
    public RecipeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecipeItemHolder holder, final int position) {
        if (holder != null) {
            holder.bindDataToRecipeHolder(postResponses.get(position).getImage(), postResponses.get(position).getName());
            holder.civ_recipeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recipeClick.OnClick(holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return postResponses.size();
    }

    public class RecipeItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.civ_recipeImage)
        ImageView civ_recipeImage;
        @BindView(R.id.tv_recipeName)
        TextView tv_recipeName;

        public RecipeItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindDataToRecipeHolder(String imageUrl, String recipeName) {
            if (!(imageUrl.isEmpty()) && !(imageUrl.equals("")))
                Glide.with(context).setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.vp_placeholder).error(R.drawable.vp_placeholder)).load(imageUrl).into(civ_recipeImage);
            tv_recipeName.setText(recipeName);
        }
    }
}
