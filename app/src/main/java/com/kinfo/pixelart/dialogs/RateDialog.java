package com.kinfo.pixelart.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.kinfo.pixelart.R;

public class RateDialog extends Dialog {

    public RateDialog(@NonNull Context context) {
        super(context);
    }

    private Button btnRate;
    private TextView tvNextTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rate_dialog);


        btnRate = findViewById(R.id.btnRate);
        tvNextTime = findViewById(R.id.tvNextTime);

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.theopen.android"));
                getContext().startActivity(intent);

            }
        });


        tvNextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();


            }
        });
    }
}
