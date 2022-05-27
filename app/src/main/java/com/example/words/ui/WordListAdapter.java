package com.example.words.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.words.R;
import com.example.words.room.Word;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    private Context context;
    private List<Word> wordList;

    WordListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WordViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.recyclerview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        if (wordList != null) {
            Word currentWord = wordList.get(position);
            holder.viewWord.setText(currentWord.getWord());
        } else {
            holder.viewWord.setText(R.string.no_words);
        }
    }

    void setWords(List<Word> wordList) {
        this.wordList = wordList;
        //notifyItemRangeInserted(0, wordList.size());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (wordList != null) return wordList.size();
        else return 0;
    }

    static class WordViewHolder extends RecyclerView.ViewHolder {

        private final MaterialTextView viewWord;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);

            this.viewWord = itemView.findViewById(R.id.viewWord);
        }
    }
}
