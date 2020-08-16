package com.pierrecattin.mealprep;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities={Ingredient.class}, version=1, exportSchema = false)
public abstract class IngredientRoomDatabase extends RoomDatabase {
    public abstract IngredientDao ingredientDao();

    private static IngredientRoomDatabase INSTANCE;
    static IngredientRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null) {
            synchronized (IngredientRoomDatabase.class){
                if (INSTANCE== null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            IngredientRoomDatabase.class, "ingredient_database")
                            .fallbackToDestructiveMigration()
                            .createFromAsset("database/ingredient_database.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
