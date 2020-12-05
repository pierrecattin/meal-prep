package com.pierrecattin.mealprep;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class PickIngredientActivity extends AppCompatActivity {
    private List<Ingredient> availableIngredients;
    private List<Ingredient> requiredIngredients;
    private List<Ingredient> forbiddenIngredients;

    private IngredientViewModel mIngredientViewModel;
    private RecyclerView availableIngredientsRecyclerView;
    private RecyclerView requiredIngredientsRecyclerView;
    private RecyclerView forbiddenIngredientsRecyclerView;

    private IngredientListAdapter availableIngredientsAdapter;
    private IngredientListAdapter requiredIngredientsAdapter;
    private IngredientListAdapter forbiddenIngredientsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_ingredient);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        availableIngredientsRecyclerView = findViewById(R.id.availableIngredientsRecyclerView);
        requiredIngredientsRecyclerView = findViewById(R.id.requiredIngredientsRecyclerView);
        forbiddenIngredientsRecyclerView = findViewById(R.id.forbiddenIngredientsRecyclerView);
        availableIngredientsAdapter = new IngredientListAdapter(this);
        requiredIngredientsAdapter = new IngredientListAdapter(this);
        forbiddenIngredientsAdapter = new IngredientListAdapter(this);

        availableIngredientsRecyclerView.setAdapter(availableIngredientsAdapter);
        requiredIngredientsRecyclerView.setAdapter(requiredIngredientsAdapter);
        forbiddenIngredientsRecyclerView.setAdapter(forbiddenIngredientsAdapter);

        availableIngredientsRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        requiredIngredientsRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        forbiddenIngredientsRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        mIngredientViewModel = ViewModelProviders.of(this).get(IngredientViewModel.class);
        updateLocalIngredientsFromDB();

        forbiddenIngredientsAdapter.setListener(new IngredientListAdapter.Listener() {
            @Override
            public void onClick(int position) {
                toggleIngredientForbidden(forbiddenIngredients.get(position));
            }
        });

        availableIngredientsAdapter.setListener(new IngredientListAdapter.Listener() {
            @Override
            public void onClick(int position) {
                toggleIngredientForbidden(availableIngredients.get(position));
            }
        });

        requiredIngredientsAdapter.setListener(new IngredientListAdapter.Listener() {
            @Override
            public void onClick(int position) {
                toggleIngredientRequired(requiredIngredients.get(position));
            }
        });

    }


    private void toggleIngredientRequired(Ingredient ingredient){
        String toastMessage;
        if(ingredient.getRequired()){
            toastMessage = "Removing required ingredient: ";
            ingredient.setRequired(false);
        } else {
            toastMessage = "Making ingredient required: ";
            ingredient.setRequired(true);
        }
        Toast toast = Toast.makeText(this, toastMessage+ingredient.toString(), Toast.LENGTH_LONG);
        toast.show();
        mIngredientViewModel.update(ingredient);
        updateLocalIngredientsFromDB();
        updateRecyclerViews();
    }

    private void toggleIngredientForbidden(Ingredient ingredient){
        String toastMessage;
        if(ingredient.getForbidden()){
            toastMessage = "Making ingredient available: ";
            ingredient.setForbidden(false);
        } else {
            toastMessage = "Making ingredient forbidden: ";
            ingredient.setForbidden(true);
        }
        Toast toast = Toast.makeText(this, toastMessage+ingredient.toString(), Toast.LENGTH_LONG);
        toast.show();
        mIngredientViewModel.update(ingredient);
        updateLocalIngredientsFromDB();
        updateRecyclerViews();
    }

    private void  updateLocalIngredientsFromDB(){
        mIngredientViewModel.getAvailableIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable final List<Ingredient> notRequiredIngredients) {
                availableIngredients = notRequiredIngredients;
                updateRecyclerViews();
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
        if(availableIngredients != null){
            availableIngredientsAdapter.setIngredients(availableIngredients);
        }
        if(requiredIngredients != null ){
            requiredIngredientsAdapter.setIngredients(requiredIngredients);
        }
        if(forbiddenIngredients != null ){
            forbiddenIngredientsAdapter.setIngredients(forbiddenIngredients);
        }
    }
}