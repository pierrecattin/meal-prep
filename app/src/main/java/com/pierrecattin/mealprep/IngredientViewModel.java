package com.pierrecattin.mealprep;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class IngredientViewModel extends AndroidViewModel {
    private IngredientRepository mRepository;
    private LiveData<List<Ingredient>> mAllIngredients;
    private LiveData<List<Ingredient>> mRequiredIngredients;

    public IngredientViewModel(Application application) {
        super(application);
        mRepository=new IngredientRepository(application);
        mAllIngredients=mRepository.getAllIngredients();
        mRequiredIngredients=mRepository.getRequiredIngredients();
    }

    LiveData<List<Ingredient>> getAllIngredients(){
        return mAllIngredients;
    }
    LiveData<List<Ingredient>> getRequiredIngredients(){
        return mRequiredIngredients;
    }

    public void insert(Ingredient ingredient){
        mRepository.insert(ingredient);
    }

    public void makeRequired(Ingredient ingredient) {mRepository.makeRequired(ingredient);}

}
