package com.example.words.repository;

import android.app.Activity;
import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.example.words.room.Word;
import com.example.words.room.WordDao;
import com.example.words.room.WordDatabase;

import java.util.List;

public class WordRepository {

    private WordDao wordDao;
    private LiveData<List<Word>> wordsList;
    private final Application application;

    public WordRepository(Application application) {
        this.application = application;
        WordDatabase database = WordDatabase.getDatabase(application);
        wordDao = database.wordDao();
        wordsList = wordDao.getAllWords();
    }

    public LiveData<List<Word>> getWordsList() {
        return wordsList;
    }

    public void insertWord(Word word) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                wordDao.insertWord(word);

                Activity activity = (Activity) application.getApplicationContext();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, word + " inserted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    public void deleteAll() {
        wordDao.deleteAll();
    }
}