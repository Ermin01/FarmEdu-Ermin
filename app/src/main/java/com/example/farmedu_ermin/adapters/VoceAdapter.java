package com.example.farmedu_ermin.adapters;

import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.R;
import com.example.farmedu_ermin.models.Voce;
import com.example.farmedu_ermin.utils.VoceDetaljiFragment;

import java.util.ArrayList;

public class VoceAdapter extends RecyclerView.Adapter<VoceAdapter.ViewHolder> {

    ArrayList<Voce> lista;

    public VoceAdapter(ArrayList<Voce> lista) {
        this.lista = lista;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView naziv;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgVoce);
            naziv = itemView.findViewById(R.id.tvNaziv);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_voce, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Voce v = lista.get(position);

        int resId = holder.itemView.getContext()
                .getResources()
                .getIdentifier(v.getNaslov(), "string",
                        holder.itemView.getContext().getPackageName());

        if (resId != 0) {
            holder.naziv.setText(resId);
        } else {
            holder.naziv.setText(v.getNaslov()); // fallback
        }

        if (v.getSlika() != 0) {
            holder.img.setImageResource(v.getSlika());
        } else {
            holder.img.setImageResource(R.drawable.voceicona);
        }

        holder.itemView.setOnClickListener(view -> {

            Bundle b = new Bundle();
            b.putSerializable("voce", v);

            Fragment fragment = new VoceDetaljiFragment();
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