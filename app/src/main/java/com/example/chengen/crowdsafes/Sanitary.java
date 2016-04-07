package com.example.chengen.crowdsafes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class Sanitary extends Fragment implements View.OnClickListener{
    private EditText reportDescription,locationDes,videoURL;
    private ImageView Image1,Image2,Image3;
    private String target;
    private byte[] photoByte1,photoByte2,photoByte3;
    private Button send;
    private boolean isSend;
    private Bitmap pictureOne;
    private Bitmap pictureTwo;
    private Bitmap pictureThree;
    private final String USER_AGENT = "Mozilla/5.0";
    private final static String Url = "https://www.crowdsafes.com/androidTest";
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.activity_sanitary, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        reportDescription = (EditText) v.findViewById(R.id.etSan);
        Image1 = (ImageView) v.findViewById(R.id.ivSanPic1);
        Image2 = (ImageView) v.findViewById(R.id.ivSanPic2);
        Image3 = (ImageView) v.findViewById(R.id.ivSanPic3);
        locationDes = (EditText) v.findViewById(R.id.etSanLocDes);
        videoURL = (EditText) v.findViewById(R.id.etSanURL);
        send = (Button) v.findViewById(R.id.btnSan);
        send.setClickable(true);
        ImageButton missMap = (ImageButton) v.findViewById(R.id.ibSanMap);
        missMap.bringToFront();
        send.setOnClickListener(this);
        missMap.setOnClickListener(this);
        Image1.setOnClickListener(this);
        Image2.setOnClickListener(this);
        Image3.setOnClickListener(this);
        reportDescription.setScrollbarFadingEnabled(true);
        reportDescription.setHint("Describe the situation");
        locationDes.setHint("Describe your location");
        videoURL.setHint("Attach your video's url here");
        reportDescription.setHintTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.c));
        locationDes.setHintTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.c));
        videoURL.setHintTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.c));
        return v;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSan:
                OnSendButtonPress();
                break;
            case R.id.ivSanPic1:
                PopupMenu popup1 = new PopupMenu(getActivity(),Image1);
                popup1.getMenuInflater().inflate(R.menu.popup_menu, popup1.getMenu());
                popup1.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intentOne = new Intent();
                        if (item.getTitle().equals("Choose existed")) {
                            intentOne.setType("image/*");
                            intentOne.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intentOne, "Select Picture"), 7);
                        } else if (item.getTitle().equals("Take a photo")) {
                            intentOne = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intentOne, 0);
                        } else if(item.getTitle().equals("Delete")){
                            if(Image1.getDrawable()==null){
                                Toast.makeText(getActivity(),"No picture to delete",Toast.LENGTH_SHORT).show();
                            }else
                            if(Image1.getDrawable().getMinimumWidth()==0){
                                Toast.makeText(getActivity(),"No picture to delete",Toast.LENGTH_SHORT).show();
                            }else {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                                builder1.setCancelable(false);
                                builder1.setMessage("Do you want to Delete this photo?");
                                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Image1.setImageBitmap(null);
                                        pictureOne = null;
                                    }
                                });
                                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert1 = builder1.create();
                                alert1.show();
                            }
                        }
                        return true;
                    }
                });
                popup1.show();
                break;
            case R.id.ivSanPic2:
                PopupMenu popup2 = new PopupMenu(getActivity(), Image2);
                popup2.getMenuInflater().inflate(R.menu.popup_menu, popup2.getMenu());
                popup2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intentTwo = new Intent();
                        if (item.getTitle().equals("Choose existed")) {
                            intentTwo.setType("image/*");
                            intentTwo.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intentTwo, "Select Picture"), 8);
                        } else if (item.getTitle().equals("Take a photo")) {
                            intentTwo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intentTwo, 1);
                        } else if(item.getTitle().equals("Delete")) {
                            if(Image2.getDrawable()==null){
                                Toast.makeText(getActivity(),"No picture to delete",Toast.LENGTH_SHORT).show();
                            }else
                            if (Image2.getDrawable().getMinimumWidth()==0) {
                                Toast.makeText(getActivity(),"No picture to delete",Toast.LENGTH_SHORT).show();
                            }else {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                                builder1.setCancelable(false);
                                builder1.setMessage("Do you want to Delete this photo?");
                                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Image2.setImageBitmap(null);
                                        pictureTwo = null;
                                    }
                                });
                                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert1 = builder1.create();
                                alert1.show();
                            }
                        }
                        return true;
                    }
                });
                popup2.show();
                break;
            case R.id.ivSanPic3:
                PopupMenu popup3 = new PopupMenu(getActivity(), Image3);
                popup3.getMenuInflater().inflate(R.menu.popup_menu, popup3.getMenu());
                popup3.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intentThree = new Intent();
                        if (item.getTitle().equals("Choose existed")) {
                            intentThree.setType("image/*");
                            intentThree.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intentThree, "Select Picture"), 9);
                        } else if (item.getTitle().equals("Take a photo")) {
                            intentThree = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intentThree, 2);
                        } else if(item.getTitle().equals("Delete")) {
                            if(Image3.getDrawable()==null){
                                Toast.makeText(getActivity(),"No picture to delete",Toast.LENGTH_SHORT).show();
                            }else
                            if (Image3.getDrawable().getMinimumWidth()==0) {
                                Toast.makeText(getActivity(),"No picture to delete",Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder builder3 = new AlertDialog.Builder(getActivity());
                                builder3.setCancelable(false);
                                builder3.setMessage("Do you want to Delete this photo?");
                                builder3.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Image3.setImageBitmap(null);
                                        pictureThree = null;
                                    }
                                });
                                builder3.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert3 = builder3.create();
                                alert3.show();
                            }
                        }
                        return true;
                    }
                });
                popup3.show();
                break;
            case R.id.ibSanMap:
                Intent map = new Intent(getActivity(),MapsActivity.class);
                startActivity(map);
        }
    }
    private void OnSendButtonPress(){
        send.setClickable(false);
        if (Image1.getDrawable() != null && Image1.getDrawable().getMinimumWidth() != 0) {
            Bitmap image1 = ((BitmapDrawable) Image1.getDrawable()).getBitmap();
            photoByte1 = bitMapToString(image1);
        }
        if (Image2.getDrawable() != null && Image2.getDrawable().getMinimumWidth() != 0) {
            Bitmap image2 = ((BitmapDrawable) Image2.getDrawable()).getBitmap();
            photoByte2 = bitMapToString(image2);
        }
        if (Image2.getDrawable() != null && Image2.getDrawable().getMinimumWidth() != 0) {
            Bitmap image3 = ((BitmapDrawable) Image3.getDrawable()).getBitmap();
            photoByte3 = bitMapToString(image3);
        }
        target = MapsActivity.getTarget();
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    upLoadToDB(reportDescription.getText().toString(),
                            photoByte1,photoByte2,photoByte3,videoURL.getText().toString(),
                            target, locationDes.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!isSend) {
            send.setClickable(true);
            Toast.makeText(getActivity(), "Upload failed", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        if (resultCode == Activity.RESULT_OK) {
            switch(requestCode){
                case 0:
                    pictureOne = (Bitmap) data.getExtras().get("data");
                    this.Image1.setImageBitmap(Bitmap.createScaledBitmap(pictureOne, 100, 100, false));
                    break;
                case 1:
                    pictureTwo = (Bitmap) data.getExtras().get("data");
                    this.Image2.setImageBitmap(Bitmap.createScaledBitmap(pictureTwo, 100, 100, false));
                    break;
                case 2:
                    pictureThree = (Bitmap) data.getExtras().get("data");
                    this.Image3.setImageBitmap(Bitmap.createScaledBitmap(pictureThree, 100, 100, false));
                    break;
                case 7:
                    Uri selectedImage1 = data.getData();
                    getActivity().getContentResolver().notifyChange(selectedImage1, null);
                    try{
                        pictureOne = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage1);
                        this.Image1.setImageBitmap(Bitmap.createScaledBitmap(pictureOne, 100, 100, false));
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    Uri selectedImage2 = data.getData();
                    getActivity().getContentResolver().notifyChange(selectedImage2, null);
                    try {
                        pictureTwo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage2);
                        this.Image2.setImageBitmap(Bitmap.createScaledBitmap(pictureTwo, 100, 100, false));
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    break;
                case 9:
                    Uri seletedImage3 = data.getData();
                    getActivity().getContentResolver().notifyChange(seletedImage3, null);
                    try{
                        pictureThree = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), seletedImage3);
                        this.Image3.setImageBitmap(Bitmap.createScaledBitmap(pictureThree, 100, 100, false));
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
    private byte[] bitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    private void upLoadToDB(String reportDescription,
                            byte[] photo,byte[] photo2, byte[] photo3, String video,String location,String locDes){
        String string = "type=Aggression"+"&reportDescription="+reportDescription+
                "&photo1="+ Arrays.toString(photo) +"&photo2="+ Arrays.toString(photo2) +"&photo3="+ Arrays.toString(photo3);
        isSend=true;
        try {
            InputStream is = new BufferedInputStream(getActivity().getAssets().open("dst_root_ca_x3.pem"));
            Certificate ca = CertificateFactory.getInstance("X.509").generateCertificate(is);
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null, null);
            ks.setCertificateEntry(Integer.toString(1), ca);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, tmf.getTrustManagers(), null);
            HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
            URL oracle = new URL(Url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);
            in.close();
            sendGet();
            sendPost(string);
        }catch (Exception e){
            e.printStackTrace();
            isSend=false;
        }
    }
    private void sendGet(){
        try {
            URL obj = new URL(Url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }catch (Exception e){
            e.printStackTrace();
            isSend=false;
        }
    }
    private void sendPost(String urlParameters){
        try {
            URL obj = new URL(Url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            if (response.toString() != null) {
                Intent succeed = new Intent(getActivity(), Thankyou.class);
                startActivity(succeed);
            }
        }catch (Exception e){
            e.printStackTrace();
            isSend=false;
        }
    }
}