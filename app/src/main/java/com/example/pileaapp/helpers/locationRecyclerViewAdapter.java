package com.example.pileaapp.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pileaapp.R;
import com.example.pileaapp.api.models.Location;

import java.util.List;

import static com.example.pileaapp.activity.LocationsActivity.instance;


public class locationRecyclerViewAdapter extends RecyclerView.Adapter<locationRecyclerViewAdapter.MyViewHolder> {

    List<Location> data;
    Context context;

    public locationRecyclerViewAdapter(Context ct, List s1){
        this.context = ct;
        this.data = s1;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.locations_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        TODO: this has to be done differently for different object types. This is just a temporary fix.
        holder.myText1.setText(((Location) data.get(position)).getName());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instance.ShowEditPopup(view, data.get(position));
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instance.ShowDeletePopup(view, data.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView myText1;
        ImageView editButton;
        ImageButton deleteButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myText1 = (TextView) itemView.findViewById(R.id.RowLocationName);
            editButton = (ImageView) itemView.findViewById(R.id.rowLocationEdit);
            deleteButton = (ImageButton) itemView.findViewById(R.id.rowLocationDelete);
        }
    }
}
