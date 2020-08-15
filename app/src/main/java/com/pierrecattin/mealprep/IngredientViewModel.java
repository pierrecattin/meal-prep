package com.pierrecattin.mealprep;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class IngredientViewModel extends AndroidViewModel {
    private IngredientRepository mRepository;
    private LiveData<List<Ingredient>> mAllIngredients;

    public IngredientViewModel(Application application) {
        super(application);
        mRepository=new IngredientRepository(application);
        mAllIngredients=mRepository.getAllIngredients();
    }

    LiveData<List<Ingredient>> getAllIngredients(){
        return mAllIngredients;
    }

    public void insert(Ingredient ingredient){
        mRepository.insert(ingredient);
    }

}
