package com.pierrecattin.mealprep;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MealPlan {
    private static final String TAG = "MealPlan";
    private List<Meal>  mMeals = new ArrayList<Meal>();
    private int mNbMeals;

    public MealPlan(int nbMeals, List<Ingredient> ingredients){
        Log.i(TAG, "MealPlan: start");
        mNbMeals = nbMeals;
        for(int i=0; i<nbMeals; i++){
            mMeals.add(new Meal());
            mMeals.get(i).fillIngredients(ingredients);
            Log.i(TAG, "MealPlan: filled meal");
        }

    }

    public List<Meal> getMeals(){
        return mMeals;
    }

    public int size(){
        return mNbMeals;
    }

}
