package com.example.wrongparking;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Recycler extends RecyclerView.Adapter<Recycler.ViewHolder> {

    private String[] data;
    public Recycler(String[] data){

        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_redaktorius, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = data[position];
        holder.recyclerID.setText(title);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView recyclerID;
        public ViewHolder(View itemView) {
            super(itemView);
            recyclerID = itemView.findViewById(R.id.recyclerID);
        }
    }

}

