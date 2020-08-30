package com.pierrecattin.mealprep;


import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MealPlan {
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

    public boolean makePlan(int nbMeals, List<Ingredient> ingredients) throws Exception {
        mNbMeals = nbMeals;

        Random rand = new Random();
        int maxTrial = 1000;
        int trialCount = 0;

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
                List<Ingredient> availableIngredients = new ArrayList<Ingredient>(ingredients);

                mMeals.get(0).fillCarbs(ingredients);
                mMeals.get(0).fillSauce(ingredients);

                availableIngredients.removeAll(mMeals.get(0).getIngredients());

                // two options of structure
                if (rand.nextInt(2) == 0) {
                    mMeals.get(2).forceAddIngredients(mMeals.get(0).getCarbs());
                    mMeals.get(2).fillSauce(availableIngredients);

                    mMeals.get(1).forceAddIngredients(mMeals.get(2).getSauce());
                    mMeals.get(1).fillCarbs(availableIngredients);
                } else {
                    mMeals.get(2).forceAddIngredients(mMeals.get(0).getSauce());
                    mMeals.get(2).fillCarbs(availableIngredients);

                    mMeals.get(1).forceAddIngredients(mMeals.get(2).getCarbs());
                    mMeals.get(1).fillSauce(availableIngredients);
                }

                trialCount++;
            }
        } else if (nbMeals == 4) {
            while (trialCount < maxTrial && !isValid()) {
                emptyMeals();
                List<Ingredient> availableIngredients = new ArrayList<Ingredient>(ingredients);

                mMeals.get(0).fillCarbs(ingredients);
                mMeals.get(0).fillSauce(ingredients);
                availableIngredients.removeAll(mMeals.get(0).getIngredients());

                mMeals.get(3).forceAddIngredients(mMeals.get(0).getCarbs());
                mMeals.get(3).fillSauce(availableIngredients);

                mMeals.get(1).forceAddIngredients(mMeals.get(3).getSauce());
                mMeals.get(1).fillCarbs(availableIngredients);
                availableIngredients.removeAll(mMeals.get(1).getCarbs());

                mMeals.get(2).forceAddIngredients(mMeals.get(0).getSauce());
                mMeals.get(2).fillCarbs(availableIngredients);

                trialCount++;
            }
        } else if (mNbMeals == 5) {
            while (trialCount < maxTrial && !isValid()) {
                emptyMeals();
                List<Ingredient> availableIngredients = new ArrayList<Ingredient>(ingredients);

                mMeals.get(0).fillCarbs(ingredients);
                mMeals.get(0).fillSauce(ingredients);
                availableIngredients.removeAll(mMeals.get(0).getIngredients());

                int structure = rand.nextInt(1);
                if (structure == 0) {
                    mMeals.get(4).forceAddIngredients(mMeals.get(0).getCarbs());
                    mMeals.get(4).fillSauce(availableIngredients);
                    availableIngredients.removeAll(mMeals.get(4).getSauce());

                    mMeals.get(3).forceAddIngredients(mMeals.get(0).getSauce());
                    mMeals.get(3).fillCarbs(availableIngredients);
                    availableIngredients.removeAll(mMeals.get(3).getCarbs());

                    mMeals.get(2).forceAddIngredients(mMeals.get(4).getSauce());
                    mMeals.get(2).fillCarbs(availableIngredients);
                    availableIngredients.removeAll(mMeals.get(2).getCarbs());

                    mMeals.get(1).forceAddIngredients(mMeals.get(3).getCarbs());
                    mMeals.get(1).fillSauce(availableIngredients);
                    availableIngredients.removeAll(mMeals.get(3).getSauce());
                }
                trialCount++;
            }
        } else if (mNbMeals == 6) {
            while (trialCount < maxTrial && !isValid()) {
                emptyMeals();
                List<Ingredient> availableIngredients = new ArrayList<Ingredient>(ingredients);

                mMeals.get(0).fillIngredients(ingredients);
                availableIngredients.removeAll(mMeals.get(0).getIngredients());

                int structure = rand.nextInt(1);
                if (structure == 0) {
                    mMeals.get(3).forceAddIngredients(mMeals.get(0).getSauce());
                    mMeals.get(3).fillIngredients(availableIngredients);
                    availableIngredients.removeAll(mMeals.get(3).getCarbs());

                    mMeals.get(1).forceAddIngredients(mMeals.get(3).getCarbs());
                    mMeals.get(1).fillIngredients(availableIngredients);
                    availableIngredients.removeAll(mMeals.get(1).getIngredients());

                    mMeals.get(4).forceAddIngredients(mMeals.get(0).getCarbs());
                    mMeals.get(4).fillIngredients(availableIngredients);
                    availableIngredients.removeAll(mMeals.get(4).getIngredients());

                    mMeals.get(2).forceAddIngredients(mMeals.get(4).getSauce());
                    mMeals.get(2).fillIngredients(availableIngredients);
                    availableIngredients.removeAll(mMeals.get(2).getCarbs());

                    mMeals.get(5).forceAddIngredients(mMeals.get(2).getCarbs());
                    mMeals.get(5).forceAddIngredients(mMeals.get(1).getSauce());
                }
                trialCount++;
            }
        }
        Log.i(TAG, "makePlan: status=" + isValid() + " after " + trialCount + " trials");
        return isValid();
    }


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
        }

        return asString;
    }


}
