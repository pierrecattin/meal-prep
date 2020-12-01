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
    private LiveData<List<Ingredient>> mNotRequiredIngredients;

    public IngredientViewModel(Application application) {
        super(application);
        mRepository=new IngredientRepository(application);
        mAllIngredients=mRepository.getAllIngredients();
        mRequiredIngredients=mRepository.getRequiredIngredients();
        mNotRequiredIngredients=mRepository.getNotRequiredIngredients();
    }

    LiveData<List<Ingredient>> getAllIngredients(){
        return mAllIngredients;
    }
    LiveData<List<Ingredient>> getRequiredIngredients(){
        return mRequiredIngredients;
    }
    LiveData<List<Ingredient>> getNotRequiredIngredients(){
        return mNotRequiredIngredients;
    }

    public void insert(Ingredient ingredient){
        mRepository.insert(ingredient);
    }

    public void update(Ingredient ingredient) {mRepository.update(ingredient);}

}
