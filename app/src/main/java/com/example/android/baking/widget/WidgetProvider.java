package com.example.android.baking.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.android.baking.R;


public class WidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list);

        SharedPreferences sharedPreferences =
                context.getSharedPreferences("com.example.android.baking", Context.MODE_PRIVATE);
        String recipeName= sharedPreferences.
                getString(context.getString(R.string.pref_recipe_name),"");

        String formattedIngredients = sharedPreferences.
                getString(context.getString(R.string.pref_recipe_ingredients),"");

        views.setTextViewText(R.id.widget_recipe_name,recipeName);
        views.setTextViewText(R.id.widget_recipe_ingredients,formattedIngredients );


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context,appWidgetManager,appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context,appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

