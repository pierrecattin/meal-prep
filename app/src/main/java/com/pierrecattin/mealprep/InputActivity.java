package com.pierrecattin.mealprep;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.List;

public class InputActivity extends AppCompatActivity {
    private Button generate;
    private TextView ingredientDisplay;
    private NumberPicker numberPickerMeals;

    private Logic logic;
    private IngredientViewModel mIngredientViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeUI();
        logic = new Logic(this);
        mIngredientViewModel = ViewModelProviders.of(this).get(IngredientViewModel.class);
        mIngredientViewModel.getAllIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable final List<Ingredient> ingredients){
                // Update the cached copy of the ingredients in the logic.
                logic.setIngredients(ingredients);
            }
        });

    }

    private void initializeUI() {
        setContentView(R.layout.activity_input);

        generate = (Button)findViewById(R.id.buttonGenerate);
        ingredientDisplay = (TextView)findViewById(R.id.textViewMealPlan);
        ingredientDisplay.setMovementMethod(new ScrollingMovementMethod());
        numberPickerMeals = findViewById(R.id.numberPickerMeals);
        numberPickerMeals.setMaxValue(6);
        numberPickerMeals.setMinValue(1);
    }
    public void generatePressed(View view){
        logic.generateMealPlan(numberPickerMeals.getValue());
    }
    public void print(String resultString){
        ingredientDisplay.setText(resultString);
    }

}
