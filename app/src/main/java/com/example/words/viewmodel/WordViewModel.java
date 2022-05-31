package com.example.words.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.words.repository.WordRepository;
import com.example.words.room.Word;

import java.util.List;

public class WordViewModel extends AndroidViewModel {

    private final WordRepository wordRepository;

    private final LiveData<List<Word>> wordsList;

    public WordViewModel(@NonNull Application application) {
        super(application);
        wordRepository = new WordRepository(application);
        wordsList = wordRepository.getWordsList();
    }

    public LiveData<List<Word>> getWordsList() {
        return wordsList;
    }

    public void insertWord(Word word) {
        wordRepository.insertWord(word);
    }

    public void deleteAll() {
        wordRepository.deleteAll();
    }

    public Word[] getAWord() {
        return wordRepository.getAWord();
    }

    public void deleteAWord(Word word) {
        wordRepository.deleteAWord(word);
    }
}
