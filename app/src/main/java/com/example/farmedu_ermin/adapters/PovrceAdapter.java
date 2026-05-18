package com.example.farmedu_ermin.adapters;

import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.R;
import com.example.farmedu_ermin.models.Povrce;
import com.example.farmedu_ermin.utils.PovrceDetaljiFragment;

import java.util.ArrayList;

public class PovrceAdapter extends RecyclerView.Adapter<PovrceAdapter.ViewHolder> {

    ArrayList<Povrce> lista;

    public PovrceAdapter(ArrayList<Povrce> lista) {
        this.lista = lista;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView naziv;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgPovrce);
            naziv = itemView.findViewById(R.id.tvNaziv);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_povrce, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Povrce p = lista.get(position);

        // 🔥 NASLOV direktno iz string resource
        int resId = holder.itemView.getContext()
                .getResources()
                .getIdentifier(p.getNaslov(), "string",
                        holder.itemView.getContext().getPackageName());

        if (resId != 0) {
            holder.naziv.setText(resId);
        } else {
            holder.naziv.setText(p.getNaslov());
        }

        // slika
        if (p.getSlika() != 0) {
            holder.img.setImageResource(p.getSlika());
        } else {
            holder.img.setImageResource(R.drawable.povrceicona);
        }

        // 🔥 klik → šalje CIJELI objekat (kao VOĆE)
        holder.itemView.setOnClickListener(view -> {

            Bundle b = new Bundle();
            b.putSerializable("povrce", p);

            Fragment fragment = new PovrceDetaljiFragment();
            fragment.setArguments(b);

            ((AppCompatActivity) view.getContext())
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left,
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                    )
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}