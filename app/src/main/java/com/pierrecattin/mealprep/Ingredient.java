package com.pierrecattin.mealprep;


import java.util.HashSet;
import java.util.Set;

public class Ingredient {
    private String name;
    private String type;
    private Set<String> styles = new HashSet<String>();

    public Ingredient(){
    }
    public Ingredient(String name, String type){
        this.setName(name);
        this.setType(type);
    }

    public Ingredient(String name, String type, Set<String> styles){
        this.setName(name);
        this.setType(type);
        for (String style : styles) this.addStyle(style);
    }

    public String getName(){
        return(name);
    }
    public boolean setName(String name){
        this.name = name;
        return(true);
    }

    public String getType(){
        return(type);
    }
    public boolean setType(String type){
        if(IngredientProperties.types.contains(type)){
            this.type = type;
            return(true);
        } else {
            return(false);
        }
    }

    public Set getStyles(){
        return(styles);
    }
    public boolean addStyle(String style){
        if(IngredientProperties.styles.contains(style)){
            styles.add(style);
            return(true);
        } else {
            return(false);
        }
    }

    public String toString(){
        if(styles.size()==0){
            return(name+"; "+type);
        } else {
            return(name+"; "+type+"; " + styles.toString());
        }

    }

}
