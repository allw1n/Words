package com.example.words.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import androidx.annotation.NonNull;

@Entity(tableName = "word_table")
public class Word {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "word")
    private String word;

    public Word(@NonNull String word) {
        this.word = word;
    }

    @NonNull
    public String getWord() {
        return word;
    }
}
