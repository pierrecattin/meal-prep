package com.pierrecattin.mealprep;

import android.util.Log;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Meal {
    private static final String TAG = "Meal";
    private Set<Ingredient> mIngredients =new HashSet<Ingredient>();
    private String mStatus;

    private Set<String> mStyles =new HashSet<String>(); // Could be deleted

    public Meal(){
        Log.i(TAG, "Create meal");
        mStatus="Uninitialized";
    }
    public boolean addIngredient(Ingredient ingredient){
        // Check that ingredient is not already in meal
        if(mIngredients.contains(ingredient)){
            return(false);
        }

        // Check that the max value for ingredient type hasn't been reached
        Constraints constraints = new Constraints();
        if(constraints.maxByType.containsKey(ingredient.getType())){
            if(this.countType(ingredient.getType()) >= constraints.maxByType.get(ingredient.getType())){
                return (false);
            }
        }

        // Check that ingredient has a style that's shared by all ingredients
        if(ingredient.getStyles().size()>0 & this.getStyles().size()>0){
            Set commonStyles = ingredient.getStyles();
            commonStyles.retainAll(this.getCommonStyles());
            if (commonStyles.size()==0){
                return(false);
            }
        }
        // Log.i(TAG, "!!!ADDING " + ingredient.getName());
        mIngredients.add(ingredient);
        addStyles(ingredient.getStyles());
        // Log.i(TAG, "Styles currently in meal:");
        // Log.i(TAG, mStyles.toString());
        return(true);
    }

    public boolean fillIngredients(List<Ingredient> ingredients){
        Log.i(TAG, "fillIngredients: ");
        int maxTrial = 1000;
        int trialCount = 0;
        while (!this.allTypesMinAchieved() & trialCount<maxTrial){
            Random rand = new Random();
            this.addIngredient(ingredients.get(rand.nextInt(ingredients.size())));
            trialCount ++;
        }
        if(this.allTypesMinAchieved()){
            this.mStatus ="Meal found in " + trialCount + " trials" + "\n\n";
            return true;
        } else {
            this.mStatus = "No valid meal found after "+trialCount+" trials";
            return false;
        }

    }


    public String getStatus(){
        return mStatus;
    }

    public Set<Ingredient> getIngredients(){
        return(mIngredients);
    }

    public int countType(String type){
        int count=0;
        //Log.v(TAG, "countType: counting number of "+type);
        for (Ingredient ingredient : mIngredients){
            //Log.v(TAG, "countType: type of "+ingredient.getName()+": "+ingredient.getType());
            if(ingredient.getType().equals(type)){
                count++;
            }
        }
        return count;
    }

    private void addStyles(Set<String> styles){
        //Log.i(TAG, "addStyles: "+styles.toString());
        for(String style : styles){
            this.mStyles.add(style);
        }
        //Log.i(TAG, "Current styles"+this.mStyles);
    }
    public Set<String> getStyles(){
        return(mStyles);
    }

    // Finds styles that are shared by all ingredients
    public Set<String> getCommonStyles(){
        Iterator<Ingredient> itr = mIngredients.iterator();
        Set<String> commonStyles = new HashSet<String>();
        // Fill commonStyles with styles of first ingredient that has styles
        while(itr.hasNext() & commonStyles.isEmpty()){
            Ingredient ingredient=itr.next();
            if(!ingredient.getStyles().isEmpty()){
                commonStyles=ingredient.getStyles();
            }
        }

        // Keep iterating to retain only common styles
        while(itr.hasNext()){
            Ingredient ingredient=itr.next();
            commonStyles.retainAll(ingredient.getStyles());
        }
        return commonStyles;
    }

    public boolean allTypesMinAchieved(){
        Constraints constraints = new Constraints();
        Set<String> types = constraints.minByType.keySet();
        //Log.i(TAG, "types:"+types);
        Iterator<String> itr = types.iterator();
        while (itr.hasNext()){
            String type=itr.next();
            //Log.i(TAG, "allTypesMinAchieved; checking min for Type "+ type+"; current number="+this.countType(type) +" / " + constraints.minByType.get(type));
            if (this.countType(type) < constraints.minByType.get(type)){
                return(false);
            }
        }
        return(true);
    }

    public String toString(){
        String strOutput = "";
        for(String type:IngredientProperties.types){
            strOutput += type + ": ";
            for (Ingredient ingredient : mIngredients){
                if(ingredient.getType().equals(type)){
                    strOutput += (ingredient.getName() + "; ");
                }
            }
            strOutput +=  "\n";
        }
        strOutput += "Style(s): ";
        strOutput += this.getCommonStyles().toString();
        strOutput +=  "\n\n";
        return(strOutput);
    }

}
