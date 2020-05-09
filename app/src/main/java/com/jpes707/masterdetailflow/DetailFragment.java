package com.jpes707.masterdetailflow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class DetailFragment extends Fragment {
    private static final String STORAGE_KEY = "goals";
    private Gson mGson = new GsonBuilder().create();
    private SharedPreferences mShared;
    private SharedPreferences.Editor mEditor;
    private String[] goals;

    private int goalNumber;

    private DetailFragmentListener mCallback;

    private TextView mTextView;
    private EditText mGoal;

    public DetailFragment() {
        // Required empty public constructor
    }

    static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get the views
        mTextView = view.findViewById(R.id.text_view);
        mGoal = view.findViewById(R.id.edit_text);

        mShared = Objects.requireNonNull(getContext()).getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE);
        mEditor = mShared.edit();

        view.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goals[goalNumber - 1] = mGoal.getText().toString();
                mEditor.putString(STORAGE_KEY, mGson.toJson(goals));
                mEditor.apply();
                mCallback.onDetailFragmentAction();
            }
        });

        view.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onDetailFragmentAction();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        goals = mGson.fromJson(mShared.getString(STORAGE_KEY, "[\"(Click to set goal 1)\", \"(Click to set goal 2)\", \"(Click to set goal 3)\"]"), String[].class);
        goalNumber = mShared.getInt("goalNumber", 0);
        updateDisplay(goalNumber);
    }

    /* ------------------------------------*/
    /*   custom helper method              */
    /*   this is called by MainActivity    */

    @SuppressLint("SetTextI18n")
    private void updateDisplay(int goalNumber){
        mTextView.setText("Editing goal " + goalNumber);
        String text = goals[goalNumber - 1];
        if (text.length() > 20 && text.substring(0, 19).equals("(Click to set goal ")) {
            text = "";
        }
        mGoal.setText(text);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DetailFragmentListener) {
            mCallback = (DetailFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " (Main Activity) must implement DetailFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public interface DetailFragmentListener {
        void onDetailFragmentAction();
    }
}
