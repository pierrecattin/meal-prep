package com.pierrecattin.mealprep;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class IngredientRepository {
    private IngredientDao mIngredientDao;
    private LiveData<List<Ingredient>> mAllIngredients;
    private LiveData<List<Ingredient>> mRequiredIngredients;
    IngredientRepository(Application application){
        IngredientRoomDatabase db = IngredientRoomDatabase.getDatabase(application);
        mIngredientDao = db.ingredientDao();
        mAllIngredients = mIngredientDao.getAllIngredients();
        mRequiredIngredients = mIngredientDao.getRequired();
    }

    LiveData<List<Ingredient>>getAllIngredients(){
        return mAllIngredients;
    }
    LiveData<List<Ingredient>>getRequiredIngredients(){
        return mRequiredIngredients;
    }

    public void insert (Ingredient ingredient){
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

    private static class makeRequiredAsyncTask extends AsyncTask<Ingredient, Void, Void> {
        private IngredientDao mAsyncTaskDao;

        makeRequiredAsyncTask(IngredientDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Ingredient... params) {
            mAsyncTaskDao.makeRequired(params[0].getName());
            return null;
        }
    }
    public void makeRequired(Ingredient ingredient)  {
        new makeRequiredAsyncTask(mIngredientDao).execute(ingredient);
    }




}
