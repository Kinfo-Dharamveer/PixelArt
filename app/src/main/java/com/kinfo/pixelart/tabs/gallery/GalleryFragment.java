package com.kinfo.pixelart.tabs.gallery;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.kinfo.pixelart.AppConstants;
import com.kinfo.pixelart.R;
import com.kinfo.pixelart.SplitImage;
import com.kinfo.pixelart.tabs.home.ColorByNo;
import com.kinfo.pixelart.utils.AppSession;
import com.kinfo.pixelart.utils.TouchImageView;
import com.kinfo.pixelart.utils.Utilities;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by kinfo on 4/13/2018.
 */

public class GalleryFragment extends Fragment {

    ViewGroup rootView;
    LinearLayout watch_video,subscribe_to_unlock;
    ImageView img;
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

    private String cropPicturePath = "", imagePath = "";
    private String picturePath = "";
    private Intent intent;
    private Uri cameraUri = null;
    File photoFile;
    private AppSession appSession;
    private Utilities utilities;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        context = getActivity();
        appSession = new AppSession(getActivity());
        utilities = Utilities.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.gallery, container, false);

        img = (ImageView) rootView.findViewById(R.id.img);
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
               /* SubscribeFragment subscribeFragment = new SubscribeFragment();
                replaceFragmentWithBack(R.id.gallery_frame, subscribeFragment, "SubscribeFragment", null);*/
            }
        });



        return rootView;
    }

    public void replaceFragmentWithBack(int containerViewId,
                                        Fragment fragment,
                                        String fragmentTag,
                                        String backStackStateName) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .addToBackStack(backStackStateName)
                .commit();
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
            loadImageFromGallery();
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
                    //alertDialogForCameraImage();
                    dailogImageChooser(getActivity(),"Choose Image");
                } else {
                    requestPermission();
                }
            } else {
                //alertDialogForCameraImage();
                dailogImageChooser(getActivity(),"Choose Image");
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
                        //alertDialogForCameraImage();
                        dailogImageChooser(getActivity(),"Choose Image");
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


    //function for choose the image from gallery or camera
    public void dailogImageChooser(Context context, String header) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image_chooser);
        Window window = dialog.getWindow();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        window.setType(WindowManager.LayoutParams.FIRST_SUB_WINDOW);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(
                Color.TRANSPARENT));
        TextView tvHeader = (TextView) dialog.findViewById(R.id.tv_header);
        TextView tvGallery = (TextView) dialog.findViewById(R.id.tv_gallery);
        TextView tvCamera = (TextView) dialog.findViewById(R.id.tv_camera);
        appSession = new AppSession(context);
        tvHeader.setText(header);
        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        // Create the File where the photo should go
                        photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            return;
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {

                            cameraUri = FileProvider.getUriForFile(
                                    getActivity(),
                                    getActivity().getApplicationContext()
                                            .getPackageName() + ".provider", photoFile);


                            appSession.setImageUri(cameraUri);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
                            startActivityForResult(intent, AppConstants.CAMERA);
                        }
                    }

                    dialog.dismiss();
                }else {

                    intent = new Intent();
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String fileName = "IMAGE_" + System.currentTimeMillis() + ".jpg";
                    cameraUri = Uri.fromFile(utilities.getNewFile(AppConstants.IMAGE_DIRECTORY, fileName));
                    appSession.setImageUri(cameraUri);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, AppConstants.CAMERA);
                    dialog.dismiss();
                }
            }
        });
        tvGallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, ""),
                        AppConstants.GALLERY);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        // mCurrentPhotoPath = "file:" + image.getAbsolutePath();

        //   appSession.setImagePath("file:" + image.getAbsolutePath());

        return image;
    }


    private Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    }

    private File getTempFile() {
        String imageName = "CROP_" + System.currentTimeMillis() + ".jpg";
        File tempFile = utilities.getNewFile(AppConstants.IMAGE_DIRECTORY_CROP, imageName);
        cropPicturePath = tempFile.getPath();
        appSession = new AppSession(context);
        appSession.setCropImagePath(tempFile.getPath());
        return tempFile;
    }

    /**
     * Default Helper method to carry out crop operation
     */
    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            // cropIntent.putExtra("aspectX", 4);
            // cropIntent.putExtra("aspectY", 3);
            // cropIntent.putExtra("outputX", 640);
            // cropIntent.putExtra("outputY", 415);
            // cropIntent.putExtra("scale", true);
            // cropIntent.putExtra("return-data", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
            cropIntent.putExtra("outputFormat",
                    Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(cropIntent, AppConstants.CROP);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context,
                    getString(R.string.crop_action_support), Toast.LENGTH_SHORT)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context,
                    getString(R.string.crop_action_support), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    Bitmap bitmap;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(getClass().getName(), "onActivityResult requestCode : " + requestCode + " requestCode : " + resultCode);
        if (requestCode == AppConstants.ACTIVITY_RESULT && resultCode == getActivity().RESULT_OK) {
            //  finish();
        }
        if (requestCode == AppConstants.CROP && resultCode == RESULT_OK) {
            try {
                if (cropPicturePath == null || cropPicturePath.equals("")
                        || !new File(cropPicturePath).isFile())
                    cropPicturePath = appSession.getCropImagePath();

                if (cropPicturePath == null || cropPicturePath.equals("")
                        || !new File(cropPicturePath).isFile())
                    cropPicturePath = picturePath;

                if (cropPicturePath == null || cropPicturePath.equals("")
                        || !new File(cropPicturePath).isFile())
                    cropPicturePath = appSession.getImagePath();

                Log.i(getClass().getName(),
                        "CROP cropPicturePath : "
                                + cropPicturePath);
                if (cropPicturePath != null && !cropPicturePath.equals("")
                        && new File(cropPicturePath).isFile()) {
                    if (bitmap != null)
                        bitmap.recycle();
                    bitmap = Utilities.decodeFile(new File(cropPicturePath),
                            640, 640);
                    cropPicturePath = Utilities.getFilePath(bitmap, context, cropPicturePath);

                    imagePath = cropPicturePath;
                    //horizontalScrollView.setVisibility(parentView.VISIBLE);

                    Intent intent = new Intent(getActivity(), ColorByNo.class);
                    intent.putExtra("image_path",imagePath);
                    startActivity(intent);

                    //Picasso.with(context).load(new File(imagePath)).resize(140, 140).centerCrop().into(img);

                } else {
                    Toast.makeText(context,
                            getString(R.string.crop_action_error),
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context,
                        getString(R.string.crop_action_error),
                        Toast.LENGTH_LONG).show();
            }
        } else if (resultCode != RESULT_CANCELED) {
            if (requestCode == AppConstants.GALLERY) {
                try {
                    Uri uriImage = data.getData();
                    if (uriImage != null) {
                        picturePath = utilities.getAbsolutePath(uriImage);
                        if (picturePath == null || picturePath.equals(""))
                            picturePath = uriImage.getPath();
                        appSession.setImagePath(picturePath);
                        Log.i(getClass().getName(), "GALLERY picturePath : " + picturePath);
                        Cursor cursor = context
                                .getContentResolver()
                                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        new String[]{MediaStore.Images.Media._ID},
                                        MediaStore.Images.Media.DATA + "=? ",
                                        new String[]{picturePath}, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                            uriImage = Uri.parse("content://media/external/images/media/" + id);
                        }
                        performCrop(uriImage);
                    } else {
                        Toast.makeText(context,
                                getString(R.string.gallery_pick_error),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context,
                            getString(R.string.gallery_pick_error),
                            Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == AppConstants.CAMERA) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    Uri uri = Uri.parse(photoFile.getAbsolutePath());
                    picturePath = photoFile.getAbsolutePath();
                    appSession.setImagePath(picturePath);
                    imagePath = picturePath;
                    //Picasso.with(context).load(new File(imagePath)).into(img);
                    Intent intent = new Intent(getActivity(), ColorByNo.class);
                    intent.putExtra("image_path",imagePath);
                    startActivity(intent);
                } else {

                    try {
                        if (cameraUri   == null)
                            cameraUri = appSession.getImageUri();
                        if (cameraUri != null) {
                            picturePath = utilities.getAbsolutePath(cameraUri);
                            if (picturePath == null || picturePath.equals(""))
                                picturePath = cameraUri.getPath();
                            appSession.setImagePath(picturePath);
                            Log.i(getClass().getName(), "CAMERA picturePath : " + picturePath);
                            Cursor cursor = context
                                    .getContentResolver()
                                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                            new String[]{MediaStore.Images.Media._ID},
                                            MediaStore.Images.Media.DATA + "=? ",
                                            new String[]{picturePath}, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                int id = cursor
                                        .getInt(cursor
                                                .getColumnIndex(MediaStore.MediaColumns._ID));
                                cameraUri = Uri
                                        .parse("content://media/external/images/media/"
                                                + id);
                            }
                            performCrop(cameraUri);
                        } else {
                            Toast.makeText(context,
                                    getString(R.string.camera_capture_error),
                                    Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context,
                                getString(R.string.camera_capture_error),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}

