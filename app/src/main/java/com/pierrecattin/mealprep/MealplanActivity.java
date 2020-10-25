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
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MealplanActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMealPlan;
    private MealPlan plan;
    private ShareActionProvider shareActionProvider;
    public static final String EXTRA_NB_MEALS = "nb_meals"; // name of extra in intent that contains number of meals
    public static final String EXTRA_INGREDIENTS = "ingredients";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeUI();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        int nbMeals = intent.getIntExtra(this.EXTRA_NB_MEALS, 1);
        List<Ingredient> ingredients = (List<Ingredient>)intent.getSerializableExtra(this.EXTRA_INGREDIENTS);
        try {
            generateMealPlan(nbMeals, ingredients);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initializeUI() {
        setContentView(R.layout.activity_mealplan);
        recyclerViewMealPlan = findViewById(R.id.recyclerViewMealPlan);
    }
    public void generateMealPlan(int nbMeals, List ingredients) throws Exception {
        plan = new MealPlan();
        plan.makePlan(nbMeals, ingredients);
        String[] carbs = new String[nbMeals];
        String[] sauces = new String[nbMeals];
        for (int i=0; i<nbMeals; i++){
            Meal currentMeal = (Meal)plan.getMeals().get(i);
            carbs[i] = currentMeal.printComponent("carbs");
            carbs[i] += "\n";
            sauces[i] = currentMeal.printComponent("sauce");
        }
        MealCardAdapter adapter = new MealCardAdapter(carbs, sauces);
        recyclerViewMealPlan.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewMealPlan.setLayoutManager(layoutManager);
        setShareActionIntent(plan.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setShareActionIntent("No plan generated");
        return super.onCreateOptionsMenu(menu);
    }
    private void setShareActionIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);
    }


}