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

    public void generateMealPlan(int nbMeals){
        String stringOut="";
        IngredientProperties ingredientProperties = new IngredientProperties();
        MealPlan plan = new MealPlan(nbMeals, ingredients);
        for (int i=0; i<plan.size(); i++){
            stringOut = stringOut + plan.getMeals().get(i).toString();
        }
        stringOut += "\n\n\n";
        out.print(stringOut);

    }

}
