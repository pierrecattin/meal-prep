package com.pierrecattin.mealprep;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

public class MealplanActivity extends AppCompatActivity {

    private TextView textViewMealPlan;
    private IngredientViewModel mIngredientViewModel;
    private List<Ingredient> ingredients;
    private MealPlan plan;
    private ShareActionProvider shareActionProvider;
    public static final String EXTRA_NB_MEALS = "nb_meals"; // name of extra in intent that contains number of meals


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeUI();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mIngredientViewModel = ViewModelProviders.of(this).get(IngredientViewModel.class);
        mIngredientViewModel.getAllIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable final List<Ingredient> ingredients) {
                // Update the cached copy of the ingredients
                setIngredients(ingredients);
            }
        });

        Intent intent = getIntent();
        final int nbMeals = intent.getIntExtra(this.EXTRA_NB_MEALS, 1);

        // Issue: onChanged hasn't been called yet, so this.ingredients is null
        // Not sure how to cleanly wait for onChanged to be called before calling  generateMealPlan,
        // So just wait wait 500ms before calling generateMealPlan. NOT GUARANTEED TO WORK EVERYTIME
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    generateMealPlan(nbMeals);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 500);


    }
    private void initializeUI() {
        setContentView(R.layout.activity_mealplan);
        textViewMealPlan = findViewById(R.id.textViewMealPlan);
        textViewMealPlan.setMovementMethod(new ScrollingMovementMethod());
    }
    public void generateMealPlan(int nbMeals) throws Exception {
        plan = new MealPlan();
        plan.makePlan(nbMeals, this.ingredients);
        textViewMealPlan.setText(plan.toString());
        setShareActionIntent(plan.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setShareActionIntent("No plan generated");
        return super.onCreateOptionsMenu(menu);
    }
    private void setShareActionIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);
    }

    void setIngredients(List<Ingredient> ingredients){
        this.ingredients = ingredients;
    }
}