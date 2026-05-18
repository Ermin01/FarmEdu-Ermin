package com.example.farmedu_ermin;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.farmedu_ermin.models.TaskModel;

import java.util.ArrayList;
import java.util.Calendar;

public class PlanerdanaFragment extends Fragment {

    private TextView selectedDayView = null;

    private GridLayout calendarGrid;
    private TextView txtMonth;
    private LinearLayout tasksContainer;
    private TextView txtTaskCount;

    private Calendar currentCalendar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentCalendar = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_planerdana,
                container,
                false
        );

        calendarGrid = view.findViewById(R.id.calendarGrid);
        txtMonth = view.findViewById(R.id.txtMonth);
        tasksContainer = view.findViewById(R.id.tasksContainer);
        txtTaskCount = view.findViewById(R.id.txtTaskCount);

        // PRETHODNI MJESEC
        view.findViewById(R.id.btnPrevMonth)
                .setOnClickListener(v -> {

                    currentCalendar.add(Calendar.MONTH, -1);

                    selectedDayView = null;

                    generateCalendar();
                });

        // SLJEDEĆI MJESEC
        view.findViewById(R.id.btnNextMonth)
                .setOnClickListener(v -> {

                    currentCalendar.add(Calendar.MONTH, 1);

                    selectedDayView = null;

                    generateCalendar();
                });

        // DODAJ TASK
        view.findViewById(R.id.btnDodajTask)
                .setOnClickListener(v -> {

                    getParentFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_in_right,
                                    R.anim.slide_out_left,
                                    R.anim.slide_in_left,
                                    R.anim.slide_out_right
                            )
                            .replace(
                                    R.id.fragmentContainer,
                                    new DodajTaskFragment()
                            )
                            .addToBackStack(null)
                            .commit();
                });

        generateCalendar();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        generateCalendar();
    }

    private void generateCalendar() {

        calendarGrid.removeAllViews();

        int month = currentCalendar.get(Calendar.MONTH);

        int year = currentCalendar.get(Calendar.YEAR);

        String[] monthsBosnian = {
                "Januar",
                "Februar",
                "Mart",
                "April",
                "Maj",
                "Juni",
                "Juli",
                "August",
                "Septembar",
                "Oktobar",
                "Novembar",
                "Decembar"
        };

        txtMonth.setText(
                monthsBosnian[month] + " " + year
        );

        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month, 1);

        int daysInMonth =
                calendar.getActualMaximum(
                        Calendar.DAY_OF_MONTH
                );

        int firstDay =
                calendar.get(Calendar.DAY_OF_WEEK);

        int emptyDays = firstDay - 2;

        if (emptyDays < 0) {
            emptyDays = 6;
        }

        // PRAZNA POLJA
        for (int i = 0; i < emptyDays; i++) {

            TextView emptyView =
                    new TextView(getContext());

            GridLayout.LayoutParams params =
                    new GridLayout.LayoutParams();

            params.width = 0;
            params.height = 110;

            params.columnSpec =
                    GridLayout.spec(
                            GridLayout.UNDEFINED,
                            1f
                    );

            emptyView.setLayoutParams(params);

            calendarGrid.addView(emptyView);
        }

        // DANI
        for (int day = 1; day <= daysInMonth; day++) {

            TextView dayView =
                    new TextView(getContext());

            GridLayout.LayoutParams params =
                    new GridLayout.LayoutParams();

            params.width = 0;
            params.height = 110;

            params.columnSpec =
                    GridLayout.spec(
                            GridLayout.UNDEFINED,
                            1f
                    );

            params.setMargins(6, 6, 6, 6);

            dayView.setLayoutParams(params);

            dayView.setGravity(Gravity.CENTER);

            dayView.setText(String.valueOf(day));

            dayView.setTextSize(16);

            String dateKey =
                    year + "-"
                            + (month + 1)
                            + "-"
                            + day;

            dayView.setTag(dateKey);

            ArrayList<TaskModel> tasks =
                    TaskStorage.getTasks(requireContext());

            boolean hasTask = false;

            for (TaskModel task : tasks) {

                if (task.getDate().equals(dateKey)) {

                    hasTask = true;
                    break;
                }
            }

            // TASK DAN
            if (hasTask) {

                dayView.setBackgroundResource(
                        R.drawable.bg_calendar_active_day
                );

                dayView.setTextColor(Color.WHITE);

                dayView.setTypeface(
                        null,
                        android.graphics.Typeface.BOLD
                );

            } else {

                dayView.setBackgroundResource(
                        R.drawable.bg_calendar_day
                );

                dayView.setTextColor(
                        Color.parseColor("#222222")
                );
            }

            int finalDay = day;

            dayView.setOnClickListener(v -> {

                // RESET STAROG
                if (selectedDayView != null) {

                    String oldDate =
                            (String) selectedDayView.getTag();

                    ArrayList<TaskModel> oldTasks =
                            TaskStorage.getTasks(requireContext());

                    boolean oldHasTask = false;

                    for (TaskModel t : oldTasks) {

                        if (t.getDate().equals(oldDate)) {

                            oldHasTask = true;
                            break;
                        }
                    }

                    if (oldHasTask) {

                        selectedDayView.setBackgroundResource(
                                R.drawable.bg_calendar_active_day
                        );

                        selectedDayView.setTextColor(Color.WHITE);

                    } else {

                        selectedDayView.setBackgroundResource(
                                R.drawable.bg_calendar_day
                        );

                        selectedDayView.setTextColor(
                                Color.parseColor("#222222")
                        );
                    }
                }

                // NOVI SELEKTOVANI
                dayView.setBackgroundResource(
                        R.drawable.bg_green_button
                );

                dayView.setTextColor(Color.WHITE);

                selectedDayView = dayView;

                showTasksForDate(
                        year + "-"
                                + (month + 1)
                                + "-"
                                + finalDay
                );
            });

            calendarGrid.addView(dayView);
        }
    }

    private void showTasksForDate(String dateKey) {

        tasksContainer.removeAllViews();

        ArrayList<TaskModel> allTasks =
                TaskStorage.getTasks(requireContext());

        ArrayList<TaskModel> filteredTasks =
                new ArrayList<>();

        for (TaskModel task : allTasks) {

            if (task.getDate().equals(dateKey)) {

                filteredTasks.add(task);
            }
        }

        if (!filteredTasks.isEmpty()) {

            txtTaskCount.setText(
                    filteredTasks.size() + " zadataka"
            );

            for (TaskModel task : filteredTasks) {

                CardView card =
                        new CardView(requireContext());

                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                params.setMargins(0, 0, 0, 24);

                card.setLayoutParams(params);

                card.setRadius(24);

                card.setCardElevation(5);

                card.setCardBackgroundColor(Color.WHITE);

                LinearLayout layout =
                        new LinearLayout(getContext());

                layout.setOrientation(
                        LinearLayout.VERTICAL
                );

                layout.setPadding(
                        40,
                        40,
                        40,
                        40
                );

                // TOP ROW
                LinearLayout topRow =
                        new LinearLayout(getContext());

                topRow.setOrientation(
                        LinearLayout.HORIZONTAL
                );

                topRow.setGravity(
                        Gravity.CENTER_VERTICAL
                );

                // TITLE
                TextView txtTitle =
                        new TextView(getContext());

                txtTitle.setText(
                        task.getIcon()
                                + " "
                                + task.getTitle()
                );

                txtTitle.setTextSize(18);

                txtTitle.setTextColor(
                        Color.parseColor("#111111")
                );

                txtTitle.setTypeface(
                        null,
                        android.graphics.Typeface.BOLD
                );

                LinearLayout.LayoutParams titleParams =
                        new LinearLayout.LayoutParams(
                                0,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                1f
                        );

                txtTitle.setLayoutParams(titleParams);

                // ACTIONS ROW
                LinearLayout actionsRow =
                        new LinearLayout(getContext());

                actionsRow.setOrientation(
                        LinearLayout.HORIZONTAL
                );

                // EDIT
                TextView btnEdit =
                        new TextView(getContext());

                btnEdit.setText("✏️");

                btnEdit.setTextSize(20);

                btnEdit.setPadding(
                        20,
                        10,
                        20,
                        10
                );

                btnEdit.setOnClickListener(v -> {

                    Bundle bundle = new Bundle();

                    bundle.putSerializable(
                            "task",
                            task
                    );

                    DodajTaskFragment fragment =
                            new DodajTaskFragment();

                    fragment.setArguments(bundle);

                    getParentFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_in_right,
                                    R.anim.slide_out_left,
                                    R.anim.slide_in_left,
                                    R.anim.slide_out_right
                            )
                            .replace(
                                    R.id.fragmentContainer,
                                    fragment
                            )
                            .addToBackStack(null)
                            .commit();
                });

                // DELETE
                TextView btnDelete =
                        new TextView(getContext());

                btnDelete.setText("🗑");

                btnDelete.setTextSize(20);

                btnDelete.setPadding(
                        20,
                        10,
                        20,
                        10
                );

                btnDelete.setOnClickListener(v -> {

                    ArrayList<TaskModel> tasks =
                            TaskStorage.getTasks(requireContext());

                    for (int i = 0; i < tasks.size(); i++) {

                        TaskModel t = tasks.get(i);

                        if (t.getTitle().equals(task.getTitle())
                                && t.getDate().equals(task.getDate())
                                && t.getTime().equals(task.getTime())) {

                            tasks.remove(i);
                            break;
                        }
                    }

                    TaskStorage.saveTasks(
                            requireContext(),
                            tasks
                    );

                    Toast.makeText(
                            requireContext(),
                            "Zadatak obrisan",
                            Toast.LENGTH_SHORT
                    ).show();

                    showTasksForDate(dateKey);

                    generateCalendar();
                });

                actionsRow.addView(btnEdit);
                actionsRow.addView(btnDelete);

                topRow.addView(txtTitle);
                topRow.addView(actionsRow);

                // TIME
                TextView txtTime =
                        new TextView(getContext());

                txtTime.setText(
                        "⏰ " + task.getTime()
                );

                txtTime.setTextSize(14);

                txtTime.setPadding(0, 14, 0, 0);

                txtTime.setTextColor(
                        Color.parseColor("#777777")
                );

                // DESCRIPTION
                TextView txtDesc =
                        new TextView(getContext());

                txtDesc.setText(
                        task.getDescription()
                );

                txtDesc.setTextSize(15);

                txtDesc.setPadding(0, 18, 0, 0);

                txtDesc.setTextColor(
                        Color.parseColor("#444444")
                );

                layout.addView(topRow);
                layout.addView(txtTime);
                layout.addView(txtDesc);

                card.addView(layout);

                tasksContainer.addView(card);
            }

        } else {

            txtTaskCount.setText("0 zadataka");

            CardView emptyCard =
                    new CardView(requireContext());

            emptyCard.setRadius(24);

            emptyCard.setCardElevation(4);

            emptyCard.setCardBackgroundColor(
                    Color.WHITE
            );

            TextView txt =
                    new TextView(getContext());

            txt.setText(
                    "Nema aktivnosti 🌱"
            );

            txt.setPadding(
                    40,
                    60,
                    40,
                    60
            );

            txt.setTextSize(16);

            txt.setGravity(Gravity.CENTER);

            txt.setTextColor(
                    Color.parseColor("#777777")
            );

            emptyCard.addView(txt);

            tasksContainer.addView(emptyCard);
        }
    }
}