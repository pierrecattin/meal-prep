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
        Ingredient[] ingredients =  new Ingredient[10];
        ingredients[0] = new Ingredient("Pasta", "Carbs");
        ingredients[1] = new Ingredient("Broccoli", "Veggies");
        ingredients[2] = new Ingredient("Tofu", "Protein");
        ingredients[3] = new Ingredient("Rice", "Carbs");
        ingredients[4] = new Ingredient("Soy sauce", "Sauce");
        ingredients[5] = new Ingredient("Potatoes", "Carbs");
        ingredients[6] = new Ingredient("Carrots", "Veggies");
        ingredients[7] = new Ingredient("Pumpkin", "Veggies");
        ingredients[8] = new Ingredient("Quorn", "Protein");
        ingredients[9] = new Ingredient("Chickpeas", "Protein");

        ingredients[0].addStyle("European");
        ingredients[2].addStyle("Asian");
        ingredients[2].addStyle("Dough");
        ingredients[4].addStyle("Asian");

        //out.print(ingredients[0].toString());

        Meal myFirstMeal = new Meal();
        int maxTrial = 10000;
        int trialCount = 0;
        while (!myFirstMeal.allTypesMinAchieved() & trialCount<maxTrial){
            Random rand = new Random();
            myFirstMeal.addIngredient(ingredients[rand.nextInt(ingredients.length)]);
            trialCount ++;
        }
        if(myFirstMeal.allTypesMinAchieved()){
            out.print("Meal found in " + trialCount + " trials" + "\n" +
                    myFirstMeal.toString());
        } else {
            out.print("No valid meal found after "+trialCount+" trials");
        }

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
