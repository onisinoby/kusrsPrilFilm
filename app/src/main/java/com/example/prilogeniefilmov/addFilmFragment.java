package com.example.prilogeniefilmov;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addFilmFragment extends Fragment {
    private DatabaseReference mDataBase;
    private final String FILM_KEY = "film";
    private EditText titleEditText, genreEditText, yearEditText, ratingEditText, directorEditText, producerEditText;
    private Button addButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_film, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleEditText = view.findViewById(R.id.edit_text_title);
        genreEditText =         view.findViewById(R.id.edit_text_genre);
        yearEditText =          view.findViewById(R.id.edit_text_year);
        ratingEditText =         view.findViewById(R.id.edit_text_rating);
        directorEditText =         view.findViewById(R.id.edit_text_director);
        producerEditText = view.findViewById(R.id.edit_text_producer);
        mDataBase =             FirebaseDatabase.getInstance("https://curs1-1d2ca-default-rtdb.europe-west1.firebasedatabase.app/").getReference(FILM_KEY);
        addButton =             view.findViewById(R.id.button_add);

        addButton.setOnClickListener(v -> {
            String title =          titleEditText.getText().toString();
            String rating =          ratingEditText.getText().toString();
            String year =           yearEditText.getText().toString();
            String genre =          genreEditText.getText().toString();
            String director =          directorEditText.getText().toString();
            String producer =  producerEditText.getText().toString();
            String id =             mDataBase.push().getKey();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(year) || TextUtils.isEmpty(genre) || TextUtils.isEmpty(director) || TextUtils.isEmpty(producer) || TextUtils.isEmpty(rating)) {
                Toast.makeText(requireContext(), "Заполните поля", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                Toast.makeText(requireContext(), "Новые данные добавлены", Toast.LENGTH_SHORT).show();
            }

            Film newFilm = new Film(title, year,  genre, director, producer, rating);
            mDataBase.child(title).setValue(newFilm);

            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }
}
