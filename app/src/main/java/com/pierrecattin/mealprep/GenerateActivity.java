package com.pierrecattin.mealprep;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.List;

public class GenerateActivity extends AppCompatActivity {
    private TextView textViewMealPlan;
    private NumberPicker numberPickerMeals;
    private IngredientViewModel mIngredientViewModel;
    private List<Ingredient> ingredients;
    private MealPlan plan;
    private ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeUI();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mIngredientViewModel = ViewModelProviders.of(this).get(IngredientViewModel.class);
        mIngredientViewModel.getAllIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable final List<Ingredient> ingredients){
                // Update the cached copy of the ingredients
                setIngredients(ingredients);
            }
        });
    }

    private void initializeUI() {
        setContentView(R.layout.activity_generate);

        textViewMealPlan = (TextView)findViewById(R.id.textViewMealPlan);
        textViewMealPlan.setMovementMethod(new ScrollingMovementMethod());
        numberPickerMeals = findViewById(R.id.numberPickerMeals);
        numberPickerMeals.setMaxValue(6);
        numberPickerMeals.setMinValue(1);
    }
    public void generatePressed(View view) throws Exception {
        generateMealPlan(numberPickerMeals.getValue());
    }
    public void generateMealPlan(int nbMeals) throws Exception {
        plan = new MealPlan();
        plan.makePlan(nbMeals, ingredients);
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
