package com.inplanesight.ui.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
        holder.airline.setText(flight.getAirline());
        holder.flightNumber.setText(flight.getNumber());
        holder.destination.setText(flight.getDestination());

        if (!flight.getGate().equals("")) {
            holder.gateGroup.setVisibility(View.VISIBLE);
            holder.gate.setText(flight.getGate());
        } else {
            holder.gate.setVisibility(View.GONE);
        }

        if (!flight.getTerminal().equals("")) {
            holder.terminalGroup.setVisibility(View.VISIBLE);
            holder.terminal.setText(flight.getGate());
        } else {
            holder.terminalGroup.setVisibility(View.GONE);
        }

        holder.terminal.setText(flight.getTerminal());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
        holder.departureTime.setText(formatter.format(flight.getTime()));

        holder.status.setText(flight.getStatus());
    }

    @Override
    public int getItemCount() {
        return flights.length;
    }

    public class FlightViewHolder extends RecyclerView.ViewHolder {

        TextView airline;
        TextView flightNumber;
        LinearLayout terminalGroup;
        TextView terminal;
        LinearLayout gateGroup;
        TextView gate;
        TextView destination;
        TextView departureTime;
        TextView status;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            airline = itemView.findViewById(R.id.flightInfoAirline);
            flightNumber = itemView.findViewById(R.id.flightInfoFlightNumVal);
            terminalGroup = itemView.findViewById(R.id.flightInfoTerminal);
            terminal = itemView.findViewById(R.id.flightInfoTerminalVal);
            gateGroup = itemView.findViewById(R.id.flightInfoGate);
            gate = itemView.findViewById(R.id.flightInfoGateVal);
            destination = itemView.findViewById(R.id.flightInfoDestinationVal);
            departureTime = itemView.findViewById(R.id.flightInfoDepartureTimeVal);
            status = itemView.findViewById(R.id.flightInfoStatusVal);
        }
    }
}
