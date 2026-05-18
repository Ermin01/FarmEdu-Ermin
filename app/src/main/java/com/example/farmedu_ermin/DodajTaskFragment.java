package com.example.farmedu_ermin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmedu_ermin.models.TaskModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class DodajTaskFragment extends Fragment {

    private EditText etNaslov;
    private EditText etOpis;

    private TextView txtDatum;
    private TextView txtVrijeme;
    private TextView txtPodsjetnik;
    private TextView txtPonovi;
    private TextView txtIkonica;

    private LinearLayout layoutDatum;
    private LinearLayout layoutVrijeme;
    private LinearLayout layoutPodsjetnik;
    private LinearLayout layoutPonovi;
    private LinearLayout layoutIkonica;

    private Calendar calendar;

    // EDIT MODE
    private boolean isEditMode = false;
    private TaskModel oldTask;

    public DodajTaskFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_dodaj_task,
                container,
                false
        );

        calendar = Calendar.getInstance();

        etNaslov = view.findViewById(R.id.etNaslov);
        etOpis = view.findViewById(R.id.etOpis);

        txtDatum = view.findViewById(R.id.txtDatum);
        txtVrijeme = view.findViewById(R.id.txtVrijeme);
        txtPodsjetnik = view.findViewById(R.id.txtPodsjetnik);
        txtPonovi = view.findViewById(R.id.txtPonovi);
        txtIkonica = view.findViewById(R.id.txtIkonica);

        layoutDatum = view.findViewById(R.id.layoutDatum);
        layoutVrijeme = view.findViewById(R.id.layoutVrijeme);
        layoutPodsjetnik = view.findViewById(R.id.layoutPodsjetnik);
        layoutPonovi = view.findViewById(R.id.layoutPonovi);
        layoutIkonica = view.findViewById(R.id.layoutIkonica);

        // =========================
        // EDIT TASK
        // =========================
        if (getArguments() != null) {

            oldTask = (TaskModel) getArguments()
                    .getSerializable("task");

            if (oldTask != null) {

                isEditMode = true;

                etNaslov.setText(
                        oldTask.getTitle()
                );

                etOpis.setText(
                        oldTask.getDescription()
                );

                txtDatum.setText(
                        oldTask.getDate()
                );

                txtVrijeme.setText(
                        oldTask.getTime()
                );

                txtPodsjetnik.setText(
                        oldTask.getReminder()
                );

                txtPonovi.setText(
                        oldTask.getRepeat()
                );

                txtIkonica.setText(
                        oldTask.getIcon()
                );

                ((TextView) view.findViewById(R.id.btnSacuvaj))
                        .setText("Uredi");

            }
        }

        // BACK
        view.findViewById(R.id.btnBack)
                .setOnClickListener(v -> {

                    requireActivity()
                            .getSupportFragmentManager()
                            .popBackStack();
                });

        // DATUM
        layoutDatum.setOnClickListener(v -> {

            DatePickerDialog dialog =
                    new DatePickerDialog(
                            requireContext(),
                            (view1, year, month, dayOfMonth) -> {

                                String datum =
                                        year + "-"
                                                + (month + 1)
                                                + "-"
                                                + dayOfMonth;

                                txtDatum.setText(datum);

                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                    );

            dialog.show();
        });

        // VRIJEME
        layoutVrijeme.setOnClickListener(v -> {

            TimePickerDialog dialog =
                    new TimePickerDialog(
                            requireContext(),
                            (view12, hourOfDay, minute) -> {

                                String vrijeme =
                                        String.format(
                                                "%02d:%02d",
                                                hourOfDay,
                                                minute
                                        );

                                txtVrijeme.setText(vrijeme);

                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                    );

            dialog.show();
        });

        // PODSJETNIK
        layoutPodsjetnik.setOnClickListener(v -> {

            String[] reminders = {
                    "5 min ranije",
                    "10 min ranije",
                    "15 min ranije",
                    "30 min ranije",
                    "1 sat ranije"
            };

            androidx.appcompat.app.AlertDialog.Builder builder =
                    new androidx.appcompat.app.AlertDialog.Builder(
                            requireContext()
                    );

            builder.setTitle("Odaberi podsjetnik");

            builder.setItems(reminders, (dialog, which) -> {

                txtPodsjetnik.setText(
                        reminders[which]
                );

            });

            builder.show();
        });

        // PONAVLJANJE
        layoutPonovi.setOnClickListener(v -> {

            String[] repeat = {
                    "Nikad",
                    "Svaki dan",
                    "Svake sedmice",
                    "Svaki mjesec",
                    "Svake godine"
            };

            androidx.appcompat.app.AlertDialog.Builder builder =
                    new androidx.appcompat.app.AlertDialog.Builder(
                            requireContext()
                    );

            builder.setTitle("Ponavljanje");

            builder.setItems(repeat, (dialog, which) -> {

                txtPonovi.setText(
                        repeat[which]
                );

            });

            builder.show();
        });

        // IKONICE
        layoutIkonica.setOnClickListener(v -> {

            String[] icons = {
                    "🌱 Farma",
                    "🚜 Traktor",
                    "🐄 Krave",
                    "🌾 Sjetva",
                    "💧 Zalijevanje",
                    "📋 Lista"
            };

            androidx.appcompat.app.AlertDialog.Builder builder =
                    new androidx.appcompat.app.AlertDialog.Builder(
                            requireContext()
                    );

            builder.setTitle("Odaberi ikonicu");

            builder.setItems(icons, (dialog, which) -> {

                txtIkonica.setText(
                        icons[which]
                );

            });

            builder.show();
        });

        // SACUVAJ
        view.findViewById(R.id.btnSacuvaj)
                .setOnClickListener(v -> {

                    String naslov =
                            etNaslov.getText()
                                    .toString()
                                    .trim();

                    String opis =
                            etOpis.getText()
                                    .toString()
                                    .trim();

                    String datum =
                            txtDatum.getText()
                                    .toString();

                    String vrijeme =
                            txtVrijeme.getText()
                                    .toString();

                    String podsjetnik =
                            txtPodsjetnik.getText()
                                    .toString();

                    String ponovi =
                            txtPonovi.getText()
                                    .toString();

                    String ikonica =
                            txtIkonica.getText()
                                    .toString();

                    if (naslov.isEmpty()) {

                        Toast.makeText(
                                requireContext(),
                                "Unesi naslov",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    SharedPreferences prefs =
                            requireContext()
                                    .getSharedPreferences(
                                            "tasks",
                                            Context.MODE_PRIVATE
                                    );

                    Gson gson = new Gson();

                    String oldJson =
                            prefs.getString(
                                    "task_list",
                                    ""
                            );

                    Type type =
                            new TypeToken<ArrayList<TaskModel>>() {
                            }.getType();

                    ArrayList<TaskModel> taskList;

                    if (oldJson.isEmpty()) {

                        taskList = new ArrayList<>();

                    } else {

                        taskList =
                                gson.fromJson(
                                        oldJson,
                                        type
                                );
                    }

                    // =========================
                    // EDIT POSTOJEĆEG
                    // =========================
                    if (isEditMode && oldTask != null) {

                        for (int i = 0; i < taskList.size(); i++) {

                            TaskModel t = taskList.get(i);

                            if (t.getTitle().equals(oldTask.getTitle())
                                    && t.getDate().equals(oldTask.getDate())
                                    && t.getTime().equals(oldTask.getTime())) {

                                taskList.set(
                                        i,
                                        new TaskModel(
                                                naslov,
                                                opis,
                                                datum,
                                                vrijeme,
                                                podsjetnik,
                                                ponovi,
                                                ikonica
                                        )
                                );

                                break;
                            }
                        }

                        Toast.makeText(
                                requireContext(),
                                "Zadatak uređen ✏️",
                                Toast.LENGTH_SHORT
                        ).show();

                    } else {

                        // NOVI TASK
                        taskList.add(
                                new TaskModel(
                                        naslov,
                                        opis,
                                        datum,
                                        vrijeme,
                                        podsjetnik,
                                        ponovi,
                                        ikonica
                                )
                        );

                        Toast.makeText(
                                requireContext(),
                                "Zadatak sačuvan 🌱",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    String json =
                            gson.toJson(taskList);

                    prefs.edit()
                            .putString(
                                    "task_list",
                                    json
                            )
                            .apply();

                    requireActivity()
                            .getSupportFragmentManager()
                            .popBackStack();
                });

        return view;
    }
}