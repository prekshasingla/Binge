package com.example.prakharagarwal.binge;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class WalletActivityFragment extends Fragment {

    TextView walletBalance,emptyView;
    List<Transaction> transactionList;
    WalletTransactionsAdapter walletTransactionsAdapter;
    ProgressBar balanceProgress,transactionProgress;


    public WalletActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_wallet, container, false);
        walletBalance = rootview.findViewById(R.id.wallet_balance);
        balanceProgress=rootview.findViewById(R.id.balance_progress);
        transactionProgress=rootview.findViewById(R.id.transaction_progress);
        emptyView=rootview.findViewById(R.id.empty_view);

        transactionList = new ArrayList<>();
        walletTransactionsAdapter=new WalletTransactionsAdapter(getActivity(),transactionList);
        RecyclerView transactionRecycler= rootview.findViewById(R.id.transaction_history_recycler);
        transactionRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        transactionRecycler.setAdapter(walletTransactionsAdapter);

        getWallet();
        getTransactions();
        return rootview;
    }

    private void getTransactions() {
        SharedPreferences prefs = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String uID = prefs.getString("username", null);
        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("users_app/" + encodeEmail(uID) + "/wallet_transactions")
                .orderBy("timestamp",Query.Direction.DESCENDING)
                .get()

                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    Map<String, Object> data = document.getData();
                                    Transaction transaction = new Transaction();
                                    transaction.crDr = ""+ data.get("credit_debit");
                                    if (Integer.parseInt(transaction.crDr) == 1) {
                                        transaction.amount = ""+ data.get("credit_amount");
                                    } else if (Integer.parseInt(transaction.crDr)== 2) {
                                        transaction.amount = ""+ data.get("debit_amount");

                                    }
                                    transaction.timestamp = ""+ data.get("timestamp");
                                    transaction.secondParty = (String) data.get("second_party");
                                    transactionList.add(transaction);

                                } else {

                                }
                            }
                        }
                        if(transactionList.size()>0){
                            emptyView.setVisibility(View.GONE);
                        }else
                            emptyView.setVisibility(View.VISIBLE);

                        walletTransactionsAdapter.notifyDataSetChanged();
                        transactionProgress.setVisibility(View.INVISIBLE);

                    }

                });
    }

    private void getWallet() {
        SharedPreferences prefs = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String uID = prefs.getString("username", null);
        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("users_app").document(encodeEmail(uID))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                if (document.get("wallet_balance") != null)
                                    walletBalance.setText(document.get("wallet_balance") + "");

                            } else {

                            }
                        } else {
                        }
                        balanceProgress.setVisibility(View.INVISIBLE);

                    }
                });
    }

    public String encodeEmail(String email) {
        return email.replace(".", getString(R.string.encode_period))
                .replace("@", getString(R.string.encode_attherate))
                .replace("$", getString(R.string.encode_dollar))
                .replace("[", getString(R.string.encode_left_square_bracket))
                .replace("]", getString(R.string.encode_right_square_bracket));
    }

    class Transaction {
        String amount;
        String crDr;
        String secondParty;
        String timestamp;

    }
}
