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

    public void generateMealPlan(int nbMeals) throws Exception {
        MealPlan plan = new MealPlan();
        plan.makePlan(nbMeals, ingredients);
        out.print(plan.toString());
    }

}
