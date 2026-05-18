package com.example.farmedu_ermin.adapters;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.farmedu_ermin.R;
import com.example.farmedu_ermin.models.OpremaModel;
import java.util.List;

public class OpremaAdapter extends RecyclerView.Adapter<OpremaAdapter.ViewHolder> {

    List<OpremaModel> list;

    public OpremaAdapter(List<OpremaModel> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title, desc;

        public ViewHolder(View v) {
            super(v);
            icon = v.findViewById(R.id.icon);
            title = v.findViewById(R.id.title);
            desc = v.findViewById(R.id.desc);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topic, parent, false); // 👈 BITNO
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        OpremaModel m = list.get(pos);

        h.icon.setImageResource(m.image);
        h.title.setText(m.name);
        h.desc.setText("Klikni za više informacija");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}