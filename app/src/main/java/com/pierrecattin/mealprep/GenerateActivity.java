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
import java.util.Random;


public class GenerateActivity extends AppCompatActivity {
    private NumberPicker numberPickerMeals;
    private List<Ingredient> ingredients;
    private List<Ingredient> requiredIngredients = new ArrayList<Ingredient>();
    private IngredientViewModel mIngredientViewModel;
    private IngredientListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        numberPickerMeals = findViewById(R.id.numberPickerMeals);
        numberPickerMeals.setMaxValue(6);
        numberPickerMeals.setMinValue(1);

        if(savedInstanceState != null){
            numberPickerMeals.setValue(savedInstanceState.getInt("numberPickerMealsValue"));
        }

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
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("numberPickerMealsValue",numberPickerMeals.getValue());
    }

    public void generatePressed(View view) throws Exception {
        MealPlan plan = new MealPlan();
        List ingredientsAvailable = ingredients;
        //ingredientsAvailable.remove(10);
        requiredIngredients = new ArrayList<>();
        //requiredIngredients.add(ingredients.get(3));


        if(plan.makePlan(numberPickerMeals.getValue(), ingredients, requiredIngredients)){
            Intent intent = new Intent(this, MealplanActivity.class);
            intent.putExtra(MealplanActivity.EXTRA_PLAN, plan);
            intent.putExtra(MealplanActivity.EXTRA_INGREDIENTS, (Serializable) ingredientsAvailable);
            intent.putExtra(MealplanActivity.EXTRA_REQUIRED_INGREDIENTS, (Serializable) requiredIngredients);
            intent.putExtra(MealplanActivity.EXTRA_NBMEALS, numberPickerMeals.getValue());
            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(this, R.string.toast_generation_error, Toast.LENGTH_LONG);
            toast.show();
        }
    }


    public void addRequiredIngredientPressed(View view){
        Intent intent = new Intent(this, PickIngredientActivity.class);
        intent.putExtra(MealplanActivity.EXTRA_INGREDIENTS, (Serializable) ingredients);
        startActivity(intent);

        //requiredIngredients.add(newRequiredIngredient);
        //adapter.addIngredient(newRequiredIngredient);
    }


    public void setIngredients(List ingredients){
        this.ingredients = ingredients;
    }
}
