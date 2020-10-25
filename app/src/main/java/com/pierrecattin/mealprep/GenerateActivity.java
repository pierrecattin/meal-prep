package com.pierrecattin.mealprep;


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.List;


public class GenerateActivity extends AppCompatActivity {
    private NumberPicker numberPickerMeals;
    private List<Ingredient> ingredients;
    private IngredientViewModel mIngredientViewModel;
    private MealPlan plan;

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

    }

    private void initializeUI() {
        setContentView(R.layout.activity_generate);
        numberPickerMeals = findViewById(R.id.numberPickerMeals);
        numberPickerMeals.setMaxValue(6);
        numberPickerMeals.setMinValue(1);
    }
    public void generatePressed(View view) throws Exception {
        plan = new MealPlan();
        plan.makePlan(numberPickerMeals.getValue(), ingredients);

        Intent intent = new Intent(this, MealplanActivity.class);
        intent.putExtra(MealplanActivity.EXTRA_PLAN, plan);
        startActivity(intent);
    }

    public void setIngredients(List ingredients){
        this.ingredients = ingredients;
    }
}
