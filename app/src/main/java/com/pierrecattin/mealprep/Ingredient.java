package com.pierrecattin.mealprep;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.endsWith;
import static org.apache.commons.lang3.StringUtils.remove;
import static org.apache.commons.lang3.StringUtils.replace;
import static org.apache.commons.lang3.StringUtils.startsWith;
import static org.apache.commons.lang3.StringUtils.substring;
import static org.apache.commons.lang3.StringUtils.substringsBetween;

@Entity(tableName = "ingredients")
public class Ingredient implements Serializable, Comparable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="name")
    private String mName;

    @NonNull
    @ColumnInfo(name="type")
    private String mType;

    @ColumnInfo(name="styles_string")
    private String mStylesString;

    @NonNull
    @ColumnInfo(name="max_use_per_plan")
    private Integer mMaxUsePerPlan;

    @NonNull
    @ColumnInfo(name="is_required")
    private boolean mRequired;

    @Ignore
    private Set<String> mStyles = new HashSet<String>();

    public Ingredient(@NonNull String name, @NonNull String type, String stylesString, @NonNull int maxUsePerPlan, boolean required){
        this.setName(name);
        this.setType(type);
        this.setStylesString(stylesString);
        this.fillStylesFromString(stylesString);
        this.setMaxUsePerPlan(maxUsePerPlan);
        this.setRequired(required);
        //Log.i(TAG, "Creating Ingredient "+mName+"; "+mType+"; "+this.getStyles().toString());
    }


    public void setName(String name){
        mName = name;
    }
    public void setType(String type){
        mType = type;
    }
    private void setStylesString(String stylesString) {
        mStylesString = stylesString;
    }
    private void setMaxUsePerPlan(Integer maxUsePerPlan) {
        mMaxUsePerPlan = maxUsePerPlan;
    }
    public void setRequired(boolean required){
        mRequired = required;
    }

    public String getName(){
        return(mName);
    }
    public String getType(){
        return(mType);
    }
    public String getStylesString() {
        return(mStylesString);
    }
    public Integer getMaxUsePerPlan(){ return (mMaxUsePerPlan);}
    public boolean getRequired(){ return (mRequired);}

    private void fillStylesFromString(String stylesString){
        if(stylesString != null){
            //Log.i(TAG, "stylesString:"+stylesString+"---------------------------");
            stylesString = remove(stylesString, " "); // remove all spaces

            if(endsWith(stylesString, ";")){
                stylesString = substring(stylesString, 0, stylesString.length()-1); // remove final ;
            }
            if(startsWith(stylesString, ";")){
                stylesString = substring(stylesString, 1, stylesString.length()); // remove initial ;
            }
            // replace semi colons so that styles are surrounded by [ ]
            stylesString = replace(stylesString, ";", "][");
            stylesString = "["+stylesString+"]";

            //Log.i(TAG, stylesString);
            String[] stylesArray = substringsBetween(stylesString, "[","]");

            for (int i=0; i<stylesArray.length; i++){
                this.addStyle(stylesArray[i]);
            }
        }

    }

    public Set<String> getStyles(){
        this.fillStylesFromString(mStylesString);
        return(mStyles);
    }

    public void addStyle(String style){
        mStyles.add(style);
    }

    public String toString(){
        return(mName);
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof Ingredient){
            Ingredient otherIngredient = (Ingredient)other;
            return(otherIngredient.getName().equals(this.getName()) &&
                    otherIngredient.getStylesString().equals(this.getStylesString()) &&
                    otherIngredient.getType().equals(this.getType())&&
                    otherIngredient.getMaxUsePerPlan().equals(this.getMaxUsePerPlan()));
        } else{
            return false;
        }
    }

    @Override
    public final int hashCode() {
        int result = 17;
        if (mName != null) {
            result = 31 * result + mName.hashCode();
        }
        if (mStylesString != null) {
            result = 31 * result + mStylesString.hashCode();
        }
        if (mType != null) {
            result = 31 * result + mType.hashCode();
        }

        if (mMaxUsePerPlan != null) {
            result = 31 * result + mMaxUsePerPlan.hashCode();
        }
        return result;
    }
    @Override
    public int compareTo(Object otherObject) {
        if(otherObject instanceof Ingredient){
            Ingredient otherIngredient = (Ingredient)otherObject;
            return(this.getName().compareTo(otherIngredient.getName()));
        } else {
            return (-1);
        }
    }

}
