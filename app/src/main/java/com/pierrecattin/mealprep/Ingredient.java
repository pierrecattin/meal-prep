package com.pierrecattin.mealprep;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.HashSet;
import java.util.Set;

@Entity(tableName = "ingredient_table")
public class Ingredient {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="name")
    private String name;

    @NonNull
    @ColumnInfo(name="type")
    private String type;

    @ColumnInfo(name="stylesString")
    private String stylesString;
    //private Set styles = new HashSet<String>();

    @Ignore
    public Ingredient(String name, String type){
        this.setName(name);
        this.setType(type);
    }

    public Ingredient(@NonNull String name, @NonNull String type, String stylesString){
        this.setName(name);
        this.setType(type);
        this.setStylesString(stylesString);
    }

    private void setStylesString(String stylesString) {
        this.stylesString = stylesString;
    }

    /*@Ignore
    public Ingredient(String name, String type, Set<String> styles){
        this.setName(name);
        this.setType(type);
        for (String style : styles) this.addStyle(style);
    }*/

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

    public String getStylesString() {
        return(this.stylesString);
    }

    /*private void setStylesString(String stylesString) {
        this.stylesString=stylesString;
    }*/

    /*public Set getStyles(){
        return(styles);
    } */

    /* public boolean addStyle(String style){
        if(IngredientProperties.styles.contains(style)){
            styles.add(style);
            return(true);
        } else {
            return(false);
        }
    } */

    public String toString(){
        //if(styles.size()==0){
            return(name+"; "+type+"; "+stylesString);
        //} else {
        //    return(name+"; "+type+"; " + styles.toString());
        //}
    }

}
