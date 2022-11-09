package com.inplanesight.ui.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inplanesight.R;
import com.inplanesight.models.Flight;

import java.time.format.DateTimeFormatter;

public class FlightInfoRecyclerViewAdapter extends RecyclerView.Adapter<FlightInfoRecyclerViewAdapter.FlightViewHolder> {

    Context context;
    Flight[] flights;

    public FlightInfoRecyclerViewAdapter(Context context, Flight[] flights) {
        this.context = context;
        this.flights = flights;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_flight_info, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flights[position];
        holder.flightNumber.setText(flight.getNumber());
        holder.origin.setText(flight.getOrigin());
        holder.destination.setText(flight.getDestination());
        holder.terminal.setText(flight.getTerminal());
        holder.gate.setText(flight.getGate());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
        holder.boardingTime.setText(formatter.format(flight.getTime()));
    }

    @Override
    public int getItemCount() {
        return flights.length;
    }

    public class FlightViewHolder extends RecyclerView.ViewHolder {

        TextView flightNumber;
        TextView origin;
        TextView destination;
        TextView terminal;
        TextView gate;
        TextView boardingTime;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            flightNumber = itemView.findViewById(R.id.flightInfoNumber);
            origin = itemView.findViewById(R.id.flightInfoOrigin);
            destination = itemView.findViewById(R.id.flightInfoDestination);
            terminal = itemView.findViewById(R.id.flightInfoTerminal);
            gate = itemView.findViewById(R.id.flightInfoGate);
            boardingTime = itemView.findViewById(R.id.flightInfoBoarding);
        }
    }
}
