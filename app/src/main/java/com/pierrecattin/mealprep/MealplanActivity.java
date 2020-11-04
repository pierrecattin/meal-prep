package com.pierrecattin.mealprep;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

public class MealplanActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMealPlan;
    private ShareActionProvider shareActionProvider;
    public static final String EXTRA_PLAN = "plan"; // name of extra in intent that contains number of mealplan
    public static final String EXTRA_INGREDIENTS = "ingredients";
    public static final String EXTRA_REQUIRED_INGREDIENTS = "required_ingredients";
    public static final String EXTRA_NBMEALS = "nbMeals";

    private MealPlan plan;
    private List<Ingredient> ingredients;
    private List<Ingredient> requiredIngredients;
    private int nbMeals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeUI();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if(savedInstanceState == null){
            plan = (MealPlan) intent.getSerializableExtra(this.EXTRA_PLAN);
        } else {
            plan = (MealPlan) savedInstanceState.getSerializable("plan");
        }
        nbMeals = intent.getIntExtra(this.EXTRA_NBMEALS, plan.length());
        ingredients = (List)intent.getSerializableExtra(this.EXTRA_INGREDIENTS);
        requiredIngredients = (List)intent.getSerializableExtra(this.EXTRA_REQUIRED_INGREDIENTS);
        displayMealPlan();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("plan", plan);
    }

    private void initializeUI() {
        setContentView(R.layout.activity_mealplan);
        recyclerViewMealPlan = findViewById(R.id.recyclerViewMealPlan);
    }

    public void generatePressed(View view) throws Exception {
        plan = new MealPlan();


        if(plan.makePlan(nbMeals, ingredients, requiredIngredients)){
            displayMealPlan();
            setShareActionIntent(plan.toString());
        } else {
            Toast toast = Toast.makeText(this, R.string.toast_generation_error, Toast.LENGTH_LONG);
            toast.show();
        }
    }
    public void displayMealPlan() {
        String[] carbs = new String[plan.length()];
        String[] sauces = new String[plan.length()];
        for (int i=0; i<plan.length(); i++){
            Meal currentMeal = (Meal)plan.getMeals().get(i);
            carbs[i] = currentMeal.printComponent("carbs");
            carbs[i] += "\n";
            sauces[i] = currentMeal.printComponent("sauce");
        }
        MealCardAdapter adapter = new MealCardAdapter(carbs, sauces);
        recyclerViewMealPlan.setAdapter(adapter);
        int RecyclerViewNbColumns;
        if(plan.length()==1){
            RecyclerViewNbColumns = 1;
        } else{
            RecyclerViewNbColumns = 2;
        }
        GridLayoutManager layoutManager = new GridLayoutManager(this, RecyclerViewNbColumns);
        recyclerViewMealPlan.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setShareActionIntent(plan.toString());
        return super.onCreateOptionsMenu(menu);
    }
    private void setShareActionIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);
    }


}