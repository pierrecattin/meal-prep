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
    private IngredientViewModel mIngredientViewModel;
    private RecyclerView availableIngredientsRecyclerView;
    private RecyclerView requiredIngredientsRecyclerView;
    private IngredientListAdapter availableIngredientsAdapter;
    private IngredientListAdapter requiredIngredientsAdapter;

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
        availableIngredientsAdapter = new IngredientListAdapter(this);
        requiredIngredientsAdapter = new IngredientListAdapter(this);

        availableIngredientsRecyclerView.setAdapter(availableIngredientsAdapter);
        availableIngredientsRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mIngredientViewModel = ViewModelProviders.of(this).get(IngredientViewModel.class);
        updateLocalIngredientsFromDB();
        availableIngredientsAdapter.setListener(new IngredientListAdapter.Listener() {
            @Override
            public void onClick(int position) {
                addRequiredIngredient(availableIngredients.get(position));
            }
        });

    }

    private void addRequiredIngredient(Ingredient ingredient){
         Toast toast = Toast.makeText(this, "New required ingredient: "+ingredient.toString(), Toast.LENGTH_LONG);
         toast.show();
         ingredient.setRequired(true);
         mIngredientViewModel.update(ingredient);

         availableIngredients.remove(ingredient);
         updateRecyclerViews();
    }

    private void  updateLocalIngredientsFromDB(){
        mIngredientViewModel.getNotRequiredIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable final List<Ingredient> notRequiredIngredients) {
                availableIngredients = notRequiredIngredients;
                updateRecyclerViews();
            }
        });
        mIngredientViewModel.getRequiredIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable final List<Ingredient> requiredIngredients) {
                setRequiredIngredients(requiredIngredients);
            }
        });
    }

    private void setRequiredIngredients(List<Ingredient> requiredIngredients){
        if(requiredIngredients!=null){
            this.requiredIngredients = requiredIngredients;
        }

    }

    private void updateRecyclerViews(){
        if(availableIngredients != null){
            availableIngredientsAdapter.setIngredients(availableIngredients);
        }
        if(requiredIngredients != null ){
            //requiredIngredientsAdapter.setIngredients(requiredIngredients);
        }
    }
}