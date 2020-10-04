package com.pierrecattin.mealprep;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.endsWith;
import static org.apache.commons.lang3.StringUtils.remove;
import static org.apache.commons.lang3.StringUtils.replace;
import static org.apache.commons.lang3.StringUtils.startsWith;
import static org.apache.commons.lang3.StringUtils.substring;
import static org.apache.commons.lang3.StringUtils.substringsBetween;

@Entity(tableName = "ingredient_table")
public class Ingredient {
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

    @Ignore
    private Set<String> mStyles = new HashSet<String>();

    public Ingredient(@NonNull String name, @NonNull String type, String stylesString, @NonNull int maxUsePerPlan){
        this.setName(name);
        this.setType(type);
        this.setStylesString(stylesString);
        this.fillStylesFromString();
        this.setMaxUsePerPlan(maxUsePerPlan);
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

    private void fillStylesFromString(){
        if(mStylesString != null){
            //Log.i(TAG, "stylesString:"+stylesString+"---------------------------");
            String formattedStylesString = mStylesString;
            formattedStylesString = remove(formattedStylesString, " "); // remove all spaces

            if(endsWith(formattedStylesString, ";")){
                formattedStylesString = substring(formattedStylesString, 0, formattedStylesString.length()-1); // remove final ;
            }
            if(startsWith(formattedStylesString, ";")){
                formattedStylesString = substring(formattedStylesString, 1, formattedStylesString.length()); // remove initial ;
            }
            // replace semi colons so that styles are surrounded by [ ]
            formattedStylesString = replace(formattedStylesString, ";", "][");
            formattedStylesString = "["+formattedStylesString+"]";

            //Log.i(TAG, stylesString);
            String[] stylesArray = substringsBetween(formattedStylesString, "[","]");

            for (int i=0; i<stylesArray.length; i++){
                this.addStyle(stylesArray[i]);
            }
        }

    }

    public Set<String> getStyles(){
        return(mStyles);
    }

    public void addStyle(String style){
        mStyles.add(style);
    }

    public String toString(){
        if(mStyles.size()==0){
            return(mName +"; "+ mType);
        } else {
            return(mName +"; "+ mType +"; " + mStyles.toString());
        }
    }

}
