package com.trinhtrung.groceryshoppingapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.trinhtrung.groceryshoppingapp.R;
import com.trinhtrung.groceryshoppingapp.activities.ViewAllActivity;
import com.trinhtrung.groceryshoppingapp.adapters.HomeCategoryAdapter;
import com.trinhtrung.groceryshoppingapp.adapters.PopularAdapters;
import com.trinhtrung.groceryshoppingapp.adapters.RecommendedAdapter;
import com.trinhtrung.groceryshoppingapp.models.HomeCategoryModel;
import com.trinhtrung.groceryshoppingapp.models.PopularModel;
import com.trinhtrung.groceryshoppingapp.models.RecommendedModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    ScrollView scrollView;
    ProgressBar progressBar;
    RecyclerView popularRec, homeCatRec, recommendedRec;
    FirebaseFirestore db;
    //Popular
    List<PopularModel> popularModelList;
    PopularAdapters popularAdapters;

    //Home Category
     List<HomeCategoryModel> homeCategoryModelList;
     HomeCategoryAdapter homeCategoryAdapter;

     //Recommended
    List<RecommendedModel> recommendedModelList;
    RecommendedAdapter recommendedAdapter;

    TextView popularShowAll, exploreShowAll, recommendedShowAll;
    //view_all_explore,view_all_popular,view_all_recommended



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,container,false);
        db = FirebaseFirestore.getInstance();
        popularShowAll = view.findViewById(R.id.view_all_popular);
        exploreShowAll = view.findViewById(R.id.view_all_explore);
        recommendedShowAll = view.findViewById(R.id.view_all_recommended);

        popularShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllActivity.class);
                startActivity(intent);
            }
        });
        exploreShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllActivity.class);
                startActivity(intent);
            }
        });
        recommendedShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllActivity.class);
                startActivity(intent);
            }
        });

        scrollView = view.findViewById(R.id.scroll_view);
        progressBar = view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);


        //adapter popular
         popularRec = view.findViewById(R.id.pop_rec);
         popularRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,false));
         popularModelList = new ArrayList<>();
         popularAdapters = new PopularAdapters(getActivity(),popularModelList);
         popularRec.setAdapter(popularAdapters);

         //adapter home category
        homeCatRec  = view.findViewById(R.id.explore_rec);
        homeCatRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        homeCategoryModelList = new ArrayList<>();
        homeCategoryAdapter = new HomeCategoryAdapter(getActivity(),homeCategoryModelList);
        homeCatRec.setAdapter(homeCategoryAdapter);

        //adapter recommended
        recommendedRec = view.findViewById(R.id.recommended_rec);
        recommendedRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        recommendedModelList = new ArrayList<>();
        recommendedAdapter = new RecommendedAdapter(getActivity(), recommendedModelList);
        recommendedRec.setAdapter(recommendedAdapter);


 // =======Popular Products
        db.collection("PopularProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PopularModel popularModel = document.toObject(PopularModel.class);
                                popularModelList.add(popularModel);
                                popularAdapters.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                scrollView.setVisibility(View.VISIBLE);

                            }
                        } else {
                            Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // =========Home Category
        db.collection("HomeCategory")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                HomeCategoryModel homeCategoryModel = document.toObject(HomeCategoryModel.class);
                                homeCategoryModelList.add(homeCategoryModel);
                                homeCategoryAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //========== Recommended
        db.collection("Recommended")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                RecommendedModel recommendedModel = document.toObject(RecommendedModel.class);
                                recommendedModelList.add(recommendedModel);
                                recommendedAdapter.notifyDataSetChanged();
                            }
                        }else {
                            Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return view;
    }


}