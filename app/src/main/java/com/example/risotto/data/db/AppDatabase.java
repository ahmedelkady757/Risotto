package com.example.risotto.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.risotto.data.db.converter.IngredientListConverter;
import com.example.risotto.data.db.dao.FavoriteDao;
import com.example.risotto.data.db.entity.FavoriteMealEntity;

@Database(entities = {FavoriteMealEntity.class}, version = 1, exportSchema = false)
@TypeConverters({IngredientListConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract FavoriteDao favoriteDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "risotto_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
