package com.pierrecattin.mealprep;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class IngredientRepository {
    private IngredientDao mIngredientDao;
    private LiveData<List<Ingredient>> mAllIngredients;
    private LiveData<List<Ingredient>> mRequiredIngredients;
    private LiveData<List<Ingredient>> mNotRequiredIngredients;
    IngredientRepository(Application application){
        IngredientRoomDatabase db = IngredientRoomDatabase.getDatabase(application);
        mIngredientDao = db.ingredientDao();
        mAllIngredients = mIngredientDao.getAllIngredients();
        mRequiredIngredients = mIngredientDao.getByRequired(true);
        mNotRequiredIngredients = mIngredientDao.getByRequired(false);
    }

    LiveData<List<Ingredient>>getAllIngredients(){
        return mAllIngredients;
    }
    LiveData<List<Ingredient>>getRequiredIngredients(){
        return mRequiredIngredients;
    }
    LiveData<List<Ingredient>>getNotRequiredIngredients(){
        return mNotRequiredIngredients;
    }

    public void insert (Ingredient ingredient){
        new insertAsyncTask(mIngredientDao).execute(ingredient);
    }

    public void update(Ingredient ingredient)  {
        new updateIngredientAsyncTask(mIngredientDao).execute(ingredient);
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

    private static class updateIngredientAsyncTask extends AsyncTask<Ingredient, Void, Void> {
        private IngredientDao mAsyncTaskDao;

        updateIngredientAsyncTask(IngredientDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Ingredient... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}
