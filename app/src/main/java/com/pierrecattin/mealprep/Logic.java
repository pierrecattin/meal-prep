package com.pierrecattin.mealprep;


import java.util.List;
import java.util.Random;

import android.util.Log;


public class Logic {
    protected InputActivity out;
    private List<Ingredient> ingredients;

    public Logic(InputActivity out){
        this.out=out;
    }

    void setIngredients(List<Ingredient> ingredients){
        this.ingredients = ingredients;
    }

    public void process(){
        IngredientProperties ingredientProperties = new IngredientProperties();
        Log.i("process", "Number of ingredients: " + ingredients.size());

        Meal myFirstMeal = new Meal();
        int maxTrial = 1000;
        int trialCount = 0;
        while (!myFirstMeal.allTypesMinAchieved() & trialCount<maxTrial){
            Random rand = new Random();
            myFirstMeal.addIngredient(ingredients.get(rand.nextInt(ingredients.size())));
            trialCount ++;
        }
        if(myFirstMeal.allTypesMinAchieved()){
            out.print("Meal found in " + trialCount + " trials" + "\n\n" +
                    myFirstMeal.toString());
        } else {
            out.print("No valid meal found after "+trialCount+" trials");
        }

    }

}
