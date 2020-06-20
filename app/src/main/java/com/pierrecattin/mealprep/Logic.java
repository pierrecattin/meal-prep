package com.pierrecattin.mealprep;

import java.util.Random;

public class Logic {
    protected InputActivity out;
    public Logic(InputActivity out){
        this.out=out;
    }

    public void process(){
        Ingredient[] ingredients =  new Ingredient[3];
        ingredients[0] = new Ingredient("Pasta", "Carbs");
        ingredients[1] = new Ingredient("Broccoli", "Veggies");
        ingredients[2] = new Ingredient("Tofu", "Protein");
        ingredients[1].addStyle("Asian");
        ingredients[1].addStyle("European");
        ingredients[2].addStyle("Asian");


        Random rand = new Random();

        out.print(ingredients[rand.nextInt(3)].toString());
    }

}
