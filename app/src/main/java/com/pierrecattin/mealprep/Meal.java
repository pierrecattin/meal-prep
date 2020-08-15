package com.pierrecattin.mealprep;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Meal {
    private Set<Ingredient> ingredients=new HashSet<Ingredient>();

    public boolean addIngredient(Ingredient ingredient){
        // Check that ingredient is not already in meal
        if(ingredients.contains(ingredient)){
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
        /*if(ingredient.getStyles().size()>0 & this.getStyles().size()>0){
            Set commonStyles;
            for (Ingredient otherIngredient : ingredients){
                if(otherIngredient.getStyles().size()>0){
                    commonStyles=otherIngredient.getStyles();
                    commonStyles.retainAll(ingredient.getStyles());
                    if (commonStyles.size()==0){
                        return(false);
                    }
                }
            }
        } */
        ingredients.add(ingredient);
        return(true);

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

    /*public Set<String> getStyles(){
        Set<String> styles=new HashSet<String>();
        for (Ingredient ingredient : ingredients){
            styles.addAll(ingredient.getStyles());
        }
        return(styles);
    }*/

    public boolean allTypesMinAchieved(){
        Constraints constraints = new Constraints();
        Set<String> types = constraints.minByType.keySet();
        Iterator<String> itr = types.iterator();
        while (itr.hasNext()){
            String type=itr.next();
            if (this.countType(type)<constraints.minByType.get(type)){
                return(false);
            }
        }
        return(true);
    }

    public String toString(){
        String strOutput = "";
        for(String type:IngredientProperties.types){
            strOutput += type + ": " + "\n";
            for (Ingredient ingredient : ingredients){
                if(ingredient.getType()==type){
                    strOutput += (ingredient.getName() + "; ");
                }
            }
            strOutput +=  "\n\n";
        }
        strOutput +=  "\n\n";
        //strOutput += "Style(s): ";
        //strOutput += this.getStyles().toString();
        return(strOutput);
    }

}
