package com.pierrecattin.mealprep;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class IngredientRepository {
    private IngredientDao mIngredientDao;
    private LiveData<List<Ingredient>> mAllIngredients;
    IngredientRepository(Application application){
        IngredientRoomDatabase db = IngredientRoomDatabase.getDatabase(application);
        mIngredientDao = db.ingredientDao();
        mAllIngredients = mIngredientDao.getAllIngredients();
    }

    LiveData<List<Ingredient>>getAllIngredients(){
        return mAllIngredients;
    }

    public void intert (Ingredient ingredient){
        new insertAsyncTask(mIngredientDao).execute(ingredient);
    }

    private static class insertAsyncTask extends AsyncTask<Ingredient, Void, Void> {
        private IngredientDao mAsyncTaskDao;
        insertAsyncTask(IngredientDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Ingredient... params){
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
