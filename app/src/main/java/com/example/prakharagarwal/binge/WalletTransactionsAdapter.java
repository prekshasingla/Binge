package com.example.prakharagarwal.binge;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class WalletTransactionsAdapter extends RecyclerView.Adapter<WalletTransactionsAdapter.MyViewHolder> {


    private Context context;
    List<WalletActivityFragment.Transaction> transactionList;


    public WalletTransactionsAdapter(Context context, List<WalletActivityFragment.Transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View myview = LayoutInflater.from(context).inflate(R.layout.item_wallet_transaction, parent, false);
        return new WalletTransactionsAdapter.MyViewHolder(myview);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        WalletActivityFragment.Transaction transaction=transactionList.get(position);
        if(Integer.parseInt(transaction.crDr)==1){
            holder.amount.setText("+"+transaction.amount);
            holder.amount.setTextColor(context.getResources().getColor(R.color.primary_green));
            holder.secondParty.setText("From: "+transaction.secondParty);


        }else{
            holder.amount.setText("-"+transaction.amount);
            holder.amount.setTextColor(context.getResources().getColor(R.color.red));
            holder.secondParty.setText("To: "+transaction.secondParty);


        }

        DateFormat formatter = new SimpleDateFormat("hh:mm dd MMM''yy");

        long milliSeconds= Long.parseLong(transaction.timestamp);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        holder.timestamp.setText(formatter.format(calendar.getTime()));
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView secondParty;
        TextView timestamp;
        TextView amount;
        public MyViewHolder(View itemView) {
            super(itemView);

            secondParty=itemView.findViewById(R.id.second_party);
            timestamp= itemView.findViewById(R.id.date_time);
            amount=itemView.findViewById(R.id.amount);
        }
    }
}
