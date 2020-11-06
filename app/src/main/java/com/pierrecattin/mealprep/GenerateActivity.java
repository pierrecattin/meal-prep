package com.pierrecattin.mealprep;


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class GenerateActivity extends AppCompatActivity {
    private NumberPicker numberPickerMeals;
    private List<Ingredient> ingredients;
    private IngredientViewModel mIngredientViewModel;
    private IngredientListAdapter adapter;

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

        RecyclerView excludedIngredientsRecyclerView = findViewById(R.id.excludedIngredientsRecyclerView);
        adapter = new IngredientListAdapter(this);
        excludedIngredientsRecyclerView.setAdapter(adapter);
        excludedIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void initializeUI() {
        setContentView(R.layout.activity_generate);
        numberPickerMeals = findViewById(R.id.numberPickerMeals);
        numberPickerMeals.setMaxValue(6);
        numberPickerMeals.setMinValue(1);
    }
    public void generatePressed(View view) throws Exception {
        MealPlan plan = new MealPlan();
        List<Ingredient> requiredIngredients = new ArrayList<Ingredient>();
        //requiredIngredients.add(ingredients.get(0));
        if(plan.makePlan(numberPickerMeals.getValue(), ingredients, requiredIngredients)){
            Intent intent = new Intent(this, MealplanActivity.class);
            intent.putExtra(MealplanActivity.EXTRA_PLAN, plan);
            intent.putExtra(MealplanActivity.EXTRA_INGREDIENTS, (Serializable) ingredients);
            intent.putExtra(MealplanActivity.EXTRA_REQUIRED_INGREDIENTS, (Serializable) requiredIngredients);
            intent.putExtra(MealplanActivity.EXTRA_NBMEALS, numberPickerMeals.getValue());
            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(this, R.string.toast_generation_error, Toast.LENGTH_LONG);
            toast.show();
        }
    }


    public void addRequiredIngredientPressed(View view){

        //List ingredientList = new ArrayList();
        //ingredientList.add(ingredients.get(0));
        //adapter.setIngredients(ingredientList);
        adapter.addIngredient(ingredients.get(0));
    }


    public void setIngredients(List ingredients){
        this.ingredients = ingredients;
    }
}
