package com.kinfo.pixelart.tabs.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.kinfo.pixelart.R;
import com.kinfo.pixelart.SplitImage;
import com.kinfo.pixelart.activity.DrawingActivity;
import com.kinfo.pixelart.model.RGB;
import com.kinfo.pixelart.pixelate.Pixelate;
import com.kinfo.pixelart.utils.ImagePixelization;
import com.kinfo.pixelart.utils.TouchImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.dionsegijn.pixelate.OnPixelateListener;

/**
 * Created by kinfo on 4/17/2018.
 */

public class ColorByNo extends AppCompatActivity implements Animation.AnimationListener {

    private AdView mAdView;
    private ImageView back_btn,tick;
    private ImageView minus,plus;
    private TouchImageView pixel_image;
    private int image;
    private String image_path="";
    Animation zoom_in_anim,zoom_out_anim;

    final private static int SEEKBAR_ANIMATION_DURATION = 10000;
    final private static int TIME_BETWEEN_TASKS = 400;
    final private static int SEEKBAR_STOP_CHANGE_DELTA = 5;
    final private static float PROGRESS_TO_PIXELIZATION_FACTOR = 0.0155f;
    Bitmap mImageBitmap;
    TouchImageView mImageView;
    boolean mIsChecked = false;
    boolean mIsBuiltinPixelizationChecked = false;
    int mLastProgress = 0;
    long mLastTime = 0;
    Bitmap mPixelatedBitmap;
    List<String> pixel_color = new ArrayList<String>();
    ArrayList<RGB> rgb_color = new ArrayList<RGB>();
    ArrayList<String> rgb_value = new ArrayList<String>();
    RGB rgb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        setContentView(R.layout.color_by_no);

        mAdView = (AdView) findViewById(R.id.adView);
        minus = (ImageView) findViewById(R.id.minus);
        plus = (ImageView) findViewById(R.id.plus);
        pixel_image = (TouchImageView) findViewById(R.id.pixel_image);
        //pixel_image.setMinimumDpi(50);
        //pixel_image.setMaxScale(2F);
        back_btn = (ImageView) findViewById(R.id.back_btn);
        tick = (ImageView) findViewById(R.id.tick);

