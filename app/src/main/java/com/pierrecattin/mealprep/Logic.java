package com.pierrecattin.mealprep;

import java.util.Random;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import com.opencsv.CSVReader;


public class Logic {
    protected InputActivity out;
    public Logic(InputActivity out){
        this.out=out;
    }

    public void process(){
        Ingredient[] ingredients =  new Ingredient[4];
        ingredients[0] = new Ingredient("Pasta", "Carbs");
        ingredients[1] = new Ingredient("Broccoli", "Veggies");
        ingredients[2] = new Ingredient("Tofu", "Protein");
        ingredients[3] = new Ingredient("Rice", "Carbs");
        ingredients[1].addStyle("Asian");
        ingredients[1].addStyle("European");
        ingredients[2].addStyle("Asian");

        Meal myFirstMeal = new Meal();
        int nbIngredients = myFirstMeal.getIngredients().size();
        while (nbIngredients<2){
            Random rand = new Random();
            myFirstMeal.addIngredient(ingredients[rand.nextInt(ingredients.length)]);
            nbIngredients = myFirstMeal.getIngredients().size();
        }
        out.print(myFirstMeal.toString());

        /*try {
            String csvfileString = this.getApplicationInfo().dataDir + File.separatorChar + "ingredients.csv";
            File csvfile = new File(csvfileString);
            CSVReader reader = new CSVReader(new FileReader(csvfile.getAbsolutePath()));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                System.out.println(nextLine[0] + nextLine[1] + "etc...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
        } */

    }

}
