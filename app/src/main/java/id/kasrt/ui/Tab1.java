package id.kasrt.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import id.kasrt.ChatActivity;
import id.kasrt.DataWarga;
import id.kasrt.LaporanActivity;
import id.kasrt.R;


public class Tab1 extends Fragment {

    public Tab1() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set OnClickListener for CardView
        CardView cdMenu1 = view.findViewById(R.id.cdMenu1);
        cdMenu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ChatActivity when CardView is clicked
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });

        CardView cdMenu2 = view.findViewById(R.id.cdMenu2);
        cdMenu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ChatActivity when CardView is clicked
                Intent intent = new Intent(getActivity(), DataWarga.class);
                startActivity(intent);
            }
        });

        CardView cdMenu3 = view.findViewById(R.id.cdMenu3);
        cdMenu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ChatActivity when CardView is clicked
                Intent intent = new Intent(getActivity(), LaporanActivity.class);
                startActivity(intent);
            }
        });
    }
}