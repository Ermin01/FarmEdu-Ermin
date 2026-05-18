package com.example.farmedu_ermin.adapters;

import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.R;
import com.example.farmedu_ermin.models.ObjectModel;

import java.util.List;

public class ObjectAdapter extends RecyclerView.Adapter<ObjectAdapter.VH> {

    List<ObjectModel> list;

    public ObjectAdapter(List<ObjectModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_object, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        ObjectModel model = list.get(position);

        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getDesc()); // 🔥 OVO TI FALI
        holder.image.setImageResource(model.getImage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, desc;

        public VH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgObject);
            title = itemView.findViewById(R.id.txtTitle);
            desc = itemView.findViewById(R.id.txtDesc); // 🔥 NOVO
        }
    }
}