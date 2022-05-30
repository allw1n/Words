package com.example.words.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.example.words.room.Word;
import com.example.words.room.WordDao;
import com.example.words.room.WordDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WordRepository {

    private final WordDao wordDao;
    private final LiveData<List<Word>> wordsList;
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

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(application, word.getWord() + " inserted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    public void deleteAll() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                wordDao.deleteAll();
            }
        }).start();
    }

    public List<Word> getAWord() {
        List<Word> word = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                word.addAll(wordDao.getAWord());
            }
        });

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Word[]> callable = new Callable<Word[]>() {
            @Override
            public Word[] call() throws Exception {
                return wordDao.getAWord();
            }
        };
        Future<Word[]> future= executor.submit(callable);
        try {
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }
}