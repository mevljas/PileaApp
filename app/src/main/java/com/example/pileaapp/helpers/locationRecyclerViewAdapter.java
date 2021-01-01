package com.example.pileaapp.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pileaapp.R;
import com.example.pileaapp.api.models.Category;
import com.example.pileaapp.api.models.Location;

import java.util.List;

public class locationRecyclerViewAdapter extends RecyclerView.Adapter<locationRecyclerViewAdapter.MyViewHolder> {

    List data;
    Context context;

    public locationRecyclerViewAdapter(Context ct, List s1){
        this.context = ct;
        this.data = s1;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.categories_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        TODO: this has to be done differently for different object types. This is just a temporary fix.
        if (data.get(position) instanceof Category)
            holder.myText1.setText(((Category) data.get(position)).getPlantCategory());
        else if (data.get(position) instanceof Location)
            holder.myText1.setText(((Location) data.get(position)).getName());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView myText1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.RowCategoryName);
        }
    }
}
