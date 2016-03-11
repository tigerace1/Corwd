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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.ToggleButton;

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

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class Missing extends Fragment implements View.OnClickListener{
    private EditText reportDescription,Contact,RespondTo,missingType,locationDes,videoURL;
    private ImageView Image1,Image2,Image3;
    private ToggleButton find,lost;
    private String target,reportType;
    private Bitmap pictureOne,pictureTwo,pictureThree;
    private byte[] photoByte1,photoByte2,photoByte3;
    private Button send;
    private boolean isSend;
    private final String USER_AGENT = "Mozilla/5.0";
    private final static String Url = "https://www.crowdsafes.com/reportMissing";
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.activity_missing, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        reportDescription = (EditText) v.findViewById(R.id.etMiss);
        Contact = (EditText)v.findViewById(R.id.etNumber);
        RespondTo = (EditText)v.findViewById(R.id.etRespond);
        missingType = (EditText)v.findViewById(R.id.etObject);
        Image1 = (ImageView) v.findViewById(R.id.ivMissPic1);
        Image2 = (ImageView) v.findViewById(R.id.ivMissPic2);
        Image3 = (ImageView) v.findViewById(R.id.ivMissPic3);
        ImageButton delOne =(ImageButton) v.findViewById(R.id.ibMissDelOne);
        ImageButton delTwo = (ImageButton) v.findViewById(R.id.ibMissDelTwo);
        ImageButton delThree = (ImageButton) v.findViewById(R.id.ibMissDelThree);
        find = (ToggleButton)v.findViewById(R.id.tbFound);
        lost = (ToggleButton)v.findViewById(R.id.tbLost);
        locationDes = (EditText)v.findViewById(R.id.etMissLocDes);
        videoURL = (EditText)v.findViewById(R.id.etMissURL);
        send = (Button) v.findViewById(R.id.btnMiss);
        ImageButton missMap = (ImageButton) v.findViewById(R.id.ibMissMap);
        send.setOnClickListener(this);
        missMap.setOnClickListener(this);
        delOne.setOnClickListener(this);
        delTwo.setOnClickListener(this);
        delThree.setOnClickListener(this);
        find.setOnClickListener(this);
        lost.setOnClickListener(this);
        Image1.setOnClickListener(this);
        Image2.setOnClickListener(this);
        Image3.setOnClickListener(this);
        this.reportType="";
        reportDescription.setText("");
        Contact.setText("");
        RespondTo.setText("");
        missingType.setText("");
        locationDes.setText("");
        videoURL.setText("");
        reportDescription.setScrollbarFadingEnabled(true);
        reportDescription.setHint("Contains time, description of the object");
        videoURL.setHint("video url here");
        locationDes.setHint("Location description");
        find.setChecked(false);
        lost.setChecked(false);
        return v;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tbFound:
                find.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_two));
                lost.setChecked(false);
                find.setTextColor(getResources().getColor(R.color.checkboxOn));
                lost.setTextColor(getResources().getColor(R.color.checkboxOff));
                this.reportType="Found";
                break;
            case R.id.tbLost:
                lost.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_two));
                find.setChecked(false);
                lost.setTextColor(getResources().getColor(R.color.checkboxOn));
                find.setTextColor(getResources().getColor(R.color.checkboxOff));
                this.reportType ="Lost";
                break;
            case R.id.btnMiss:
                send.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_two));
                send.setClickable(false);
                if (Image1.getDrawable() != null) {
                    Bitmap image1 = ((BitmapDrawable) Image1.getDrawable()).getBitmap();
                    photoByte1 = bitMapToString(image1);
                }
                if (Image2.getDrawable() != null) {
                    Bitmap image2 = ((BitmapDrawable) Image2.getDrawable()).getBitmap();
                    photoByte2= bitMapToString(image2);
                }
                if (Image3.getDrawable() != null) {
                    Bitmap image3 = ((BitmapDrawable) Image3.getDrawable()).getBitmap();
                    photoByte3 = bitMapToString(image3);
                }
                target = MapsActivity.getTarget();
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            upLoadToDB(reportType,missingType.getText().toString(),reportDescription.getText().toString(),
                                    RespondTo.getText().toString(),Contact.getText().toString(),photoByte1,photoByte2,photoByte3,
                                    videoURL.getText().toString(),target,locationDes.getText().toString());
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
                if(isSend==false) {
                    send.setClickable(true);
                    Toast.makeText(getActivity(), "Upload failed", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.ivMissPic1:
                PopupMenu popup1 = new PopupMenu(getActivity(), Image1);
                popup1.getMenuInflater().inflate(R.menu.popup_menu, popup1.getMenu());
                popup1.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("choose a photo")) {
                            Intent intentOne = new Intent();
                            intentOne.setType("image/*");
                            intentOne.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intentOne, "Select Picture"), 7);
                        } else if (item.getTitle().equals("take a picture")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 0);
                        }
                        return true;
                    }
                });
                popup1.show();
                break;
            case R.id.ivMissPic2:
                PopupMenu popup2 = new PopupMenu(getActivity(), Image2);
                popup2.getMenuInflater().inflate(R.menu.popup_menu, popup2.getMenu());
                popup2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("choose a photo")) {
                            Intent intentTwo = new Intent();
                            intentTwo.setType("image/*");
                            intentTwo.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intentTwo, "Select Picture"), 8);
                        } else if (item.getTitle().equals("take a picture")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 1);
                        }
                        return true;
                    }
                });
                popup2.show();
                break;
            case R.id.ivMissPic3:
                PopupMenu popup3 = new PopupMenu(getActivity(), Image3);
                popup3.getMenuInflater().inflate(R.menu.popup_menu, popup3.getMenu());
                popup3.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("choose a photo")) {
                            Intent intentThree = new Intent();
                            intentThree.setType("image/*");
                            intentThree.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intentThree, "Select Picture"),9);
                        } else if (item.getTitle().equals("take a picture")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent,2);
                        }
                        return true;
                    }
                });
                popup3.show();
                break;
            case R.id.ibMissDelOne:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setCancelable(false);
                builder1.setMessage("Do you want to Delete this photo?");
                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Image1.setImageBitmap(null);
                        pictureOne=null;
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
                break;
            case R.id.ibMissDelTwo:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                builder2.setCancelable(false);
                builder2.setMessage("Do you want to Delete this photo?");
                builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Image2.setImageBitmap(null);
                        pictureTwo=null;
                    }
                });
                builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert2 = builder2.create();
                alert2.show();
                break;
            case R.id.ibMissDelThree:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                builder.setMessage("Do you want to Delete this photo?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Image3.setImageBitmap(null);
                        pictureThree=null;
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case R.id.ibMissMap:
                Intent map = new Intent(getContext(),MapsActivity.class);
                startActivity(map);
                break;
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
        byte[] b = baos.toByteArray();
        //return Base64.encodeToString(b, Base64.DEFAULT);
        return b;
    }
    private void upLoadToDB(String reportType,String missingType, String reportDescription,
                            String responseTo,String contact, byte[] photo1,byte[] photo2,byte[] photo3, String video,
                            String location,String locDes){
        String string = "type=Aggression"+"&reportDescription="+reportDescription+
                "&photo1="+photo1+"&photo2="+photo2+"&photo3="+photo3;
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

