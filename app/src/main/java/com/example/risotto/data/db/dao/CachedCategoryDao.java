package com.example.risotto.data.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.risotto.data.db.entity.CachedCategoryEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface CachedCategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertCategories(List<CachedCategoryEntity> categories);

    @Query("SELECT * FROM cached_categories")
    Flowable<List<CachedCategoryEntity>> getAllCategories();
}
