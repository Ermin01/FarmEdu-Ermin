package com.example.farmedu_ermin.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.R;
import com.example.farmedu_ermin.models.ZacinskoBilje;
import com.example.farmedu_ermin.utils.ZacinskoDetaljiFragment;

import java.util.ArrayList;

public class ZacinskoBiljeAdapter extends RecyclerView.Adapter<ZacinskoBiljeAdapter.ViewHolder> {

    Context context;
    ArrayList<ZacinskoBilje> lista;

    public ZacinskoBiljeAdapter(Context context, ArrayList<ZacinskoBilje> lista) {
        this.context = context;
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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_povrce, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ZacinskoBilje z = lista.get(position);

        // 🔥 PREVOD preko strings.xml
        int resId = holder.itemView.getContext()
                .getResources()
                .getIdentifier(z.getNaslov(), "string",
                        holder.itemView.getContext().getPackageName());

        if (resId != 0) {
            holder.naziv.setText(resId);
        } else {
            holder.naziv.setText(z.getNaslov());
        }

        holder.img.setImageResource(
                z.getSlika() != 0 ? z.getSlika() : R.drawable.placeholder
        );

        holder.itemView.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putSerializable("biljka", z);

            Fragment fragment = new ZacinskoDetaljiFragment();
            fragment.setArguments(bundle);

            ((AppCompatActivity) v.getContext())
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