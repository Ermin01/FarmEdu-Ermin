package com.example.farmedu_ermin.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.view.animation.AlphaAnimation;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.farmedu_ermin.R;
import com.example.farmedu_ermin.models.Zivotinja;

import java.util.HashSet;
import java.util.Set;

public class DetaljiZivotinjeFragment extends Fragment {

    private ImageView btnFavorite;
    private boolean isFavorite = false;

    private static final String PREF_NAME = "favorites_pref";
    private static final String KEY_FAVORITES = "favorites";

    // 🔥 toggle animacija
    private void toggle(View content, ImageView arrow) {

        if (content == null || arrow == null) return;

        if (content.getVisibility() == View.GONE) {

            content.setVisibility(View.VISIBLE);

            AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
            fadeIn.setDuration(250);
            content.startAnimation(fadeIn);

            arrow.animate().rotation(180).setDuration(200).start();

        } else {

            AlphaAnimation fadeOut = new AlphaAnimation(1f, 0f);
            fadeOut.setDuration(200);
            content.startAnimation(fadeOut);

            content.setVisibility(View.GONE);

            arrow.animate().rotation(0).setDuration(200).start();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalji_zivotinje, container, false);

        ImageView img = view.findViewById(R.id.imgDetalj);
        TextView naziv = view.findViewById(R.id.txtNaziv);

        TextView txtOsnovne = view.findViewById(R.id.txtOsnovne);
        TextView txtPonasanje = view.findViewById(R.id.txtPonasanje);
        TextView txtZdravlje = view.findViewById(R.id.txtZdravlje);
        TextView txtIshrana = view.findViewById(R.id.txtIshrana);
        TextView txtAktivnost = view.findViewById(R.id.txtAktivnost);

        LinearLayout hOsnovne = view.findViewById(R.id.headerOsnovne);
        ImageView aOsnovne = view.findViewById(R.id.arrowOsnovne);

        LinearLayout hPonasanje = view.findViewById(R.id.headerPonasanje);
        ImageView aPonasanje = view.findViewById(R.id.arrowPonasanje);

        LinearLayout hZdravlje = view.findViewById(R.id.headerZdravlje);
        ImageView aZdravlje = view.findViewById(R.id.arrowZdravlje);

        LinearLayout hIshrana = view.findViewById(R.id.headerIshrana);
        ImageView aIshrana = view.findViewById(R.id.arrowIshrana);

        LinearLayout hAktivnost = view.findViewById(R.id.headerAktivnost);
        ImageView aAktivnost = view.findViewById(R.id.arrowAktivnost);

        ImageView btnBack = view.findViewById(R.id.btnBack);
        btnFavorite = view.findViewById(R.id.btnFavorite);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        }

        if (hOsnovne != null) hOsnovne.setOnClickListener(v -> toggle(txtOsnovne, aOsnovne));
        if (hPonasanje != null) hPonasanje.setOnClickListener(v -> toggle(txtPonasanje, aPonasanje));
        if (hZdravlje != null) hZdravlje.setOnClickListener(v -> toggle(txtZdravlje, aZdravlje));
        if (hIshrana != null) hIshrana.setOnClickListener(v -> toggle(txtIshrana, aIshrana));
        if (hAktivnost != null) hAktivnost.setOnClickListener(v -> toggle(txtAktivnost, aAktivnost));

        Bundle args = getArguments();

