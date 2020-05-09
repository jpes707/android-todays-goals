package com.jpes707.masterdetailflow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class MasterFragment extends Fragment implements View.OnClickListener {
    private static final String STORAGE_KEY = "goals";
    private Gson mGson = new GsonBuilder().create();
    private SharedPreferences mShared;
    private String[] goals;
    private MasterFragmentListener mCallback;
    private TextView mButton1, mButton2, mButton3;

    public MasterFragment() {
    }

    static MasterFragment newInstance() {
        return new MasterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShared = Objects.requireNonNull(getContext()).getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_master, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTasks();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mButton1 = view.findViewById(R.id.goalButton1);
        mButton2 = view.findViewById(R.id.goalButton2);
        mButton3 = view.findViewById(R.id.goalButton3);

        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);

        updateTasks();

        view.findViewById(R.id.reset_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goals = new String[]{"(Click to set goal 1)", "(Click to set goal 2)", "(Click to set goal 3)"};
                SharedPreferences.Editor mEditor = mShared.edit();
                mEditor.putString(STORAGE_KEY, mGson.toJson(goals));
                mEditor.apply();
                updateTasks();
            }
        });

    }

    @Override
    public void onClick(View v) {

        int itemNo = -1;

        switch (v.getId()) {
            case R.id.goalButton1:
                itemNo = 0;
                break;
            case R.id.goalButton2:
                itemNo = 1;
                break;
            case R.id.goalButton3:
                itemNo = 2;
                break;
            default:
                break;
        }
        mCallback.onMasterFragmentData(itemNo);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MasterFragmentListener) {
            mCallback = (MasterFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " (MainActivity) must implement MasterFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface MasterFragmentListener {
        void onMasterFragmentData(int i);
    }

    @SuppressLint("SetTextI18n")
    private void updateTasks() {
        goals = mGson.fromJson(mShared.getString(STORAGE_KEY, "[\"(Click to set goal 1)\", \"(Click to set goal 2)\", \"(Click to set goal 3)\"]"), String[].class);
        mButton1.setText(goals[0]);
        mButton2.setText(goals[1]);
        mButton3.setText(goals[2]);
    }
}

