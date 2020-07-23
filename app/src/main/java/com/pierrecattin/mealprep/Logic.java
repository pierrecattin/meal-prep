package com.pierrecattin.mealprep;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.util.Set;

import com.opencsv.CSVReader;


public class Logic {
    protected InputActivity out;
    public Logic(InputActivity out){
        this.out=out;
    }

    public void process(){
        IngredientProperties ingredientProperties = new IngredientProperties();

        Set<Ingredient> ingredientsSet=new HashSet<Ingredient>();
        ingredientsSet.add(new Ingredient("Pasta", "Carbs", new HashSet<>(Arrays.asList("European"))));
        ingredientsSet.add(new Ingredient("Rice", "Carbs"));
        ingredientsSet.add(new Ingredient("Potatoes", "Carbs"));

        ingredientsSet.add(new Ingredient("Tofu", "Protein"));
        ingredientsSet.add(new Ingredient("Quorn", "Protein"));
        ingredientsSet.add(new Ingredient("Veggie mince meat", "Protein"));

        ingredientsSet.add(new Ingredient("Chickpeas", "Peas"));
        ingredientsSet.add(new Ingredient("Green beans", "Peas"));
        
        ingredientsSet.add(new Ingredient("Broccoli", "Veggies"));
        ingredientsSet.add(new Ingredient("Carrots", "Veggies"));
        ingredientsSet.add(new Ingredient("Pumpkin", "Veggies", new HashSet<>(Arrays.asList("European"))));

        ingredientsSet.add(new Ingredient("Soy sauce", "Sauce", new HashSet<>(Arrays.asList("Asian"))));
        ingredientsSet.add(new Ingredient("Cream", "Sauce"));
        ingredientsSet.add(new Ingredient("White Wine", "Sauce", new HashSet<>(Arrays.asList("European"))));

        Ingredient[] ingredients = ingredientsSet.toArray( new Ingredient[ingredientsSet.size()]);


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
            out.print("Meal found in " + trialCount + " trials" + "\n\n" +
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