        if (args != null && args.containsKey("zivotinja")) {

            Zivotinja z = (Zivotinja) args.getSerializable("zivotinja");

            if (z != null) {

                String petName = z.getIme();

                // 🔥 LOAD FAVORITES
                SharedPreferences prefs = requireContext()
                        .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

                Set<String> favorites = new HashSet<>(
                        prefs.getStringSet(KEY_FAVORITES, new HashSet<>())
                );

                isFavorite = favorites.contains(petName);
                updateFavoriteIcon();

                // ⭐ CLICK FAVORITE
                btnFavorite.setOnClickListener(v -> {

                    SharedPreferences sp = requireContext()
                            .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

                    Set<String> current = new HashSet<>(
                            sp.getStringSet(KEY_FAVORITES, new HashSet<>())
                    );

                    if (isFavorite) {
                        current.remove(petName);
                        Toast.makeText(getContext(), "Uklonjeno ❌", Toast.LENGTH_SHORT).show();
                    } else {
                        current.add(petName);
                        Toast.makeText(getContext(), "Dodano ❤️", Toast.LENGTH_SHORT).show();
                    }

                    sp.edit()
                            .putStringSet(KEY_FAVORITES, current)
                            .apply();

                    isFavorite = !isFavorite;
                    updateFavoriteIcon();
                });

                if (naziv != null) naziv.setText(z.getIme());

                if (img != null && z.getSlika() != 0) {
                    img.setImageResource(z.getSlika());
                }

                // 🔥 OSNOVNE
                if (z.getOsnovneInfo() != null && txtOsnovne != null) {
                    txtOsnovne.setText(
                            label(R.string.zivotni_vijek, z.getOsnovneInfo().getZivotniVijek()) +
                                    label(R.string.visina, z.getOsnovneInfo().getVisina()) +
                                    label(R.string.tezina, z.getOsnovneInfo().getTezina()) +
                                    label(R.string.porijeklo, z.getOsnovneInfo().getPorijeklo()) +
                                    label(R.string.brzina, z.getOsnovneInfo().getBrzina()) +
                                    label(R.string.tip_dlake, z.getOsnovneInfo().getTipDlake()) +
                                    label(R.string.snaga, z.getOsnovneInfo().getSnaga())
                    );
                }

                // 🔥 PONAŠANJE
                if (z.getPonasanje() != null && txtPonasanje != null) {
                    txtPonasanje.setText(
                            label(R.string.temperament, z.getPonasanje().getTemperament()) +
                                    label(R.string.inteligencija, z.getPonasanje().getInteligencija()) +
                                    label(R.string.socijalno, z.getPonasanje().getSocijalnoPonasanje()) +
                                    label(R.string.ljudi, z.getPonasanje().getOdnosPremaLjudima()) +
                                    label(R.string.djeca, z.getPonasanje().getOdnosPremaDjeci()) +
                                    label(R.string.zivotinje, z.getPonasanje().getOdnosPremaZivotinjama()) +
                                    label(R.string.energija, z.getPonasanje().getNivoEnergije()) +
                                    label(R.string.trening, z.getPonasanje().getLakocaTreniranja()) +
                                    label(R.string.instinkti, z.getPonasanje().getInstinkti())
                    );
                }

                // 🔥 ZDRAVLJE
                if (z.getZdravlje() != null && txtZdravlje != null) {
                    txtZdravlje.setText(
                            label(R.string.starost, z.getZdravlje().getProsjecnaStarost()) +
                                    label(R.string.bolesti, z.getZdravlje().getBolesti()) +
                                    label(R.string.genetika, z.getZdravlje().getGenetika()) +
                                    label(R.string.otpornost, z.getZdravlje().getOtpornost()) +
                                    label(R.string.osjetljivost, z.getZdravlje().getOsjetljivost())
                    );
                }

                // 🔥 ISHRANA
                if (z.getIshrana() != null && txtIshrana != null) {
                    txtIshrana.setText(
                            label(R.string.tip, z.getIshrana().getTip()) +
                                    label(R.string.obroci, z.getIshrana().getDnevniUnos())
                    );
                }

                // 🔥 AKTIVNOST
                if (z.getAktivnost() != null && txtAktivnost != null) {
                    txtAktivnost.setText(
                            label(R.string.kretanje, z.getAktivnost().getKretanje()) +
                                    label(R.string.trening, z.getAktivnost().getTrening())
                    );
                }
            }
        }

        return view;
    }

    // ⭐ ICON UPDATE
    private void updateFavoriteIcon() {

        if (btnFavorite == null) return;

        btnFavorite.setColorFilter(
                requireContext().getColor(
                        isFavorite ? android.R.color.holo_red_light
                                : android.R.color.white
                )
        );
    }

    private String label(int resId, String value) {
        return getString(resId) + ": " + (value != null ? value : "-") + "\n";
    }
}