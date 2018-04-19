package com.kinfo.pixelart.tabs.gallery;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.kinfo.pixelart.R;
import com.kinfo.pixelart.SplitImage;
import com.kinfo.pixelart.utils.TouchImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Created by kinfo on 4/13/2018.
 */

public class GalleryFragment extends Fragment {

    ViewGroup rootView;
    LinearLayout watch_video,subscribe_to_unlock;
    private RewardedVideoAd mRewardedVideoAd;
    TouchImageView sourceImage;
    Uri selectedImage;
    private final int RESULT_LOAD_IMAGE = 1;
    int chunkSideLength = 50;

    ArrayList<Bitmap> chunkedImage;

    // Number of rows and columns in chunked image
    int rows, cols;
    Context context;
    int  result1, result3;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.gallery, container, false);

        watch_video = (LinearLayout) rootView.findViewById(R.id.watch_video);
        subscribe_to_unlock = (LinearLayout) rootView.findViewById(R.id.subscribe_to_unlock);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
        mRewardedVideoAd.setRewardedVideoAdListener(rewardedVideoAdListener);

        watch_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRewardedVideoAd();
            }
        });

        subscribe_to_unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        return rootView;
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(getString(R.string.rewarded_video),
                new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice("675C9940F5CF016DA24764D3B58BD0C4")
                        .build());

        // showing the ad to user

    }

    private void showRewardedVideo() {
        // make sure the ad is loaded completely before showing it
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onResume() {
        mRewardedVideoAd.resume(getActivity());
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(getActivity());
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(getActivity());
        super.onDestroy();
    }

    RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
        @Override
        public void onRewardedVideoAdLoaded() {
            Toast.makeText(getActivity(), "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
            showRewardedVideo();
        }

        @Override
        public void onRewardedVideoAdOpened() {
            Toast.makeText(getActivity(), "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewardedVideoStarted() {
            Toast.makeText(getActivity(), "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewardedVideoAdClosed() {
            Toast.makeText(getActivity(), "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
            ////// UpdateDataBase
            loadImageFromGallery();
        }

        @Override
        public void onRewarded(RewardItem reward) {
            Toast.makeText(getActivity(), getString(R.string.rewarded_video) + " " +  reward.getAmount() + " " + reward.getType(), Toast.LENGTH_LONG).show();
            // Reward the user.
            loadImageFromGallery();
        }

        @Override
        public void onRewardedVideoAdLeftApplication() {
            Toast.makeText(getActivity(), "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewardedVideoAdFailedToLoad(int i) {
            Toast.makeText(getActivity(), "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewardedVideoCompleted() {

        }
    };

    public void loadImageFromGallery(){

        try {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= Build.VERSION_CODES.M) {
                result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                result3 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);


                if (result1 == 0 && result3 == 0) {
                    alertDialogForCameraImage();
                } else {
                    requestPermission();
                }
            } else {
                alertDialogForCameraImage();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{/*Manifest.permission.READ_PHONE_STATE,*/ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE/*,
                Manifest.permission.ACCESS_NETWORK_STATE*/}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    //boolean phoneAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    //boolean networkAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (/*phoneAccepted && */writeAccepted/* && networkAccepted*/ && readAccepted) {
//                        Toast.makeText(context, "You already have the permission", Toast.LENGTH_SHORT).show();
                        alertDialogForCameraImage();
                    } else {
//                        Toast.makeText(context, "Oops you just denied the permission", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{/*Manifest.permission.READ_PHONE_STATE,*/ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE/*, Manifest.permission.ACCESS_NETWORK_STATE*/},
                                    PERMISSION_REQUEST_CODE);
                        }
                    }
                }
                break;
        }
    }


    void pickImageFromGallery() {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // startActivityForResult(pickPhoto , 0);
        startActivityForResult(pickPhoto, RESULT_LOAD_IMAGE);
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case RESULT_LOAD_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    // takenPictureData = handleResultFromChooser(data);

                    selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null,
                            null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    // ImageView imageView = (ImageView) findViewById(R.id.imgView);
                    sourceImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                    // Function of split the image(divide the image into pieces)
                    splitImage(sourceImage, chunkSideLength);
                }
                break;
        }

        // And show the result in the image view when take picture from camera.

    }*/

    public void alertDialogForCameraImage() {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle("Pick Image From Gallery: ");
        adb.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                pickImageFromGallery();

            }
        });
        adb.show();
    }

    /**
     * Splits the source image and show them all into a grid in a new activity
     *
     * @param image
     *            The source image to split
     * @param chunkSideLength
     *            Image parts side length
     */
    private void splitImage(ImageView image, int chunkSideLength) {
        Random random = new Random(System.currentTimeMillis());

        // height and weight of higher|wider chunks if they would be
        int higherChunkSide, widerChunkSide;

        // Getting the scaled bitmap of the source image
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();

        rows = bitmap.getHeight() / chunkSideLength;
        higherChunkSide = bitmap.getHeight() % chunkSideLength + chunkSideLength;

        cols = bitmap.getWidth() / chunkSideLength;
        widerChunkSide = bitmap.getWidth() % chunkSideLength + chunkSideLength;

        // To store all the small image chunks in bitmap format in this list
        chunkedImage = new ArrayList<Bitmap>(rows * cols);

        if (higherChunkSide != chunkSideLength) {
            if (widerChunkSide != chunkSideLength) {
                // picture has both higher and wider chunks plus one big square chunk

                ArrayList<Bitmap> widerChunks = new ArrayList<Bitmap>(rows - 1);
                ArrayList<Bitmap> higherChunks = new ArrayList<Bitmap>(cols - 1);
                Bitmap squareChunk;

                int yCoord = 0;
                for (int y = 0; y < rows - 1; ++y) {
                    int xCoord = 0;
                    for (int x = 0; x < cols - 1; ++x) {
                        chunkedImage.add(Bitmap.createBitmap(bitmap, xCoord, yCoord, chunkSideLength, chunkSideLength));
                        xCoord += chunkSideLength;
                    }
                    // add last chunk in a row to array of wider chunks
                    widerChunks.add(Bitmap.createBitmap(bitmap, xCoord, yCoord, widerChunkSide, chunkSideLength));

                    yCoord += chunkSideLength;
                }

                // add last row to array of higher chunks
                int xCoord = 0;
                for (int x = 0; x < cols - 1; ++x) {
                    higherChunks.add(Bitmap.createBitmap(bitmap, xCoord, yCoord, chunkSideLength, higherChunkSide));
                    xCoord += chunkSideLength;
                }

                //save bottom-right big square chunk
                squareChunk = Bitmap.createBitmap(bitmap, xCoord, yCoord, widerChunkSide, higherChunkSide);

                //shuffle arrays
                Collections.shuffle(chunkedImage);
                Collections.shuffle(higherChunks);
                Collections.shuffle(widerChunks);

                //determine random position of big square chunk
                int bigChunkX = random.nextInt(cols);
                int bigChunkY = random.nextInt(rows);

                //add wider and higher chunks into resulting array of chunks
                //all wider(higher) chunks should be in one column(row) to avoid collisions between chunks
                //We must insert it row by row because they will displace each other from their columns otherwise
                for (int y = 0; y < rows - 1; ++y) {
                    chunkedImage.add(cols * y + bigChunkX, widerChunks.get(y));
                }

                //And then we insert the whole row of higher chunks
                for (int x = 0; x < cols - 1; ++x) {
                    chunkedImage.add(bigChunkY * cols + x, higherChunks.get(x));
                }

                chunkedImage.add(bigChunkY * cols + bigChunkX, squareChunk);
            } else {
                // picture has only number of higher chunks

                ArrayList<Bitmap> higherChunks = new ArrayList<Bitmap>(cols);

                int yCoord = 0;
                for (int y = 0; y < rows - 1; ++y) {
                    int xCoord = 0;
                    for (int x = 0; x < cols; ++x) {
                        chunkedImage.add(Bitmap.createBitmap(bitmap, xCoord, yCoord, chunkSideLength, chunkSideLength));
                        xCoord += chunkSideLength;
                    }
                    yCoord += chunkSideLength;
                }

                // add last row to array of higher chunks
                int xCoord = 0;
                for (int x = 0; x < cols; ++x) {
                    higherChunks.add(Bitmap.createBitmap(bitmap, xCoord, yCoord, chunkSideLength, higherChunkSide));
                    xCoord += chunkSideLength;
                }

                //shuffle arrays
                Collections.shuffle(chunkedImage);
                Collections.shuffle(higherChunks);

                //add higher chunks into resulting array of chunks
                //Each higher chunk should be in his own column to preserve original image size
                //We must insert it row by row because they will displace each other from their columns otherwise
                List<Point> higherChunksPositions = new ArrayList<Point>(cols);
                for (int x = 0; x < cols; ++x) {
                    higherChunksPositions.add(new Point(x, random.nextInt(rows)));
                }

                //sort positions of higher chunks. THe upper-left elements should be first
                Collections.sort(higherChunksPositions, new Comparator<Point>() {
                    @Override
                    public int compare(Point lhs, Point rhs) {
                        if (lhs.y != rhs.y) {
                            return lhs.y < rhs.y ? -1 : 1;
                        } else if (lhs.x != rhs.x) {
                            return lhs.x < rhs.x ? -1 : 1;
                        }
                        return 0;
                    }
                });

                for (int x = 0; x < cols; ++x) {
                    Point currentCoord = higherChunksPositions.get(x);
                    chunkedImage.add(currentCoord.y * cols + currentCoord.x, higherChunks.get(x));
                }

            }
        } else {
            if (widerChunkSide != chunkSideLength) {
                // picture has only number of wider chunks

                ArrayList<Bitmap> widerChunks = new ArrayList<Bitmap>(rows);

                int yCoord = 0;
                for (int y = 0; y < rows; ++y) {
                    int xCoord = 0;
                    for (int x = 0; x < cols - 1; ++x) {
                        chunkedImage.add(Bitmap.createBitmap(bitmap, xCoord, yCoord, chunkSideLength, chunkSideLength));
                        xCoord += chunkSideLength;
                    }
                    // add last chunk in a row to array of wider chunks
                    widerChunks.add(Bitmap.createBitmap(bitmap, xCoord, yCoord, widerChunkSide, chunkSideLength));

                    yCoord += chunkSideLength;
                }

                //shuffle arrays
                Collections.shuffle(chunkedImage);
                Collections.shuffle(widerChunks);

                //add wider chunks into resulting array of chunks
                //Each wider chunk should be in his own row to preserve original image size
                for (int y = 0; y < rows; ++y) {
                    chunkedImage.add(cols * y + random.nextInt(cols), widerChunks.get(y));
                }

            } else {
                // picture perfectly splits into square chunks
                int yCoord = 0;
                for (int y = 0; y < rows; ++y) {
                    int xCoord = 0;
                    for (int x = 0; x < cols; ++x) {
                        chunkedImage.add(Bitmap.createBitmap(bitmap, xCoord, yCoord, chunkSideLength, chunkSideLength));
                        xCoord += chunkSideLength;
                    }
                    yCoord += chunkSideLength;
                }

                Collections.shuffle(chunkedImage);
            }
        }

        // Function of merge the chunks images(after image divided in pieces then i can call this function to combine
        // and merge the image as one)
        mergeImage(chunkedImage, bitmap.getWidth(), bitmap.getHeight());
    }

    void mergeImage(ArrayList<Bitmap> imageChunks, int width, int height) {

        // create a bitmap of a size which can hold the complete image after merging
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);

        // create a canvas for drawing all those small images
        Canvas canvas = new Canvas(bitmap);
        int count = 0;
        Bitmap currentChunk = imageChunks.get(0);

        //Array of previous row chunks bottom y coordinates
        int[] yCoordinates = new int[cols];
        Arrays.fill(yCoordinates, 0);

        for (int y = 0; y < rows; ++y) {
            int xCoord = 0;
            for (int x = 0; x < cols; ++x) {
                currentChunk = imageChunks.get(count);
                canvas.drawBitmap(currentChunk, xCoord, yCoordinates[x], null);
                xCoord += currentChunk.getWidth();
                yCoordinates[x] += currentChunk.getHeight();
                count++;
            }
        }

    /*
     * The result image is shown in a new Activity
     */
        sourceImage.setImageBitmap(bitmap);
      /*  Intent intent = new Intent(SplitImage.this, Share.class);
        intent.putExtra("merged_image", bitmap);
        startActivity(intent);
        finish();*/
    }
}

