package com.pierrecattin.mealprep;

import java.util.HashMap;
import java.util.Map;

public class Constraints {
    public static Map<String, Integer> minByType =
            new HashMap<String, Integer>();

    public static Map<String, Integer> maxByType =
            new HashMap<String, Integer>();

    public Constraints(){
        minByType.put("Carbs", 1);
        maxByType.put("Carbs", 1);

        minByType.put("Veggies", 1);
        maxByType.put("Veggies", 2);

        minByType.put("Protein", 1);
        maxByType.put("Protein", 1);

        minByType.put("Peas", 0);
        maxByType.put("Peas", 1);

        minByType.put("Sauce", 1);
        maxByType.put("Sauce", 1);

        minByType.put("Spice", 0);
        maxByType.put("Spice", 1);


    }
}
