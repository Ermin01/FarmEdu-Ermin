package com.example.farmedu_ermin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.R;
import com.example.farmedu_ermin.models.Poljoprivrednemasine;

import java.util.ArrayList;

public class MasineAdapter extends RecyclerView.Adapter<MasineAdapter.ViewHolder> {

    Context context;
    ArrayList<Poljoprivrednemasine> lista;

    public MasineAdapter(Context context, ArrayList<Poljoprivrednemasine> lista) {
        this.context = context;
        this.lista = lista;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_masina, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Poljoprivrednemasine masina = lista.get(position);

        holder.tvNaslov.setText(masina.getNaslov());
        holder.tvOpis.setText(masina.getOpis());

        // 🔥 SIGURNOSNI FIX
        if (masina.getSlika() != 0) {
            holder.imgMasina.setImageResource(masina.getSlika());
        } else {
            holder.imgMasina.setImageResource(R.drawable.bosanski_brdski_konj); // 👈 dodaj ovu sliku
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgMasina;
        TextView tvNaslov, tvOpis;

        public ViewHolder(View itemView) {
            super(itemView);

            imgMasina = itemView.findViewById(R.id.imgMasina);
            tvNaslov = itemView.findViewById(R.id.tvNaslov);
            tvOpis = itemView.findViewById(R.id.tvOpis);
        }
    }

}