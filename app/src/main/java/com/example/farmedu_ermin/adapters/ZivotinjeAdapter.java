package com.example.farmedu_ermin.adapters;

import android.os.Bundle;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.R;
import com.example.farmedu_ermin.ListaZivotinjaFragment;
import com.example.farmedu_ermin.models.Zivotinja;
import com.example.farmedu_ermin.utils.DetaljiZivotinjeFragment;

import java.util.List;

public class ZivotinjeAdapter extends RecyclerView.Adapter<ZivotinjeAdapter.ViewHolder> {

    private List<Zivotinja> lista;
    private int lastPosition = -1;

    public ZivotinjeAdapter(List<Zivotinja> lista) {
        this.lista = lista;


        setHasStableIds(true); // ✅ OVDJE
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView naziv;
        ImageView slika;

        public ViewHolder(View itemView) {
            super(itemView);
            naziv = itemView.findViewById(R.id.txtNaziv);
            slika = itemView.findViewById(R.id.imgZivotinja);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_kategorija_zivotnje, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Zivotinja z = lista.get(position);

        holder.naziv.setText(z.getIme());

        if (z.getSlika() != 0) {
            holder.slika.setImageResource(z.getSlika());
        } else {
            holder.slika.setImageResource(R.drawable.book_test);
        }

        // ✅ SIGURNA ANIMACIJA (koristi adapter position)
        int currentPos = holder.getAdapterPosition();
        if (currentPos != RecyclerView.NO_POSITION && currentPos > lastPosition) {

            Animation anim = AnimationUtils.loadAnimation(
                    holder.itemView.getContext(),
                    R.anim.fade_scale
            );

            holder.itemView.startAnimation(anim);
            lastPosition = currentPos;
        }

        // 🔥 TOUCH ANIMACIJA
        holder.itemView.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start();
                    v.setElevation(12f);
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    v.setElevation(6f);
                    break;
            }

            return false;
        });

        // ✅ CLICK (ISPRAVNO)
        holder.itemView.setOnClickListener(v -> {

            int pos = holder.getAdapterPosition();

            if (pos == RecyclerView.NO_POSITION) return;

            Zivotinja clicked = lista.get(pos);

            v.animate()
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setDuration(80)
                    .withEndAction(() -> {

                        v.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(120)
                                .start();

                        openFragment(v, clicked);

                    })
                    .start();
        });
    }

    private void openFragment(View v, Zivotinja z) {

        Bundle bundle = new Bundle();
        Fragment fragment;

        if (z.getKategorija() != null && z.getKategorija().equals(z.getIme())) {

            bundle.putString("kategorija", z.getIme());

            fragment = new ListaZivotinjaFragment();
            fragment.setArguments(bundle);

        } else {

            bundle.putSerializable("zivotinja", z);

            fragment = new DetaljiZivotinjeFragment();
            fragment.setArguments(bundle);
        }

        openWithAnimation((AppCompatActivity) v.getContext(), fragment);
    }

    private void openWithAnimation(AppCompatActivity activity, Fragment fragment) {

        activity.getSupportFragmentManager()
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
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}