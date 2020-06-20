package com.pierrecattin.mealprep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InputActivity extends AppCompatActivity {
    private Button generate;
    private TextView ingredientDisplay;
    private Logic logic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeUI();
        logic = new Logic(this);
    }

    private void initializeUI() {
        setContentView(R.layout.activity_input);

        generate = (Button)findViewById(R.id.buttonGenerate);
        ingredientDisplay = (TextView)findViewById(R.id.textViewIngredient);
    }
    public void generatePressed(View view){
        logic.process();
    }
    public void print(String resultString){
        ingredientDisplay.setText(resultString);
    }

}
