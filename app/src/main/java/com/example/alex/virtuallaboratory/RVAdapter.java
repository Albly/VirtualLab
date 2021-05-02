package com.example.alex.virtuallaboratory;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alex.virtuallaboratory.Fragments.Measurement;

import java.util.List;

/**
 * Created by Alex on 28.01.2018.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CardViewHolder> {
    List<Measurement> measurements;

    public RVAdapter(List<Measurement> measurements){
        this.measurements = measurements;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        CardViewHolder viewHolder = new CardViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {

        holder.tv_friction.setText("Коэффициент трения: "+ measurements.get(position).friction);
        holder.tv_measurement.setText("Измерение: " +String.valueOf(measurements.get(position).measurement));
        holder.tv_angle.setText("Средний угол наклона: "+measurements.get(position).angle);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return measurements.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView tv_measurement;
        TextView tv_friction;
        TextView tv_angle;

        CardViewHolder(View itemView){
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.cv);
            tv_angle = (TextView)itemView.findViewById(R.id.tv_angle);
            tv_friction = (TextView)itemView.findViewById(R.id.tv_friction);
            tv_measurement = (TextView)itemView.findViewById(R.id.tv_number_measurement);
        }
    }
}
