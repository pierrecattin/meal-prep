package com.pierrecattin.mealprep;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

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

    @Query("SELECT * from ingredients where is_required")
    LiveData<List<Ingredient>> getRequired();

	//TODO:replace by update https://developer.android.com/codelabs/android-training-room-delete-data?index=..%2F..%2Fandroid-training#8
    @Query("UPDATE ingredients SET is_required = 1 where name=:name")
    void makeRequired(String name);

    @Query("UPDATE ingredients SET is_required = 0 where name=:name")
    void makeNotRequired(String name);

}
