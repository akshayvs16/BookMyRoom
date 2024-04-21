package com.project.bookmyroom.view.components.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.bookmyroom.R;
import com.project.bookmyroom.view.activity.DetailsActivity;
import com.project.bookmyroom.viewmodel.TopPlacesData;

import org.jetbrains.annotations.Nullable;

import java.util.List;


public class TopPlacesAdapter extends RecyclerView.Adapter<TopPlacesAdapter.TopPlacesViewHolder> {

    Context context;
    List<TopPlacesData> topPlacesDataList;

    public TopPlacesAdapter(Context context, List<TopPlacesData> topPlacesDataList) {
        this.context = context;
        this.topPlacesDataList = topPlacesDataList;
    }

    @NonNull
    @Override
    public TopPlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.near_hotels_row_item, parent, false);

        // here we create a recyclerview row item layout file
        return new TopPlacesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopPlacesViewHolder holder, int position) {

        TopPlacesData currentItem = topPlacesDataList.get(position);

        holder.countryName.setText(currentItem.getCountryName());
        holder.placeName.setText(currentItem.getPlaceName());
        holder.price.setText(currentItem.getPrice());

        // Load image using Glide
        Glide.with(context)
                .load(currentItem.getImageUrl()) // Assuming imageUrl is a valid URL string
                /* .placeholder(R.drawable.placeholder_image) // Placeholder image while loading
                 .error(R.drawable.error_image) // Error image if loading fails*/
                .into(holder.placeImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, DetailsActivity.class);
                i.putExtra("PLACE_DATA", currentItem);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topPlacesDataList.size();
    }

    public void setData(@Nullable List<TopPlacesData> data) {
        topPlacesDataList.clear();
        if (data != null) {
            topPlacesDataList.addAll(data);
        }
        notifyDataSetChanged();
    }

    public static final class TopPlacesViewHolder extends RecyclerView.ViewHolder{

        ImageView placeImage;
        TextView placeName, countryName, price;

        public TopPlacesViewHolder(@NonNull View itemView) {
            super(itemView);

            placeImage = itemView.findViewById(R.id.place_image);
            placeName = itemView.findViewById(R.id.place_name);
            countryName = itemView.findViewById(R.id.country_name);
            price = itemView.findViewById(R.id.price);

        }
    }
}
