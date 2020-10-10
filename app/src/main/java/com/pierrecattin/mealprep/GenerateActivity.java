package com.pierrecattin.mealprep;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.List;

public class GenerateActivity extends AppCompatActivity {
    private TextView textViewMealPlan;
    private NumberPicker numberPickerMeals;
    private IngredientViewModel mIngredientViewModel;
    private List<Ingredient> ingredients;

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
        MealPlan plan = new MealPlan();
        plan.makePlan(nbMeals, ingredients);
        textViewMealPlan.setText(plan.toString());
    }

    void setIngredients(List<Ingredient> ingredients){
        this.ingredients = ingredients;
    }
}
