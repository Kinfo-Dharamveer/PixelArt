package com.kinfo.pixelart.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kinfo.pixelart.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageShareActivity extends AppCompatActivity {


    @BindView(R.id.imageBackArrow) ImageView imageBackArrow;
    @BindView(R.id.imageDraw) ImageView imageDraw;

    @BindView(R.id.rlSquare) RelativeLayout rlSquare;
    @BindView(R.id.rlCircle) RelativeLayout rlCircle;
    @BindView(R.id.rlPolygon) RelativeLayout rlPolygon;
    @BindView(R.id.rlTriangle) RelativeLayout rlTriangle;
    @BindView(R.id.rlHeart) RelativeLayout rlHeart;
    @BindView(R.id.rlPentagon) RelativeLayout rlPentagon;
    @BindView(R.id.rlDiamond) RelativeLayout rlDiamond;
    @BindView(R.id.rlCircleCircle) RelativeLayout rlCircleCircle;
    @BindView(R.id.rlStar) RelativeLayout rlStar;

    @BindView(R.id.showingLayout) LinearLayout showingLayout;
    @BindView(R.id.hiddenLayout) LinearLayout hiddenLayout;

    @BindView(R.id.btnShare) Button btnShare;
    @BindView(R.id.tvTapText) TextView tvTapText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_share);
        ButterKnife.bind(this);


        SpannableString ss = new SpannableString("Tap #No.Draw to copy \n this hashtag");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                Dialog dialog = new Dialog(ImageShareActivity.this);
                dialog.setContentView(R.layout.no_draw_dialog);
                Window dialogWindow = dialog.getWindow();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();

                ImageView imageView  = dialog.findViewById(R.id.imageCircleAnimation);

                ((Animatable)imageView.getDrawable()).start();

                dialogWindow.setGravity(Gravity.CENTER);



                lp.width = 500; // Width
                lp.height = 500; // Height

                dialogWindow.setAttributes(lp);
                dialog.show();


            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 4, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        tvTapText.setText(ss);
        tvTapText.setMovementMethod(LinkMovementMethod.getInstance());
        tvTapText.setHighlightColor(Color.TRANSPARENT);
    }

    public void squareClick(View view) {

        RelativeLayout.LayoutParams imParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ImageView imageTick = new ImageView(this);
        imageTick.setImageResource(R.drawable.tick);

        imageTick.setLayoutParams(imParams);


        rlSquare.setBackgroundColor(getResources().getColor(R.color.grey));
    }



    private boolean isPanelShown() {
        return hiddenLayout.getVisibility() == View.VISIBLE;
    }

    public void circleClick(View view) {
        rlCircle.setBackgroundColor(getResources().getColor(R.color.grey));

    }

    public void polygonClick(View view) {
        rlPolygon.setBackgroundColor(getResources().getColor(R.color.grey));

    }

    public void triangleClick(View view) {
        rlTriangle.setBackgroundColor(getResources().getColor(R.color.grey));

    }

    public void heartClick(View view) {
        rlHeart.setBackgroundColor(getResources().getColor(R.color.grey));

    }

    public void pentagonClick(View view) {
        rlPentagon.setBackgroundColor(getResources().getColor(R.color.grey));

    }

    public void diamondClick(View view) {
        rlDiamond.setBackgroundColor(getResources().getColor(R.color.grey));

    }

    public void circleCirClick(View view) {
        rlCircleCircle.setBackgroundColor(getResources().getColor(R.color.grey));

    }

    public void starClick(View view) {
        rlStar.setBackgroundColor(getResources().getColor(R.color.grey));

    }

    public void btnShareClick(View view) {

        showingLayout.setVisibility(View.GONE);

        if (!isPanelShown()) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_up);


            hiddenLayout.startAnimation(bottomUp);
            hiddenLayout.setVisibility(View.VISIBLE);
        }
        else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_down);

            hiddenLayout.startAnimation(bottomDown);
            hiddenLayout.setVisibility(View.GONE);
        }
    }


    public void imageBackArrow(View view) {
        finish();
    }

    public void rlDownloads(View view) {

        Object drawable = imageDraw.getDrawable();
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
        Bitmap bitmap = bitmapDrawable .getBitmap();
        saveToInternalStorage(bitmap);

    }

    public void rlInstagram(View view) {
        String type = "image/*";
        String imageName = String.valueOf(imageDraw.getTag());
        //String filename = "/myPhoto.jpg";
        String filename = "/"+imageName+".jpg";
        String mediaPath = Environment.getExternalStorageDirectory() + filename;

        if(isAppInstalled("    com.instagram.android"))
            createInstagramIntent(type, mediaPath);
        else
            Toast.makeText(this, "Instagram is not installed", Toast.LENGTH_SHORT).show();


    }

    public void rlShare(View view) {
    }


    private boolean isAppInstalled(String packageName) {
        PackageManager pm = getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    private void createInstagramIntent(String type, String mediaPath){

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Create the URI from the media
        File media = new File(mediaPath);
        Uri uri = Uri.fromFile(media);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                Toast.makeText(this, "Saved to Storage", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
}
