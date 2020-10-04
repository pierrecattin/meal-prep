package com.pierrecattin.mealprep;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Meal {
    private static final String TAG = "Meal";
    private Set<Ingredient> mIngredients =new HashSet<Ingredient>();


    public Meal(){
    }

    // Checks if ingredients could be added without violating any Meal-Level constraint
    public boolean isAllowed(Ingredient ingredient){
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
        if(ingredient.getStyles().size()>0 && this.getCommonStyles().size() > 0){
            Set commonStyles = ingredient.getStyles();
            commonStyles.retainAll(this.getCommonStyles());
            if (commonStyles.size()==0){
                return(false);
            }
        }
        return(true);
    }

    // Add an ingredient if allowed
    public boolean addIngredient(Ingredient ingredient){
        if(isAllowed(ingredient)){
            mIngredients.add(ingredient);
            return(true);
        } else {
            return(false);
        }
    }

    // Add a set of ingredients, even if it would violate constraints
    public void forceAddIngredients(Set<Ingredient> ingredients){
        mIngredients.addAll(ingredients);
    }

    // get list of ingredients that have a specific type (if keepType==true), or that do not have a specific type (if !keepType)
    public List<Ingredient> filterIngredients(List<Ingredient> ingredients, String type, boolean keepType){
        List<Ingredient> filteredIngredients = new ArrayList<Ingredient>();
        for(Ingredient ingredient : ingredients){
            boolean keepCurrentIngredient =
                    (keepType && ingredient.getType().equals(type)) ||
                            (!keepType && !ingredient.getType().equals(type));
            if(keepCurrentIngredient){
                filteredIngredients.add(ingredient);
            }
        }
        return(filteredIngredients);
    }

    // Add ingredients until enough ingredients of type Carbs are in Meal
    public boolean fillCarbs(List<Ingredient> ingredients) throws Exception {
        ingredients = filterIngredients(ingredients, "Carbs", true);
        int maxTrial = 1000;
        int trialCount = 0;
        while (!this.typesQuantityRespected("Carbs", "min") && trialCount < maxTrial) {
            Random rand = new Random();
            Ingredient newIngredient = ingredients.get(rand.nextInt(ingredients.size()));
            this.addIngredient(newIngredient);
            trialCount++;
        }
        if (this.typesQuantityRespected("Carbs", "min")) {
            return true;
        } else {
            return false;
        }
    }

    // Add ingredients until all other types than Carbs are filled
    public boolean fillSauce(List<Ingredient> ingredients) throws Exception {
        ingredients = filterIngredients(ingredients, "Carbs", false);
        int maxTrial = 1000;
        int trialCount = 0;
        while (!this.typesQuantityRespected("All but carbs", "min") & trialCount < maxTrial) {
            Random rand = new Random();
            Ingredient newIngredient = ingredients.get(rand.nextInt(ingredients.size()));
            this.addIngredient(newIngredient);
            trialCount++;
        }
        if (this.typesQuantityRespected("All but carbs", "min")) {
            return true;
        } else {
            return false;
        }
    }

    // Add ingredients until all contraints are met
    public boolean fillIngredients(List<Ingredient> ingredients) throws Exception {
        boolean success=true;
        success = success && this.fillCarbs(ingredients);
        success= success && this.fillSauce(ingredients);
        return(success);
    }

    public Set<Ingredient> getCarbs(){
        Set<Ingredient> carbs = new HashSet<Ingredient>();
        for(Ingredient ingredient:mIngredients){
            if(ingredient.getType().equals("Carbs")){
                carbs.add(ingredient);
            }

        }
        return(carbs);
    }

    public Set<Ingredient> getSauce(){
        Set<Ingredient> sauce = new HashSet<Ingredient>();
        for(Ingredient ingredient:mIngredients){
            if(!ingredient.getType().equals("Carbs")){
                sauce.add(ingredient);
            }
        }
        return(sauce);
    }

    public Set<Ingredient> getIngredients(){
        return(mIngredients);
    }

    // Count number of ingredients having a given type
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

    // Find if contraints on number of ingredients by type are respected
    public boolean typesQuantityRespected(String typesToCheck, String direction) throws Exception {
        Constraints constraints = new Constraints();
        Set<String> filteredTypes;
        if(direction.equals("min")){
            filteredTypes = new HashSet<String>(constraints.minByType.keySet());
        } else if (direction.equals("max")){
            filteredTypes = new HashSet<String>(constraints.maxByType.keySet());
        } else {
            throw new Exception("direction arg has invalid value: "+direction);
        }

        if(typesToCheck.equals("All but carbs")){
            filteredTypes.remove("Carbs");
        } else if (typesToCheck.equals("Carbs")){
            filteredTypes.clear();
            filteredTypes.add("Carbs");
        } else if (!typesToCheck.equals("All")){
            throw new Exception("typesToCheck arg has invalid value: "+typesToCheck);
        }

        for (String type:filteredTypes){
            if(direction.equals("min")){
                if (this.countType(type) < constraints.minByType.get(type)){
                    return(false);
                }
            } else if (direction.equals("max")){
                if (this.countType(type) > constraints.maxByType.get(type)){
                    return(false);
                }
            }
        }
        return(true);
    }


    // Check if constraints on min/max by Ingredient type are met
    public boolean isValid() throws Exception {
        if(!this.typesQuantityRespected("All", "min")){
            return false;
        }

        if(!this.typesQuantityRespected("All", "max")){
            return false;
        }
        return true;
    }

    public String toString(){
        String strOutput = "";
        boolean typeHasIngredients;
        for(String type:Constraints.minByType.keySet()){
            typeHasIngredients = false;
            for (Ingredient ingredient : mIngredients){
                if(ingredient.getType().equals(type)){
                    if(!typeHasIngredients){
                        strOutput += type + ": ";
                        typeHasIngredients=true;
                    }
                    strOutput += (ingredient.getName() + "; ");
                }
            }
            if(typeHasIngredients){
                strOutput +=  "\n";
            }
        }
        strOutput += "Style(s): ";
        strOutput += this.getCommonStyles().toString();
        strOutput +=  "\n\n";
        return(strOutput);
    }

}
