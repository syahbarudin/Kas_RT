package id.kasrt.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.kasrt.QrScannerActivity;
import id.kasrt.R;

public class ScanFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Intent intent = new Intent(getActivity(), QrScannerActivity.class);
        startActivity(intent);

        // Since we are navigating away from the fragment, return null for the view
        return null;
    }
}
