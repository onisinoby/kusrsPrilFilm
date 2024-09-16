package com.example.prilogeniefilmov;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class informationAboutFilm extends Fragment {
    private DatabaseReference mDataBase;
    private StorageReference mStorageRef;
    private final String DATA_BASE_URL = "https://curs1-1d2ca-default-rtdb.europe-west1.firebasedatabase.app/";
    private final String FILM_KEY = "film";
    private Button deleteButton;
    private TextView titleTextView, yearTextView, genreTextView, directorTextView, producerTextView, ratingTextView;
    private ImageView filmImageView;
    private Film selectedFilm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information_about_film, container, false);

        if (getArguments() != null) {
            selectedFilm = (Film) getArguments().getSerializable("film");
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataBase = FirebaseDatabase.getInstance(DATA_BASE_URL).getReference(FILM_KEY);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        filmImageView = view.findViewById(R.id.filmImage);

        StorageReference imgRef = mStorageRef.child("film_photos/" + selectedFilm.getTitle() + ".jpg");
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Загрузка изображения в ImageView
                Glide.with(requireContext()).load(uri).into(filmImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Обработка ошибок при загрузке изображения
                Toast.makeText(requireContext(), "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show();
            }
        });

        titleTextView = view.findViewById(R.id.title);
        titleTextView.setText("Название: " + selectedFilm.getTitle());

        yearTextView = view.findViewById(R.id.year);
        yearTextView.setText("Год: " + selectedFilm.getYear());

        genreTextView = view.findViewById(R.id.genre);
        genreTextView.setText("Жанр: " + selectedFilm.getGenre());

        directorTextView = view.findViewById(R.id.director);
        directorTextView.setText("Режисёр: " + selectedFilm.getDirector());

        producerTextView = view.findViewById(R.id.producer);
        producerTextView.setText("Продюсер: " + selectedFilm.getProducer());

        ratingTextView = view.findViewById(R.id.rating);
        ratingTextView.setText("Рейтинг: " + selectedFilm.getRating() + "/10");

        deleteButton = view.findViewById(R.id.removeButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Удаление");
                builder.setMessage("Вы уверены, что хотите удалить данные?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDataBase.child(selectedFilm.getTitle()).removeValue();
                        Toast.makeText(requireContext(), "Данные удалены", Toast.LENGTH_SHORT).show();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}