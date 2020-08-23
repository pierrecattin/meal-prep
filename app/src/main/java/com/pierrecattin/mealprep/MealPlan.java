package com.pierrecattin.mealprep;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MealPlan {
    private static final String TAG = "MealPlan";
    private List<Meal>  mMeals = new ArrayList<Meal>();
    private int mNbMeals;

    public MealPlan(){
    }

    public boolean addMeal(Meal meal){
        Map<Ingredient, Integer> ingredientsCount = this.countIngredients();
        boolean maxIngredientReached=false;

        Iterator<Ingredient> itr = meal.getIngredients().iterator();
        while (!maxIngredientReached && itr.hasNext()) {
            Ingredient ingredient = itr.next();
            if (ingredientsCount.containsKey(ingredient)) {
                maxIngredientReached = ingredientsCount.get(ingredient) >= ingredient.getMaxUsePerPlan();
            }
        }

        if (!maxIngredientReached){
            mMeals.add(meal);
            return (true);
        } else{
            return(false);
        }
    }
    public boolean fillMeals(int nbMeals, List<Ingredient> ingredients) throws Exception {
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
                newMeal.fillIngredients(ingredients);
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

    public Map<Ingredient, Integer> countIngredients(){
        Map<Ingredient, Integer> ingredientsCount =new HashMap<Ingredient, Integer>();
        for(Meal meal: mMeals){
            for (Ingredient ingredient:meal.getIngredients()){
                if(ingredientsCount.containsKey(ingredient)){
                    ingredientsCount.put(ingredient, ingredientsCount.get(ingredient)+1);
                } else {
                    ingredientsCount.put(ingredient, 1);
                }
            }
        }
        return ingredientsCount;
    }


}
