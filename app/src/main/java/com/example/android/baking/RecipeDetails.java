package com.example.android.baking;

import android.app.FragmentTransaction;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.baking.adapter.RecipeDetailsAdapter;
import com.example.android.baking.model.PostResponse;
import com.example.android.baking.model.Step;
import com.example.android.baking.provider.Contract;
import com.example.android.baking.provider.Contract.Entry;
import com.example.android.baking.uitils.CommonUtils;
import com.example.android.baking.widget.WidgetProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetails extends AppCompatActivity {
    @BindView(R.id.rv_selectedRecipe)
    RecyclerView rv_selectedRecipe;
    List<String> strings = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    PostResponse postResponse;
    int recipePosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("com.example.android.baking", Context.MODE_PRIVATE);
        recipePosition = getIntent().getIntExtra("position", 0);
        postResponse = MainActivity.postResponses.get(recipePosition);
        setTitle(postResponse.getName());
        strings.add(String.format("%s Ingredients", postResponse.getName()));
        List<Step> steps = postResponse.getSteps();
        for (int i = 0; i < steps.size(); i++) {
            strings.add(steps.get(i).getShortDescription());
        }
        rv_selectedRecipe.setAdapter(new RecipeDetailsAdapter(strings, new RecipeDetailsAdapter.StepClick() {
            @Override
            public void OnClick(int position) {
                if (position == 0 && !(getResources().getBoolean(R.bool.isTablet))) {
                    Intent intent = new Intent(RecipeDetails.this, Ingredient.class);
                    intent.putExtra("position", recipePosition);
                    startActivity(intent);
                } else if (position != 0 && !(getResources().getBoolean(R.bool.isTablet))) {
                    Intent intent = new Intent(RecipeDetails.this, Recipe.class);
                    intent.putExtra("position", position - 1);
                    intent.putExtra("recipePosition", recipePosition);
                    startActivity(intent);
                } else if (position == 0 && (getResources().getBoolean(R.bool.isTablet))) {
                    IngredientFragment ingredientFragment = new IngredientFragment();
                    getFragmentManager().beginTransaction().replace(R.id.frg_container, ingredientFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                } else if (position != 0 && (getResources().getBoolean(R.bool.isTablet))) {
                    RecipeFragment recipeFragment = new RecipeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("step_position", position - 1);
                    recipeFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.frg_container, recipeFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                }

            }
        }));
        rv_selectedRecipe.setHasFixedSize(true);
        rv_selectedRecipe.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.selected_recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_addToWIdget) {
            item.setVisible(false);
            showIngredientsInWidget(recipePosition);
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return true;
    }

    private void showIngredientsInWidget(int adapterPosition) {

        List<com.example.android.baking.model.Ingredient> ingredients = MainActivity.postResponses.get(adapterPosition).getIngredients();

        String recipeName = MainActivity.postResponses.get(adapterPosition).getName();
        sharedPreferences.edit()
                .putString(getString(R.string.pref_recipe_name), recipeName)
                .apply();

        saveIngredients(ingredients);

        Uri uri = Contract.Entry.CONTENT_URI;
        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null);

        if (cursor != null) {

            //Delete the existing data first
            while (cursor.moveToNext()) {
                getContentResolver().delete(uri,
                        Entry._ID + "=?",
                        new String[]{
                                cursor.getString(cursor.getColumnIndex(Entry._ID))}
                );
            }

            cursor.close();

            ContentValues[] values = new ContentValues[ingredients.size()];

            for (int i = 0; i < ingredients.size(); i++) {

                values[i] = new ContentValues();

                values[i].put(Entry.COLUMN_NAME_QUANTITY, ingredients.get(i).getQuantity());
                values[i].put(Entry.COLUMN_NAME_MEASURE, ingredients.get(i).getMeasure());
                values[i].put(Entry.COLUMN_NAME_INGREDIENTS, ingredients.get(i).getIngredient());
            }
            getContentResolver().bulkInsert(uri, values);
        }

        //Update recipe's name and ingredients
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(),
                        WidgetProvider.class));

        WidgetProvider ingredientWidget = new WidgetProvider();
        ingredientWidget.onUpdate(this, AppWidgetManager.getInstance(this), ids);
    }

    private void saveIngredients(List<com.example.android.baking.model.Ingredient> ingredients) {

        String noOfIngredient = ingredients.size() + " Ingredients";

        StringBuilder sb = new StringBuilder();
        sb.append(noOfIngredient);

        for (com.example.android.baking.model.Ingredient ingredient : ingredients) {

            String name = ingredient.getIngredient();
            double quantity = ingredient.getQuantity();
            String measure = ingredient.getMeasure();

            sb.append("\n");
            sb.append(CommonUtils.formatIngredient(this, name, quantity, measure));
        }

        String formattedIngredients = String.valueOf(CommonUtils.setTextWithSpan(sb.toString(), noOfIngredient,
                new StyleSpan(Typeface.BOLD)));

        sharedPreferences.edit()
                .putString(getString(R.string.pref_recipe_ingredients), formattedIngredients)
                .apply();

    }

}
