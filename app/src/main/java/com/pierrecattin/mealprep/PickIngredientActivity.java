package com.pierrecattin.mealprep;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class PickIngredientActivity extends AppCompatActivity {
    public static final String EXTRA_INGREDIENTS = "ingredients";
    List<Ingredient> ingredients;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pick_ingredient);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        RecyclerView ingredientsRecyclerView = findViewById(R.id.ingredientsRecyclerView);
        IngredientListAdapter adapter = new IngredientListAdapter(this);
        ingredientsRecyclerView.setAdapter(adapter);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        ingredients = (List)intent.getSerializableExtra(this.EXTRA_INGREDIENTS);
        adapter.setIngredients(ingredients);

        adapter.setListener(new IngredientListAdapter.Listener() {
            @Override
            public void onClick(int position) {
                //Toast toast = Toast.makeText(this, "ingredient position:", Toast.LENGTH_LONG);
                Log.i("PickIngredientActivity", "onClick: position="+position);
            }
        });

    }
}