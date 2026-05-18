package com.example.farmedu_ermin.adapters;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.farmedu_ermin.R;
import com.example.farmedu_ermin.models.Zitarica;
import com.example.farmedu_ermin.utils.ZitariceDetaljiFragment;

import java.util.ArrayList;

public class ZitariceAdapter extends RecyclerView.Adapter<ZitariceAdapter.ViewHolder> {

    Context context;
    ArrayList<Zitarica> lista;

    public ZitariceAdapter(Context context, ArrayList<Zitarica> lista) {
        this.context = context;
        this.lista = lista;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_povrce, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Zitarica z = lista.get(position);
        Context ctx = holder.itemView.getContext();

        int resId = ctx.getResources()
                .getIdentifier(z.getNaslov(), "string", ctx.getPackageName());

        holder.tvNaziv.setText(
                resId != 0 ? ctx.getString(resId) : z.getNaslov().replace("_", " ")
        );

        holder.img.setImageResource(
                z.getSlika() != 0 ? z.getSlika() : R.drawable.zitarice_naslov
        );

        holder.itemView.setOnClickListener(v -> {

            Bundle b = new Bundle();
            b.putSerializable("zitarica", z);

            ZitariceDetaljiFragment fragment = new ZitariceDetaljiFragment();
            fragment.setArguments(b);

            if (ctx instanceof FragmentActivity) {
                ((FragmentActivity) ctx)
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView tvNaziv;

        public ViewHolder(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.imgPovrce);
            tvNaziv = itemView.findViewById(R.id.tvNaziv);
        }
    }
}