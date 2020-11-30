package com.pierrecattin.mealprep;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class PickIngredientActivity extends AppCompatActivity {
    public static final String EXTRA_INGREDIENTS = "ingredients";
    List<Ingredient> ingredients;
    private IngredientViewModel mIngredientViewModel;
    RecyclerView ingredientsRecyclerView;
    IngredientListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pick_ingredient);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ingredientsRecyclerView = findViewById(R.id.ingredientsRecyclerView);
        adapter = new IngredientListAdapter(this);

        ingredientsRecyclerView.setAdapter(adapter);
        ingredientsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        Intent intent = getIntent();
        ingredients = (List)intent.getSerializableExtra(this.EXTRA_INGREDIENTS);

        updateRecyclerView();

        mIngredientViewModel = ViewModelProviders.of(this).get(IngredientViewModel.class);
        adapter.setListener(new IngredientListAdapter.Listener() {
            @Override
            public void onClick(int position) {
                addRequiredIngredient(ingredients.get(position));



            }
        });

    }
     void addRequiredIngredient(Ingredient ingredient){
         Toast toast = Toast.makeText(this, "New required ingredient: "+ingredient.toString(), Toast.LENGTH_LONG);
         toast.show();
         ingredient.setRequired(true);
         mIngredientViewModel.update(ingredient);

         ingredients.remove(ingredient);
         updateRecyclerView();
    }

    void updateRecyclerView(){
        adapter.setIngredients(ingredients);
    }
}