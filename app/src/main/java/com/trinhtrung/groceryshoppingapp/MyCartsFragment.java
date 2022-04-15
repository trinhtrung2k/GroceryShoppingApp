package com.trinhtrung.groceryshoppingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.trinhtrung.groceryshoppingapp.adapters.MyCartAdapter;
import com.trinhtrung.groceryshoppingapp.models.MyCartModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MyCartsFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseAuth auth;
    FirebaseFirestore db;
    MyCartAdapter myCartAdapter;
    List<MyCartModel> myCartModelList;
    TextView overTotalAmount;
    ProgressBar progressBar;
    Button buyNow;
    int totalBill;


    public MyCartsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_carts, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        overTotalAmount  = view.findViewById(R.id.textView6);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,new IntentFilter("MyTotalAmount"));

        buyNow = view.findViewById(R.id.buy_now);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myCartModelList = new ArrayList<>();
        myCartAdapter = new MyCartAdapter(getActivity(),myCartModelList);
        recyclerView.setAdapter(myCartAdapter);

        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                                String documentId = documentSnapshot.getId();
                                MyCartModel myCartModel = documentSnapshot.toObject(MyCartModel.class);
                                myCartModel.setDocumentId(documentId);
                                myCartModelList.add(myCartModel);
                                myCartAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }else {
                            Toast.makeText(getActivity(), "Error:" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),PlacedOrderActivity.class);
                intent.putExtra("itemList", (Serializable) myCartModelList);
                startActivity(intent);

            }
        });
        return view;
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           int totalBill = intent.getIntExtra("totalAmount",0);
           overTotalAmount.setText("Total Bill : " + totalBill + " VND");

        }
    };
}