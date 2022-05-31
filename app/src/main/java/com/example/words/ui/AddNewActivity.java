package com.example.words.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.TextUtilsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.example.words.databinding.ActivityAddNewBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;
import java.util.Objects;

public class AddNewActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.words.ui.AddNewActivity";

    ActivityAddNewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MaterialButton buttonAdd = binding.buttonAdd;
        TextInputLayout layoutInputWord = binding.layoutInputWord;
        TextInputEditText editInputWord = binding.editInputWord;

        editInputWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (layoutInputWord.isErrorEnabled()) layoutInputWord.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newWord = Objects.requireNonNull(editInputWord.getText())
                        .toString().toLowerCase();
                if (TextUtils.isEmpty(newWord)) {
                    layoutInputWord.setError("Required");
                    return;
                }

                Intent replyIntent = new Intent();
                replyIntent.putExtra(EXTRA_REPLY, newWord);
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });
    }
}