package com.example.pileaapp.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Base64;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

//    Decode Base64 image
    private Bitmap decodeImage(Plant currentPlant){
        byte[] decodedString = Base64.decode(currentPlant.getImage(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Plant currentPlant = (Plant) data.get(position);

        holder.myText1.setText((currentPlant).getName());

        Date lastWatering = new Date();
        Date currentDate = new Date();
        SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");

        try {
            lastWatering = dateOnly.parse(currentPlant.getNextWateredDate());
            holder.days.setText("Water on "+dateOnly.format(lastWatering));

            if(currentDate.after(lastWatering)){
                holder.days.setTextColor(Color.parseColor("#FF0000"));
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }


//        Decode Base64 image
        if (currentPlant.getImage() != null){
            holder.plantImage.setImageBitmap(decodeImage(currentPlant));
        }



        holder.plantImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("halloookkko "+currentPlant.getCategory());
                instance.DetailPlantAcitivty(v, currentPlant);
            }
        });


        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                System.out.println("Instance" +instance+"   ");
                instance.ShowDeletePopup(view, currentPlant);
            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                System.out.println("halloookkko "+data.get(position).getCategory());
                instance.ShowEditPopup(view, currentPlant);
            }
        });
        holder.waterButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                instance.waterPlant(currentPlant);
            }
        });



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView myText1;
        TextView days;
        ImageButton deleteButton;
        ImageButton editButton;
        ImageButton waterButton;
        ImageView plantImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.plantsTVName);
            days = itemView.findViewById(R.id.plantsTVNotes);

            editButton = (ImageButton) itemView.findViewById(R.id.plantsIBEdit);
            deleteButton = (ImageButton) itemView.findViewById(R.id.plantsIBClose);
            waterButton = (ImageButton) itemView.findViewById(R.id.plantsIBWater);

            plantImage = (ImageView) itemView.findViewById(R.id.plantsIVImage);
        }
    }
}
