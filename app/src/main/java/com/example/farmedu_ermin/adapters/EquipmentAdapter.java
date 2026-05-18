package com.example.farmedu_ermin.adapters;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.farmedu_ermin.R;
import com.example.farmedu_ermin.models.EquipmentModel;
import java.util.List;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.VH> {

    List<EquipmentModel> list;

    public EquipmentAdapter(List<EquipmentModel> list) {
        this.list = list;
    }

    public static class VH extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;

        public VH(View v) {
            super(v);
            icon = v.findViewById(R.id.icon);
            title = v.findViewById(R.id.title);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        return new VH(LayoutInflater.from(p.getContext())
                .inflate(R.layout.item_equipment, p, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        EquipmentModel m = list.get(pos);
        h.icon.setImageResource(m.icon);
        h.title.setText(m.title);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}