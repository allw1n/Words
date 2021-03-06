package com.example.words.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Word.class}, version = 1, exportSchema = false)
public abstract class WordDatabase extends RoomDatabase {

    private static final RoomDatabase.Callback populateCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    WordDao dao;
                    dao = INSTANCE.wordDao();

                    if (dao.getAWord().length == 0) {
                        String[] words = {"like", "a", "somebody"};
                        for (String word : words) {
                            dao.insertWord(new Word(word));
                        }
                    }
                }
            }).start();
        }
    };

    private static final String DB_NAME = "word_database";

    public abstract WordDao wordDao();

    private static volatile WordDatabase INSTANCE;

    public static WordDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WordDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WordDatabase.class, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(populateCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}