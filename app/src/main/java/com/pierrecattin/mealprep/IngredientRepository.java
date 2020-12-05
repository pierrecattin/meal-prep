package com.pierrecattin.mealprep;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class IngredientRepository {
    private IngredientDao mIngredientDao;
    private LiveData<List<Ingredient>> mAllIngredients;
    private LiveData<List<Ingredient>> mAvailableIngredients; // not required and not forbidden
    private LiveData<List<Ingredient>> mRequiredIngredients;
    private LiveData<List<Ingredient>> mForbiddenIngredients;

    IngredientRepository(Application application){
        IngredientRoomDatabase db = IngredientRoomDatabase.getDatabase(application);
        mIngredientDao = db.ingredientDao();
        mAllIngredients = mIngredientDao.getAllIngredients();
        mAvailableIngredients = mIngredientDao.getByRequiredAndForbidden(false, false);
        mRequiredIngredients = mIngredientDao.getByRequired(true);
        mForbiddenIngredients = mIngredientDao.getByForbidden(true);
    }

    LiveData<List<Ingredient>>getAllIngredients(){
        return mAllIngredients;
    }
    LiveData<List<Ingredient>>getAvailableIngredients(){
        return mAvailableIngredients;
    }
    LiveData<List<Ingredient>>getRequiredIngredients(){
        return mRequiredIngredients;
    }
    LiveData<List<Ingredient>>getForbiddenIngredients(){
        return mForbiddenIngredients;
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
