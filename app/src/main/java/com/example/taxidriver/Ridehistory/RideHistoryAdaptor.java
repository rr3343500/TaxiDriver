package com.example.taxidriver.Ridehistory;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxidriver.R;
import com.example.taxidriver.extended.TexiFonts;

import java.util.List;

public class RideHistoryAdaptor extends RecyclerView.Adapter<RideHistoryAdaptor.ViewHolder>{
    Context context;
    List<HistoryData> historyData;


    public RideHistoryAdaptor(Context context, List<HistoryData> historyData) {

        this.context = context;
        this.historyData = historyData;
    }


    @NonNull
    @Override
    public RideHistoryAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.ridehistory,parent,false);
       ViewHolder viewHolder =new ViewHolder(view);
       return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RideHistoryAdaptor.ViewHolder holder, int position) {
         holder.dis.setText("Distance : "+historyData.get(position).getDis()+" KM");
         holder.amt.setText("Amount   : ₹ "+historyData.get(position).getAmt());
         holder.pic.setText(historyData.get(position).getPic());
         holder.drop.setText(historyData.get(position).getdrop());
         holder.date.setText(historyData.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return historyData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
          TexiFonts date,pic,drop,amt,dis;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date=itemView.findViewById(R.id.date);
            amt=itemView.findViewById(R.id.amt);
            pic=itemView.findViewById(R.id.pickup);
            drop=itemView.findViewById(R.id.drop);
            dis=itemView.findViewById(R.id.dis);










        }
    }
}
