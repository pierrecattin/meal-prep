package com.pierrecattin.mealprep;


import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MealPlan  implements Serializable {
    private static final String TAG = "MealPlan";
    private List<Meal> mMeals = new ArrayList<Meal>();
    private int mNbMeals;

    public MealPlan() {
    }


    public boolean addMeal(Meal meal) {
        Map<Ingredient, Integer> ingredientsCount = this.countIngredients();
        boolean maxIngredientReached = false;

        Iterator<Ingredient> itr = meal.getIngredients().iterator();
        while (!maxIngredientReached && itr.hasNext()) {
            Ingredient ingredient = itr.next();
            if (ingredientsCount.containsKey(ingredient)) {
                maxIngredientReached = ingredientsCount.get(ingredient) >= ingredient.getMaxUsePerPlan();
            }
        }

        if (!maxIngredientReached) {
            mMeals.add(meal);
            return (true);
        } else {
            return (false);
        }
    }

    public boolean isValid() throws Exception {
        // Not enough meals
        if (mMeals.size() < mNbMeals) {
            return false;
        }
        // All meals are valid as standalone meals
        for (Meal meal : mMeals) {
            if (!meal.isValid()) {
                return false;
            }
        }

        // Max usage of ingredients are respected
        Map<Ingredient, Integer> ingredientsCount = this.countIngredients();
        Iterator<Ingredient> itr = ingredientsCount.keySet().iterator();
        while (itr.hasNext()) {
            Ingredient ingredient = itr.next();
            if (ingredientsCount.get(ingredient) > ingredient.getMaxUsePerPlan()) {
                return false;
            }
        }

        return true;
    }

    private void emptyMeals() {
        mMeals.clear();
        for (int i = 0; i < mNbMeals; i++) {
            mMeals.add(new Meal());
        }
    }

    // Create Meal based on list of ingredients, return ingredients that haven't been used
    private List<Ingredient> fillFirstMeal(List<Ingredient> ingredients) throws Exception {
        List<Ingredient> availableIngredients = new ArrayList<Ingredient>(ingredients);
        mMeals.get(0).fillCarbs(ingredients);
        mMeals.get(0).fillSauce(ingredients);
        availableIngredients.removeAll(mMeals.get(0).getIngredients());
        return availableIngredients;
    }

    // Copy one part of a Meal into another Meal. Return
    private List<Ingredient> fillMealFromMeal(List<Ingredient> availableIngredients, int sourceMealIndex, int targetMealIndex, String component) throws Exception {
        if(component=="Carbs"){
            mMeals.get(targetMealIndex).forceAddIngredients(mMeals.get(sourceMealIndex).getCarbs());
            mMeals.get(targetMealIndex).fillSauce(availableIngredients);
            availableIngredients.removeAll(mMeals.get(targetMealIndex).getSauce());
        } else if(component == "Sauce"){
            mMeals.get(targetMealIndex).forceAddIngredients(mMeals.get(sourceMealIndex).getSauce());
            mMeals.get(targetMealIndex).fillCarbs(availableIngredients);
            availableIngredients.removeAll(mMeals.get(targetMealIndex).getCarbs());
        } else {
            throw new Exception("component arg has invalid value: "+component);
        }
        return availableIngredients;
    }

    // Fill all Meals
    public boolean makePlan(int nbMeals, List<Ingredient> ingredients) throws Exception {
        mNbMeals = nbMeals;

        Random rand = new Random();
        int maxTrial = 1000;
        int trialCount = 0;
        Integer structure = null;

        if (nbMeals <= 2) {
            while (trialCount < maxTrial && !isValid()) {
                mMeals.clear();
                List<Ingredient> availableIngredients = new ArrayList<Ingredient>(ingredients);
                for (int i = 0; i < nbMeals; i++) {
                    Meal newMeal = new Meal();
                    newMeal.fillIngredients(availableIngredients);
                    availableIngredients.removeAll(newMeal.getIngredients());
                    this.addMeal(newMeal);
                }
                trialCount++;
            }
        } else if (nbMeals == 3) {
            while (trialCount < maxTrial && !isValid()) {
                emptyMeals();
                List<Ingredient> availableIngredients = fillFirstMeal(ingredients);
                // two options of structure
                structure = rand.nextInt(2)+1;

                if (structure == 1) {
                    availableIngredients = fillMealFromMeal(availableIngredients, 0,2,"Carbs");
                    fillMealFromMeal(availableIngredients, 2,1,"Sauce");
                } else {
                    availableIngredients = fillMealFromMeal(availableIngredients, 0,2,"Sauce");
                    fillMealFromMeal(availableIngredients, 2,1,"Carbs");
                }

                trialCount++;
            }
        } else if (nbMeals == 4) {
            while (trialCount < maxTrial && !isValid()) {
                emptyMeals();
                List<Ingredient> availableIngredients = fillFirstMeal(ingredients);

                availableIngredients = fillMealFromMeal(availableIngredients, 0,3,"Carbs");
                availableIngredients = fillMealFromMeal(availableIngredients, 3,1,"Sauce");
                fillMealFromMeal(availableIngredients, 0,2,"Sauce");

                trialCount++;
            }
        } else if (mNbMeals == 5) {
            while (trialCount < maxTrial && !isValid()) {
                emptyMeals();
                List<Ingredient> availableIngredients = fillFirstMeal(ingredients);

                structure = rand.nextInt(3) + 1;

                if (structure == 1) {
                    availableIngredients = fillMealFromMeal(availableIngredients, 0,4,"Carbs");
                    availableIngredients = fillMealFromMeal(availableIngredients, 0,3,"Sauce");
                    availableIngredients = fillMealFromMeal(availableIngredients, 4,2,"Sauce");
                    fillMealFromMeal(availableIngredients, 3,1,"Carbs");
                } else if (structure == 2){
                    availableIngredients = fillMealFromMeal(availableIngredients, 0,2,"Sauce");
                    availableIngredients = fillMealFromMeal(availableIngredients, 2,4, "Carbs");
                    availableIngredients = fillMealFromMeal(availableIngredients, 4,1,"Sauce");
                    fillMealFromMeal(availableIngredients, 0,3, "Carbs");
                } else if (structure == 3){
                    availableIngredients = fillMealFromMeal(availableIngredients, 0,2,"Carbs");
                    availableIngredients = fillMealFromMeal(availableIngredients, 2,4, "Sauce");
                    availableIngredients = fillMealFromMeal(availableIngredients, 4,1,"Carbs");
                    fillMealFromMeal(availableIngredients, 0,3, "Sauce");
                }
                trialCount++;
            }
        } else if (mNbMeals == 6) {
            while (trialCount < maxTrial && !isValid()) {
                emptyMeals();
                List<Ingredient> availableIngredients = fillFirstMeal(ingredients);

                structure = rand.nextInt(3)+1;
                if (structure == 1) {
                    availableIngredients = fillMealFromMeal(availableIngredients, 0,3,"Sauce");
                    availableIngredients = fillMealFromMeal(availableIngredients, 3,1,"Carbs");
                    availableIngredients = fillMealFromMeal(availableIngredients, 0,4,"Carbs");
                    fillMealFromMeal(availableIngredients, 4,2,"Sauce");

                    mMeals.get(5).forceAddIngredients(mMeals.get(2).getCarbs());
                    mMeals.get(5).forceAddIngredients(mMeals.get(1).getSauce());
                } else if (structure==2){
                    availableIngredients = fillMealFromMeal(availableIngredients,0,3,"Carbs");
                    availableIngredients = fillMealFromMeal(availableIngredients,3,1, "Sauce");

                    mMeals.get(4).forceAddIngredients(mMeals.get(1).getCarbs());
                    mMeals.get(4).forceAddIngredients(mMeals.get(0).getSauce());

                    availableIngredients=fillMealFromMeal(availableIngredients,0,5, "Carbs");
                    fillMealFromMeal(availableIngredients,5,2, "Sauce");

                } else if (structure==3){
                    availableIngredients = fillMealFromMeal(availableIngredients,0,5,"Carbs");
                    availableIngredients = fillMealFromMeal(availableIngredients, 5,1,"Sauce");
                    availableIngredients = fillMealFromMeal(availableIngredients,1,4,"Carbs");
                    fillMealFromMeal(availableIngredients,0,3,"Sauce");

                    mMeals.get(2).forceAddIngredients(mMeals.get(0).getCarbs());
                    mMeals.get(2).forceAddIngredients(mMeals.get(4).getSauce());
                }
                trialCount++;
            }
        }
        if(structure != null){
            Log.i(TAG, "makePlan: structure #" + structure);
        }
        Log.i(TAG, "makePlan: status=" + isValid() + " after " + trialCount + " trials");
        return isValid();
    }

    // count number of use by ingredient
    public Map<Ingredient, Integer> countIngredients() {
        Map<Ingredient, Integer> ingredientsCount = new HashMap<Ingredient, Integer>();
        for (Meal meal : mMeals) {
            for (Ingredient ingredient : meal.getIngredients()) {
                if (ingredientsCount.containsKey(ingredient)) {
                    ingredientsCount.put(ingredient, ingredientsCount.get(ingredient) + 1);
                } else {
                    ingredientsCount.put(ingredient, 1);
                }
            }
        }
        return ingredientsCount;
    }

    public String toString() {
        String asString = "";
        for (int i = 0; i < mMeals.size(); i++) {
            asString = asString + mMeals.get(i).toString();
            asString += "\n\n";
        }

        return asString;
    }

    public List getMeals(){
        return this.mMeals;
    }

    public int length(){
        return mNbMeals;
    }


}
