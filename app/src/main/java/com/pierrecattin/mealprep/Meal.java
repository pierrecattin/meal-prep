package com.pierrecattin.mealprep;

import android.util.Log;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Meal {
    private static final String TAG = "Meal";
    private Set<Ingredient> mIngredients =new HashSet<Ingredient>();
    private Set<String> mStyles =new HashSet<String>();

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

        // Check that ingredients has a style that's compatible with all other ingredients
        Log.i(TAG, "#################################");
        Log.i(TAG, "Trying new ingredient: " + ingredient.toString());
        Log.i(TAG, ingredient.getStylesString());
        Log.i(TAG, "Styles currently in meal:");
        Log.i(TAG, mStyles.toString());
        if(ingredient.getStyles().size()>0 & this.getStyles().size()>0){
            Set commonStyles;
            for (Ingredient otherIngredient : mIngredients){
                Log.i(TAG, "----------- Other ingredient: "+otherIngredient.toString());
                Log.i(TAG, "----------- styles: "+otherIngredient.getStyles().toString());
                if(otherIngredient.getStyles().size()>0){ //ISSUE: otherIngredient.getStyles is sometimes empty when otherIngredient actually has styles
                    commonStyles=otherIngredient.getStyles();
                    commonStyles.retainAll(ingredient.getStyles());
                    Log.i(TAG, "----------- commonstyles:"+commonStyles.toString());
                    if (commonStyles.size()==0){
                        return(false);
                    }
                }
            }
        }
        Log.i(TAG, "!!!ADDING " + ingredient.getName());
        mIngredients.add(ingredient);
        addStyles(ingredient.getStyles());
        return(true);

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
            strOutput += type + ": " + "\n";
            for (Ingredient ingredient : mIngredients){
                if(ingredient.getType().equals(type)){
                    strOutput += (ingredient.getName() + "; ");
                }
            }
            strOutput +=  "\n\n";
        }
        strOutput +=  "\n\n";
        strOutput += "Style(s): ";
        strOutput += this.mStyles.toString();
        return(strOutput);
    }

}
