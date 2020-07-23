package com.pierrecattin.mealprep;

import java.util.HashSet;
import java.util.Set;

public class Meal {
    private Set<Ingredient> ingredients=new HashSet<Ingredient>();

    public boolean addIngredient(Ingredient ingredient){
        if(ingredients.contains(ingredient)){
            return(false);
        } else {
            ingredients.add(ingredient);
            return(true);
        }
    }

    public Set<Ingredient> getIngredients(){
        return(ingredients);
    }

    public int countType(String type){
        int count=0;
        for (Ingredient ingredient : ingredients){
            if(ingredient.getType() == type){
                count++;
            }
        }
        return count;
    }

    public Set<String> getStyles(){
        Set<String> styles=new HashSet<String>();
        for (Ingredient ingredient : ingredients){
            styles.addAll(ingredient.getStyles());
        }
        return(styles);
    }

    public String toString(){
        String strOutput = "Ingredients: ";
        for (Ingredient ingredient : ingredients){
            strOutput += (ingredient.getName() + "; ");
        }
        strOutput += "\n";
        strOutput += "Ingredient types: ";
        for(String type:IngredientProperties.types){
            strOutput += (type + "=" + this.countType(type) + "; ");
        }
        strOutput += "\n";
        strOutput += "Style(s): ";
        strOutput += this.getStyles().toString();
        return(strOutput);
    }

}
