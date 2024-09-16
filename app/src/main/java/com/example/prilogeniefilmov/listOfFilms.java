package com.example.prilogeniefilmov;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class listOfFilms extends Fragment implements adapterForFilm.OnItemClickListener {
    private DatabaseReference mDataBase;
    private FirebaseAuth firebaseAuth;
    private final String DATA_BASE_URL = "https://curs1-1d2ca-default-rtdb.europe-west1.firebasedatabase.app/";
    private final String FILM_KEY = "film";
    private RecyclerView filmRecyclerView;
    private SearchView searchView;
    private Button addFilmButton, signOutButton;
    private adapterForFilm filmAdapter;
    private ArrayList<Film> filmList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_of_films_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(searchView.getQuery())) {
            searchView.setQuery("", true);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataBase = FirebaseDatabase.getInstance(DATA_BASE_URL).getReference(FILM_KEY);
        firebaseAuth = FirebaseAuth.getInstance();

        filmList = new ArrayList<>();
        filmAdapter = new adapterForFilm(filmList);
        getDataFromDB();

        searchView = view.findViewById(R.id.search_view);
        filmRecyclerView = view.findViewById(R.id.film_recycler_view);
        addFilmButton = view.findViewById(R.id.add_film_button);
        signOutButton = view.findViewById(R.id.signOutButton);

        filmRecyclerView.setAdapter(filmAdapter);
        filmRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        filmAdapter.setOnItemClickListener(new adapterForFilm.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Обработка нажатия на элемент RecyclerView
                Film selectedFilm = filmList.get(position);

                informationAboutFilm informationAboutFilmFragment = new informationAboutFilm();

                Bundle bundle = new Bundle();
                bundle.putSerializable("film", selectedFilm);
                informationAboutFilmFragment.setArguments(bundle);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, informationAboutFilmFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filmAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filmAdapter.filter(newText);
                return false;
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Выход из аккаунта");
                builder.setMessage("Вы уверены, что хотите выйти из аккаунта?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                        Intent intent = new Intent(getActivity(), AutoriseActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
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

        addFilmButton.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new addFilmFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    private void getDataFromDB() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!filmList.isEmpty()) {
                    filmList.clear();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Film film = dataSnapshot.getValue(Film.class);
                    assert film != null;
                    film.setTitle(dataSnapshot.getKey());
                    filmList.add(film);
                }
                filmAdapter.updateData(filmList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDataBase.addValueEventListener(valueEventListener);
    }

    @Override
    public void onItemClick(int position) {
        Film selectedFilm = filmList.get(position);

        informationAboutFilm informationAboutFilmFragment = new informationAboutFilm();

        Bundle bundle = new Bundle();
        bundle.putSerializable("film", selectedFilm);
        informationAboutFilmFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, informationAboutFilmFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}