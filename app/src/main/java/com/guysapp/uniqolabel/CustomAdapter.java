package com.guysapp.uniqolabel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guysapp.uniqolabel.ForecastModel.ForecastResponse;
import com.guysapp.uniqolabel.ForecastModel.ListItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {


    List<ListItem> forecastResponses;
    Context context;
    public CustomAdapter(Context context,List<ListItem> forecastResponses){
        this.forecastResponses = forecastResponses;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.row_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Log.d("Adapter",position+" ");
        Date d =  new Date(((long) forecastResponses.get(position).getDt()) * 1000);
        SimpleDateFormat simpledate  = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
        System.out.println(simpledate.format(d));
        holder.txDate.setText(simpledate.format(d));

        holder.txTemp.setText(forecastResponses.get(position).getMain().getTemp()+ (char) 0x00B0+"");
        holder.txPressure.setText(forecastResponses.get(position).getMain().getPressure()+"");
        holder.txHumidity.setText(forecastResponses.get(position).getMain().getHumidity()+"");




    }

    @Override
    public int getItemCount() {
        return forecastResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txDate,txTemp,txHumidity,txPressure;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txDate = itemView.findViewById(R.id.tx_dateUpcoming);
            txTemp = itemView.findViewById(R.id.tx_temp_forecast);
            txHumidity = itemView.findViewById(R.id.tx_humdity_forecast);
            txPressure = itemView.findViewById(R.id.tx_pressure_forecast);
        }
    }


}
