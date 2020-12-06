package com.pierrecattin.mealprep;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;


public class GenerateActivity extends AppCompatActivity {
    private NumberPicker numberPickerMeals;

    private List<Ingredient> ingredients;
    private List<Ingredient> requiredIngredients;
    private List<Ingredient> forbiddenIngredients;

    private IngredientViewModel mIngredientViewModel;

    private RecyclerView requiredIngredientsRecyclerView;
    private RecyclerView forbiddenIngredientsRecyclerView;

    private IngredientListAdapter requiredIngredientsAdapter;
    private IngredientListAdapter forbiddenIngredientsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        numberPickerMeals = findViewById(R.id.numberPickerMeals);
        numberPickerMeals.setMaxValue(6);
        numberPickerMeals.setMinValue(1);

        if(savedInstanceState != null){
            numberPickerMeals.setValue(savedInstanceState.getInt("numberPickerMealsValue"));
        }

        setUpRecyclerViews();

        mIngredientViewModel = ViewModelProviders.of(this).get(IngredientViewModel.class);
        updateLocalIngredientsFromDB();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("numberPickerMealsValue",numberPickerMeals.getValue());
    }

    private void setUpRecyclerViews(){
        requiredIngredientsRecyclerView = findViewById(R.id.requiredIngredientsRecyclerView);
        forbiddenIngredientsRecyclerView = findViewById(R.id.forbiddenIngredientsRecyclerView);
        requiredIngredientsAdapter = new IngredientListAdapter(this);
        forbiddenIngredientsAdapter = new IngredientListAdapter(this);

        requiredIngredientsRecyclerView.setAdapter(requiredIngredientsAdapter);
        forbiddenIngredientsRecyclerView.setAdapter(forbiddenIngredientsAdapter);

        requiredIngredientsRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        forbiddenIngredientsRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        requiredIngredientsAdapter.setListener(new IngredientListAdapter.Listener() {
            @Override
            public void onClick(int position) {
                removeRequiredIngredient(requiredIngredientsAdapter.getIngredientAtPosition(position));
            }
        });

        forbiddenIngredientsAdapter.setListener(new IngredientListAdapter.Listener() {
            @Override
            public void onClick(int position) {
                removeForbiddenIngredient(forbiddenIngredientsAdapter.getIngredientAtPosition(position));
            }
        });

    }

    public void generatePressed(View view) throws Exception {
        MealPlan plan = new MealPlan();
        List ingredientsAvailable = ingredients;
        if(forbiddenIngredients != null){
            ingredientsAvailable.removeAll(forbiddenIngredients);
        }

        if(plan.makePlan(numberPickerMeals.getValue(), ingredientsAvailable, requiredIngredients)){
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

    public void manageIngredientsPressed(View view){
        Intent intent = new Intent(this, ManageIngredientsActivity.class);
        startActivity(intent);
    }


    private void removeRequiredIngredient(Ingredient ingredient){
        ingredient.setRequired(false);
        mIngredientViewModel.update(ingredient);
        updateLocalIngredientsFromDB();
    }
    private void removeForbiddenIngredient(Ingredient ingredient){
        ingredient.setForbidden(false);
        mIngredientViewModel.update(ingredient);
        updateLocalIngredientsFromDB();
    }


    private void  updateLocalIngredientsFromDB() {
        mIngredientViewModel.getAllIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable final List<Ingredient> ing) {
                // Update the cached copy of the ingredients
                ingredients = ing;
            }
        });

        mIngredientViewModel.getRequiredIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable final List<Ingredient> reqIngredients) {
                requiredIngredients = reqIngredients;
                updateRecyclerViews();
            }
        });
        mIngredientViewModel.getForbiddenIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable final List<Ingredient> forbIngredients) {
                forbiddenIngredients = forbIngredients;
                updateRecyclerViews();
            }
        });
    }
    private void updateRecyclerViews(){
        if(requiredIngredients != null ){
            requiredIngredientsAdapter.setIngredients(requiredIngredients);
        }
        if(forbiddenIngredients != null ){
            forbiddenIngredientsAdapter.setIngredients(forbiddenIngredients);
        }
    }
}
