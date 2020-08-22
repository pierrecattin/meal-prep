package com.pierrecattin.mealprep;


import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MealPlan {
    private static final String TAG = "MealPlan";
    private List<Meal>  mMeals = new ArrayList<Meal>();
    private int mNbMeals;

    public MealPlan(){
    }

    public void addMeal(Meal meal){
        mMeals.add(meal);
    }
    public boolean fillMeals(int nbMeals, List<Ingredient> ingredients){
        mNbMeals = nbMeals;
        int maxTrial = 100;
        int maxIterPerTrial = 100;
        int trialCount = 0;
        int iterCount;
        while(trialCount<maxTrial && mMeals.size()<mNbMeals){
            iterCount=0;
            mMeals.clear();
            while (iterCount<maxIterPerTrial && mMeals.size()<mNbMeals){
                Meal newMeal = new Meal();
                newMeal.fillIngredients(ingredients, true);
                this.addMeal(newMeal);
                iterCount++;
            }
            trialCount++;
        }
        if(mMeals.size()<mNbMeals){
            mMeals.clear();
            return false;
        } else{
            return true;
        }
    }


    public List<Meal> getMeals(){
        return mMeals;
    }

    public Set<Ingredient> getIngredients(){
        Set<Ingredient> ingredients =new HashSet<Ingredient>();
        for(int i=0; i<mMeals.size(); i++){
            ingredients.addAll(mMeals.get(i).getIngredients());
        }
        return ingredients;
    }


}
