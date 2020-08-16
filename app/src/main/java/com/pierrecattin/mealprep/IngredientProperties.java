package com.pierrecattin.mealprep;
import java.util.HashSet;
import java.util.Set;

public class IngredientProperties {
    public static Set<String> types=new HashSet<String>();

    public IngredientProperties(){
        types.add("Carbs");
        types.add("Protein");
        types.add("Peas");
        types.add("Veggies");
        types.add("Spice");
        types.add("Sauce");
    }
}


