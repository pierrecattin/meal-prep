package com.pierrecattin.mealprep;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Set;

@Dao
public interface IngredientDao {
    @Insert
    void insert(Ingredient ingredient);

    @Query("DELETE FROM ingredient_table")
    void deleteAll();

    @Query("SELECT * from ingredient_table")
    Set<Ingredient> getAllIngredients();
}
