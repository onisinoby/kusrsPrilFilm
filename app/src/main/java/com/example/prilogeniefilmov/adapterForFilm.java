package com.example.prilogeniefilmov;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class adapterForFilm extends RecyclerView.Adapter<adapterForFilm.FilmViewHolder> {

    private ArrayList<Film> filmList;
    ArrayList<Film> fullList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public adapterForFilm(ArrayList<Film> filmList) {
        this.filmList = filmList;
        this.fullList = new ArrayList<>(filmList);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_item_layout, parent, false);
        return new FilmViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        Film currentFilm = filmList.get(position);
        holder.filmTextView.setText(currentFilm.getTitle() + " " + currentFilm.getYear());
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }

    public void updateData(ArrayList<Film> newData) {
        fullList.clear();
        fullList.addAll(newData);
        notifyDataSetChanged();
    }

    public void filter(String text) {
        filmList.clear();
        if (text.isEmpty()) {
            filmList.addAll(fullList);
        } else {
            String query = text.toLowerCase().trim();
            for (Film film : fullList) {
                String search = film.getTitle().toLowerCase() + " " + film.getYear().toLowerCase();
                String search2 = film.getYear().toLowerCase() + " " + film.getTitle().toLowerCase();
                if (search.contains(query) || search2.contains(query)) {
                    filmList.add(film);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class FilmViewHolder extends RecyclerView.ViewHolder {

        public TextView filmTextView;

        public FilmViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            filmTextView = itemView.findViewById(R.id.filmTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getBindingAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