        zoom_in_anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        zoom_out_anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out);

        zoom_in_anim.setAnimationListener(ColorByNo.this);
        zoom_out_anim.setAnimationListener(ColorByNo.this);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                image= 0;
            } else {
                if(extras.containsKey("image")) {
                    image = extras.getInt("image");
                    //pixel_image.setImage(ImageSource.resource(image));
                    //pixel_image.setImageResource(image);
                    mImageBitmap = BitmapFactory.decodeResource(getResources(),image);
                }
                if(extras.containsKey("image_path")) {

                   /* File sd = Environment.getExternalStorageDirectory();
                    File image = new File(sd+extras.getString("image_path"), "New Pic");
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
                    bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);*/
                    image_path = extras.getString("image_path");
                    mImageBitmap = BitmapFactory.decodeFile(image_path);
                    //Picasso.with(ColorByNo.this).load(new File(image_path)).resize(140, 140).centerCrop().into(pixel_image);
                }

              /*  mImageBitmap = BitmapFactory.decodeResource(getResources(),image);
                //pixel_image.setImageBitmap(mImageBitmap);
                new Pixelate(mImageBitmap)
                        .setDensity(1)
                        .setListener(new OnPixelateListener() {
                            @Override
                            public void onPixelated(Bitmap bitmap, int density) {
                                pixel_image.setImageBitmap(mImageBitmap);
                            }
                        })
                        .make();*/

            }
        } else {
           /* image= (Integer) savedInstanceState.getSerializable("image");
            pixel_image.setImage(ImageSource.resource(image));*/

        }

        pixel_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(ColorByNo.this,DrawingActivity.class);
                if(!image_path.equalsIgnoreCase("")) {
                    intent.putExtra("image_path", image_path);
                }
                startActivity(intent);*/
            }
        });


        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ColorByNo.this,Share.class);
                intent.putExtra("image",image);
                startActivity(intent);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pixel_image.startAnimation(zoom_out_anim);
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pixel_image.startAnimation(zoom_in_anim);
            }
        });

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice("675C9940F5CF016DA24764D3B58BD0C4")
                .build();

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {
                Toast.makeText(ColorByNo.this, "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(ColorByNo.this, "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                Toast.makeText(ColorByNo.this, "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        mAdView.loadAd(adRequest);


        //pixel_image.setImageBitmap(mImageBitmap);
        invokePixelization();

    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void invokePixelization () {
       // mLastTime = System.currentTimeMillis();
        //mLastProgress = mSeekBar.getProgress();
        if (mIsChecked) {
            PixelizeImageAsyncTask asyncPixelateTask = new PixelizeImageAsyncTask();
            asyncPixelateTask.execute(PROGRESS_TO_PIXELIZATION_FACTOR,
                    mImageBitmap);
        } else {
            pixel_image.setImageDrawable(pixelizeImage(PROGRESS_TO_PIXELIZATION_FACTOR, mImageBitmap));
            // 0.013


          /*  Bitmap bitmap = ((BitmapDrawable)pixelizeImage(mSeekBar.getProgress()
                    / PROGRESS_TO_PIXELIZATION_FACTOR, mImageBitmap)).getBitmap();
            mImageView.setImage(ImageSource.bitmap(bitmap));*/
        }
    }

    public void linBackArrow(View view) {

        finish();
    }


    private class PixelizeImageAsyncTask extends AsyncTask<Object, Void, BitmapDrawable> {
        @Override
        protected BitmapDrawable doInBackground(Object... params) {
            float pixelizationFactor = (Float)params[0];
            Bitmap originalBitmap = (Bitmap)params[1];
            return pixelizeImage(pixelizationFactor, originalBitmap);
        }
        @Override
        protected void onPostExecute(BitmapDrawable result) {
            pixel_image.setImageDrawable(result);


            /*Bitmap bitmap = ((BitmapDrawable)result).getBitmap();
            mImageView.setImage(ImageSource.bitmap(bitmap));*/
        }

        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public BitmapDrawable pixelizeImage(float pixelizationFactor, Bitmap bitmap) {
        if (mIsBuiltinPixelizationChecked) {
            return builtInPixelization(pixelizationFactor, bitmap);
        } else {
            return customImagePixelization(pixelizationFactor, bitmap);
        }
    }

    public BitmapDrawable builtInPixelization(float pixelizationFactor, Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int downScaleFactorWidth = (int)(pixelizationFactor * width);
        downScaleFactorWidth = downScaleFactorWidth > 0 ? downScaleFactorWidth : 1;
        int downScaleFactorHeight = (int)(pixelizationFactor * height);
        downScaleFactorHeight = downScaleFactorHeight > 0 ? downScaleFactorHeight : 1;
        int downScaledWidth =  width / downScaleFactorWidth;
        int downScaledHeight = height / downScaleFactorHeight;
        Bitmap pixelatedBitmap = Bitmap.createScaledBitmap(bitmap, downScaledWidth,
                downScaledHeight, false);
        /* Bitmap's createScaledBitmap method has a filter parameter that can be set to either
         * true or false in order to specify either bilinear filtering or point sampling
         * respectively when the bitmap is scaled up or now.
         *
         * Similarly, a BitmapDrawable also has a flag to specify the same thing. When the
         * BitmapDrawable is applied to an ImageView that has some scaleType, the filtering
         * flag is taken into consideration. However, for optimization purposes, this flag was
         * ignored in BitmapDrawables before Jelly Bean MR1.
         *
         * Here, it is important to note that prior to JBMR1, two bitmap scaling operations
         * are required to achieve the pixelization effect. Otherwise, a BitmapDrawable
         * can be created corresponding to the downscaled bitmap such that when it is
         * upscaled to fit the ImageView, the upscaling operation is a lot faster since
         * it uses internal optimizations to fit the ImageView.
         * */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), pixelatedBitmap);
            bitmapDrawable.setFilterBitmap(false);
            return bitmapDrawable;
        } else {
            Bitmap upscaled = Bitmap.createScaledBitmap(pixelatedBitmap, width, height, false);
            return new BitmapDrawable(getResources(), upscaled);
        }
    }

    public BitmapDrawable customImagePixelization(float pixelizationFactor, Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();


        if (mPixelatedBitmap == null || !(width == mPixelatedBitmap.getWidth() && height ==
                mPixelatedBitmap.getHeight())) {
            mPixelatedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
        int xPixels = (int) (pixelizationFactor * ((float)width));
        xPixels = xPixels > 0 ? xPixels : 1;
        int yPixels = (int)  (pixelizationFactor * ((float)height));
        yPixels = yPixels > 0 ? yPixels : 1;
        int pixel = 0,pixel_c=0, red = 0, green = 0, blue = 0,red_new = 0, green_new = 0, blue_new = 0,red_color = 211, green_color = 211, blue_color = 211, numPixels = 0;
        int[] bitmapPixels = new int[width * height];
        bitmap.getPixels(bitmapPixels, 0, width, 0, 0, width, height);
        int[] pixels = new int[yPixels * xPixels];
        int maxX, maxY;

        for (int y = 0; y < height; y+=yPixels)
        {
            for (int x = 0; x < width; x+=xPixels)
            {
                numPixels = red = green = blue = 0;
                maxX = Math.min(x + xPixels, width);
                maxY = Math.min(y + yPixels, height);
                for (int i = x; i < maxX; i++) {
                    for (int j = y; j < maxY; j++) {
                        pixel = bitmapPixels[j * width + i];
                        red += Color.red(pixel);
                        green += Color.green(pixel);
                        blue += Color.blue(pixel);

                        red_new = (red+green+blue)/3;
                        green_new = (red+green+blue)/3;
                        blue_new = (red+green+blue)/3;

                      /*  Log.e("red",red+"");
                        Log.e("green",green+"");
                        Log.e("blue",blue+"");*/
                        numPixels ++;

                    }
                }

                pixel = Color.rgb(red_new / numPixels, green_new / numPixels, blue_new / numPixels);
                rgb = new RGB();
                rgb.setRed(red / numPixels);
                rgb.setGreen(green / numPixels);
                rgb.setBlue(blue / numPixels);
                rgb.setRgb_value(red/ numPixels+" "+green/ numPixels+" "+blue/ numPixels);

                rgb_value.add(rgb.getRgb_value());
                rgb_color.add(rgb);
                
                //pixel_c = Color.rgb(211/ numPixels , 211/ numPixels , 211/ numPixels);
                //Log.i("pixel",pixel+"");
                //Log.e("red green blue",red+" "+green+" "+blue);
                //Log.e("pixel red green blue",red / numPixels+" "+green / numPixels+" "+blue / numPixels);
                //if(pixel==pixel){
                //pixel_color.add(red / numPixels+" "+green / numPixels+" "+blue / numPixels);

                //}
                Arrays.fill(pixels, pixel);
                int w = Math.min(xPixels, width - x);
                int h = Math.min(yPixels, height - y);
                mPixelatedBitmap.setPixels(pixels, 0 , w, x , y, w, h);

              /*  if(pixel_color.contains(red+" "+green+" "+blue)){

                }else{
                    pixel_color.add(pixel+" "+red+" "+green+" "+blue);
                    //Log.i("pixelValues",pixel+" "+red+" "+green+" "+blue);
                }*/
            }
        }

        /*for(int i=0; i<pixel_color.size();i++){

            //Log.e("pixelListColor",pixel_color.get(i)+"");
        }*/
       /* Collections.sort(pixel_color);
        System.out.println("\nHere are the duplicate elements from list : " + findDuplicates(pixel_color));
        System.out.println("\nSize of list : " + findDuplicates(pixel_color).size());
        Set<Integer> set= new HashSet<Integer>();
        set=findDuplicates(pixel_color);
        Iterator itr = set.iterator();*/
       /* while(itr.hasNext())
        {*/

      /*  System.out.println("\nExample 2 - Count all with frequency");
        Set<Integer> uniqueSet = new HashSet<Integer>(pixel_color);
        for (int temp : uniqueSet) {
            System.out.println(temp + ": " + Collections.frequency(pixel_color, temp));
        }*/



           /* for(int i =0; i<10 ;i++) {
                System.out.println("itr.next>>>>>>>>>>"+itr.next());
                //String hexColor = String.format("#%06X", (0xFFFFFF & (int)itr.next()));
                int red_rgb = Color.red((int)itr.next());
                int green_rgb = Color.green((int)itr.next());
                int blue_rgb = Color.blue((int)itr.next());
                int alpha_rgb = Color.alpha((int)itr.next());

                System.out.println("rgb color>>>>>>>>>>>>>>"+red_rgb+" "+ green_rgb+" "+ blue_rgb);
            }*/
        //}
        /*Set<Integer> uniqueSet = new HashSet<Integer>(pixel_color);
        for (int temp : uniqueSet) {
            System.out.println("print>>>>>>>>"+temp + ": " + Collections.frequency(pixel_color, temp));
        }*/
        if(rgb_color.size()>0) {
            Log.i("rgbColor Size",rgb_color.size()+"");
            for (int i = 0; i <10;i++){
                Log.i("rgb colors>>>>>>",rgb_color.get(i).getRed()+" "+rgb_color.get(i).getGreen()+" "+rgb_color.get(i).getBlue());
            }

            ArrayList<String> newList = removeDuplicates(rgb_value);
            Log.i("newList Size",newList.size()+"");
            for (int i = 0; i <10;i++){
                Log.i("newList colors>>>>>>",newList.get(i)+" "+newList.get(i)+" "+newList.get(i));
            }

        }
        return new BitmapDrawable(getResources(), mPixelatedBitmap);
    }

    static ArrayList<String> removeDuplicates(ArrayList<String> list) {

        // Store unique items in result.
        ArrayList<String> result = new ArrayList<>();

        // Record encountered Strings in HashSet.
        HashSet<String> set = new HashSet<>();

        // Loop over argument list.
        for (String item : list) {

            // If String is not in set, add it to the list and the set.
            if (!set.contains(item)) {
                result.add(item);
                set.add(item);
            }
        }
        return result;
    }

}
