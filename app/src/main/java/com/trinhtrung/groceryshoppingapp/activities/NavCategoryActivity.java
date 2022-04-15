package com.trinhtrung.groceryshoppingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.trinhtrung.groceryshoppingapp.R;
import com.trinhtrung.groceryshoppingapp.adapters.NavCategoryDetailedAdapter;
import com.trinhtrung.groceryshoppingapp.models.NavCategoryDetailedModel;
import com.trinhtrung.groceryshoppingapp.models.PopularModel;
import com.trinhtrung.groceryshoppingapp.models.ViewAllModel;

import java.util.ArrayList;
import java.util.List;

public class NavCategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NavCategoryDetailedAdapter navCategoryDetailedAdapter;
    List<NavCategoryDetailedModel> navCategoryDetailedModelList;
    FirebaseFirestore db;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_category);
        String type = getIntent().getStringExtra("type");

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.nav_cat_det_rec);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        navCategoryDetailedModelList = new ArrayList<>();
        navCategoryDetailedAdapter = new NavCategoryDetailedAdapter(this,navCategoryDetailedModelList);
        recyclerView.setAdapter(navCategoryDetailedAdapter);

        //getting drink
        if (type != null && type.equalsIgnoreCase("drink")) {
            db.collection("NavCategoryDetailed").whereEqualTo("type","drink")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    NavCategoryDetailedModel navCategoryDetailedModel = document.toObject(NavCategoryDetailedModel.class);
                                    navCategoryDetailedModelList.add(navCategoryDetailedModel);
                                    navCategoryDetailedAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);

                                }
                            } else {
                                Toast.makeText(NavCategoryActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        //getting
        //getting drink
        if (type != null && type.equalsIgnoreCase("meat and seafood")) {
            db.collection("NavCategoryDetailed").whereEqualTo("type","meat and seafood")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    NavCategoryDetailedModel navCategoryDetailedModel = document.toObject(NavCategoryDetailedModel.class);
                                    navCategoryDetailedModelList.add(navCategoryDetailedModel);
                                    navCategoryDetailedAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);

                                }
                            } else {
                                Toast.makeText(NavCategoryActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }

    }
}