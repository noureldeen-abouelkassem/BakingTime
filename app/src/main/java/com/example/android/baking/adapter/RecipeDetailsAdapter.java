package com.example.android.baking.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.baking.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsAdapter extends RecyclerView.Adapter<RecipeDetailsAdapter.RecipeItemHolder> {
    private final List<String> strings;
    private final StepClick stepClick;

    public interface StepClick {
        void OnClick(int position);
    }

    public RecipeDetailsAdapter(List<String> strings, StepClick stepClick) {
        this.strings = strings;
        this.stepClick = stepClick;
    }

    @Override
    public RecipeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_details_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecipeItemHolder holder, final int position) {
        if (holder != null) {
            holder.bindDataToRecipeStepHolder(strings.get(position));
            holder.tv_stepName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stepClick.OnClick(holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class RecipeItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_stepName)
        TextView tv_stepName;

        public RecipeItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindDataToRecipeStepHolder(String stepName) {
            tv_stepName.setText(stepName);
        }
    }
}
