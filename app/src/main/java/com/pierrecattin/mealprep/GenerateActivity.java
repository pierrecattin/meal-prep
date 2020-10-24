package com.pierrecattin.mealprep;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;


public class GenerateActivity extends AppCompatActivity {
    private NumberPicker numberPickerMeals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeUI();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initializeUI() {
        setContentView(R.layout.activity_generate);
        numberPickerMeals = findViewById(R.id.numberPickerMeals);
        numberPickerMeals.setMaxValue(6);
        numberPickerMeals.setMinValue(1);
    }
    public void generatePressed(View view) {
        Intent intent = new Intent(this, MealplanActivity.class);
        intent.putExtra(MealplanActivity.EXTRA_NB_MEALS, numberPickerMeals.getValue());
        startActivity(intent);
    }
}
