package com.pierrecattin.mealprep;


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
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
    private IngredientListAdapter requiredIngredientsAdapter;

    private RecyclerView requiredIngredientsRecyclerView;

    public static final String EXTRA_REQUIRED_INGREDIENT = "required_ingredient";

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

        mIngredientViewModel = ViewModelProviders.of(this).get(IngredientViewModel.class);
        mIngredientViewModel.getAllIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable final List<Ingredient> ingredients) {
                // Update the cached copy of the ingredients
                setIngredients(ingredients);
            }
        });

        requiredIngredientsAdapter = new IngredientListAdapter(this);
        requiredIngredientsRecyclerView = findViewById(R.id.requiredIngredientsRecyclerView);
        mIngredientViewModel.getRequiredIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable final List<Ingredient> requiredIngredients) {
                setRequiredIngredients(requiredIngredients);
                updateRequiredIngredientsView();
            }
        });

        requiredIngredientsAdapter.setListener(new IngredientListAdapter.Listener() {
            @Override
            public void onClick(int position) {
                removeRequiredIngredient(requiredIngredients.get(position));
            }
        });
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
    public void setRequiredIngredients(List requiredIngredients){
        if(requiredIngredients.size()>0){
            this.requiredIngredients = requiredIngredients;
        } else{
            this.requiredIngredients.clear();
        }
    }

    private void removeRequiredIngredient(Ingredient ingredient){
        this.requiredIngredients.remove(ingredient);
        ingredient.setRequired(false);
        mIngredientViewModel.update(ingredient);
        updateRequiredIngredientsView();
    }
    private void updateRequiredIngredientsView(){
        requiredIngredientsAdapter.setIngredients(requiredIngredients);
        requiredIngredientsRecyclerView.setAdapter(requiredIngredientsAdapter);
        int RecyclerViewNbColumns;
        if(requiredIngredients==null || requiredIngredients.size()<=1){
            RecyclerViewNbColumns = 1;
        } else{
            RecyclerViewNbColumns = 2;
        }
        requiredIngredientsRecyclerView.setLayoutManager(new GridLayoutManager(this, RecyclerViewNbColumns));
    }
}
