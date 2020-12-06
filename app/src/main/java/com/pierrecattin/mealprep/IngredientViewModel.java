package com.pierrecattin.mealprep;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class IngredientViewModel extends AndroidViewModel {
    private IngredientRepository mRepository;
    private LiveData<List<Ingredient>> mAllIngredients;
    private LiveData<List<Ingredient>> mAvailableIngredients; // not required and not forbidden
    private LiveData<List<Ingredient>> mRequiredIngredients;
    private LiveData<List<Ingredient>> mForbiddenIngredients;

    public IngredientViewModel(Application application) {
        super(application);
        mRepository=new IngredientRepository(application);
        mAllIngredients=mRepository.getAllIngredients();
        mAvailableIngredients=mRepository.getAvailableIngredients();
        mRequiredIngredients=mRepository.getRequiredIngredients();
        mForbiddenIngredients=mRepository.getForbiddenIngredients();
    }

    LiveData<List<Ingredient>> getAllIngredients(){
        return mAllIngredients;
    }

    LiveData<List<Ingredient>> getAvailableIngredients(){
        return mAvailableIngredients;
    }
    LiveData<List<Ingredient>> getRequiredIngredients(){
        return mRequiredIngredients;
    }
    LiveData<List<Ingredient>> getForbiddenIngredients(){
        return mForbiddenIngredients;
    }

    public void insert(Ingredient ingredient){
        mRepository.insert(ingredient);
    }

    public void update(Ingredient ingredient) {mRepository.update(ingredient);}

    public void resetRequired(){
        mRepository.resetRequired();
    }

    public void resetForbidden(){
        mRepository.resetForbidden();
    }

}
