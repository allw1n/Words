package com.example.words.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.words.R;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import com.example.words.databinding.ActivityMainBinding;
import com.example.words.room.Word;
import com.example.words.viewmodel.WordViewModel;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WordViewModel wordViewModel;

    private final ActivityResultLauncher<Intent> addNewWord = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent replyData = result.getData();
                        if (replyData != null) {
                            Word newWord = new Word(replyData.getStringExtra(AddNewActivity.EXTRA_REPLY));
                            wordViewModel.insertWord(newWord);
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.words.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        RecyclerView recyclerWord = binding.getRoot().findViewById(R.id.recyclerWord);
        recyclerWord.setLayoutManager(new LinearLayoutManager(this));
        WordListAdapter wordListAdapter = new WordListAdapter(this);
        recyclerWord.setAdapter(wordListAdapter);

        wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        wordViewModel.getWordsList().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@NonNull final List<Word> wordList) {
                wordListAdapter.setWords(wordList);
            }
        });

        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper
                .SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Word swipedWord = wordListAdapter.getWordAtPosition(
                        viewHolder.getAdapterPosition());
                wordViewModel.deleteAWord(swipedWord);
            }
        });
        touchHelper.attachToRecyclerView(recyclerWord);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewWord.launch(new Intent(MainActivity.this, AddNewActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear_data) {
            if (wordViewModel.getAWord().length == 0) {
                Toast.makeText(this, "Nothing to clear!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Clearing... data", Toast.LENGTH_SHORT).show();
                wordViewModel.deleteAll();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}