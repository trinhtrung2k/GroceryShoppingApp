package com.trinhtrung.groceryshoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.trinhtrung.groceryshoppingapp.activities.DetailedActivity;
import com.trinhtrung.groceryshoppingapp.models.MyCartModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlacedOrderActivity extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed_order);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        List<MyCartModel> list = (ArrayList<MyCartModel>) getIntent().getSerializableExtra("itemList");

        if (list != null && list.size() >0){
                for (MyCartModel myCartModel: list){
                    final HashMap<String,Object> cartMap = new HashMap<>();
                    cartMap.put("productName", myCartModel.getProductName());
                    cartMap.put("productPrice", myCartModel.getProductPrice());
                    cartMap.put("productDate", myCartModel.getProductDate());
                    cartMap.put("productTime", myCartModel.getProductTime());
                    cartMap.put("totalQuantity", myCartModel.getTotalQuantity());
                    cartMap.put("totalPrice", myCartModel.getTotalPrice());

                    db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                            .collection("MyOrder").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(PlacedOrderActivity.this, "Your Order Has Been Placed", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }
        }



    }
}