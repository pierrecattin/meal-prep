package com.pierrecattin.mealprep;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface IngredientDao {
    @Insert
    void insert(Ingredient ingredient);

    @Query("DELETE FROM ingredients")
    void deleteAll();

    @Query("SELECT * from ingredients")
    LiveData<List<Ingredient>> getAllIngredients();

    @Query("SELECT * from ingredients where name=:name")
    LiveData<List<Ingredient>> getByName(String name);

    @Query("SELECT * from ingredients where is_required=:required")
    LiveData<List<Ingredient>> getByRequired(boolean required);

    @Query("SELECT * from ingredients where is_forbidden=:forbidden")
    LiveData<List<Ingredient>> getByForbidden(boolean forbidden);

    @Update
    void update(Ingredient... ingredient);

}
