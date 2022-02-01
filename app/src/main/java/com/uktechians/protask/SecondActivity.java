package com.uktechians.protask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.uktechians.protask.model.Applicable;
import com.uktechians.protask.model.InputElement;

import java.util.List;

public class SecondActivity extends AppCompatActivity {

    Applicable applicable;
    List<InputElement> inputElement;
    LinearLayout linearLayout;
    @SuppressLint({"ResourceAsColor", "UseCompatLoadingForColorStateLists"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        linearLayout = findViewById(R.id.rootView);

        String str  = getIntent().getStringExtra("dataClass");
        if (str!=null){
            applicable = new Gson().fromJson(str, Applicable.class);
            Log.d("Tag", applicable.toString());
        }
        if (applicable!=null){
            if (applicable.getInputElements().size()>0){
                TextInputLayout.LayoutParams editTextParams = new TextInputLayout.LayoutParams(
                        TextInputLayout.LayoutParams.MATCH_PARENT, TextInputLayout.LayoutParams.WRAP_CONTENT);

                for (InputElement inputElement: applicable.getInputElements()){
                    TextInputEditText textInputEditText = new TextInputEditText(this);

                    textInputEditText.setTextSize(18);
                    textInputEditText.setTextColor(getResources().getColor(R.color.black));
                    textInputEditText.setHint(inputElement.getName());
                    textInputEditText.setHintTextColor(getResources().getColor(R.color.black));
                    textInputEditText.setPadding(20,20,20,20);
                    textInputEditText.setBackgroundColor(getResources().getColor(R.color.teal_700));

                    editTextParams.setMargins(10,10,10,10);
                    linearLayout.addView(textInputEditText, editTextParams);
                }
            }
        }
    }
}