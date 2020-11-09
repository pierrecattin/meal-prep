package com.pierrecattin.mealprep;


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
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


public class GenerateActivity extends LifecycleLoggingAppCompatActivity  {
    private NumberPicker numberPickerMeals;
    private List<Ingredient> ingredients;
    private List<Ingredient> requiredIngredients = new ArrayList<Ingredient>();
    private IngredientViewModel mIngredientViewModel;
    private IngredientListAdapter adapter;
    public static final String EXTRA_REQUIRED_INGREDIENT = "required_ingredient";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        numberPickerMeals = findViewById(R.id.numberPickerMeals);
        numberPickerMeals.setMaxValue(6);
        numberPickerMeals.setMinValue(1);

        Intent intent = getIntent();
        Ingredient newRequiredIngredient = (Ingredient)intent.getSerializableExtra(this.EXTRA_REQUIRED_INGREDIENT);
        if(newRequiredIngredient != null){
            this.requiredIngredients.add(newRequiredIngredient);
        }

        if(savedInstanceState != null){
            numberPickerMeals.setValue(savedInstanceState.getInt("numberPickerMealsValue"));
            this.requiredIngredients = (List)savedInstanceState.getSerializable("requiredIngredients");
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

        RecyclerView requiredIngredientsRecyclerView = findViewById(R.id.requiredIngredientsRecyclerView);
        adapter = new IngredientListAdapter(this);
        if(requiredIngredients!=null && requiredIngredients.size()>0){
            adapter.setIngredients(requiredIngredients);
        }
        requiredIngredientsRecyclerView.setAdapter(adapter);
        int RecyclerViewNbColumns;
        if(requiredIngredients==null || requiredIngredients.size()<=1){
            RecyclerViewNbColumns = 1;
        } else{
            RecyclerViewNbColumns = 2;
        }
        requiredIngredientsRecyclerView.setLayoutManager(new GridLayoutManager(this, RecyclerViewNbColumns));
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("numberPickerMealsValue",numberPickerMeals.getValue());
        savedInstanceState.putSerializable("requiredIngredients",(Serializable)requiredIngredients);
    }

    public void generatePressed(View view) throws Exception {
        MealPlan plan = new MealPlan();
        List ingredientsAvailable = ingredients;
        //ingredientsAvailable.remove(10);

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
