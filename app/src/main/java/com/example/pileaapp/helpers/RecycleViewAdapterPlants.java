package com.example.pileaapp.helpers;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pileaapp.R;


import com.example.pileaapp.activity.CategoriesActivity;
import com.example.pileaapp.activity.LocationsActivity;
import com.example.pileaapp.activity.PlantsActivity;
import com.example.pileaapp.api.models.Plant;

import java.util.List;

import static com.example.pileaapp.activity.PlantsActivity.instance;


public class RecycleViewAdapterPlants extends RecyclerView.Adapter<RecycleViewAdapterPlants.MyViewHolder> {

    List<Plant> data;
    Context context;

    public RecycleViewAdapterPlants(Context ct, List s1){
        this.context = ct;
        this.data = s1;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.myText1.setText(((Plant) data.get(position)).getName());


        holder.plantImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("halloooo");
                instance.DetailPlantAcitivty(v, data.get(position));
            }
        });


        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                System.out.println("Instance" +instance+"   ");
                instance.ShowDeletePopup(view, data.get(position));
            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                instance.ShowEditPopup(view, data.get(position));
            }
        });




    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView myText1;
        ImageButton deleteButton;
        ImageButton editButton;
        ImageView plantImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.plantsTVName);
            editButton = (ImageButton) itemView.findViewById(R.id.plantsIBWater);
            deleteButton = (ImageButton) itemView.findViewById(R.id.plantsIBClose);
            plantImage = (ImageView) itemView.findViewById(R.id.plantsIVImage);
        }
    }
}
