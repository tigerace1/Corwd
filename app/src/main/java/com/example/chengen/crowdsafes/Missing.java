package com.example.chengen.crowdsafes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
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
import android.widget.ToggleButton;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

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
import java.util.ArrayList;
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class Missing extends Fragment implements View.OnClickListener {
    private EditText reportDescription, Contact, RespondTo, missingType, locationDes, videoURL,reward;
    private ImageView Image1, Image2, Image3;
    private ToggleButton find, lost;
    private static String target, reportType,name1,name2,name3;
    private static Bitmap pictureOne, pictureTwo, pictureThree;
    private Button send;
    private boolean isSend;
    private final String USER_AGENT = "Mozilla/5.0";
    private final static String Url = "https://www.crowdsafes.com/reportMissing";
    private static final String SERVER_ADDRESS="http://crowdsafe.azurewebsites.net/";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.activity_missing, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        reportDescription = (EditText) v.findViewById(R.id.etMiss);
        Contact = (EditText) v.findViewById(R.id.etNumber);
        RespondTo = (EditText) v.findViewById(R.id.etRespond);
        missingType = (EditText) v.findViewById(R.id.etObject);
        Image1 = (ImageView) v.findViewById(R.id.ivMissPic1);
        Image2 = (ImageView) v.findViewById(R.id.ivMissPic2);
        Image3 = (ImageView) v.findViewById(R.id.ivMissPic3);
        find = (ToggleButton) v.findViewById(R.id.tbFound);
        lost = (ToggleButton) v.findViewById(R.id.tbLost);
        reward = (EditText)v.findViewById(R.id.etReward);
        locationDes = (EditText) v.findViewById(R.id.etMissLocDes);
        videoURL = (EditText) v.findViewById(R.id.etMissURL);
        send = (Button) v.findViewById(R.id.btnMiss);
        ImageButton missMap = (ImageButton) v.findViewById(R.id.ibMissMap);
        missMap.bringToFront();
        send.setOnClickListener(this);
        missMap.setOnClickListener(this);
        find.setOnClickListener(this);
        lost.setOnClickListener(this);
        Image1.setOnClickListener(this);
        Image2.setOnClickListener(this);
        Image3.setOnClickListener(this);
        setHints();
        getData();
        return v;
    }
    private void getData(){
        SharedPreferences sharedPref =getActivity().getSharedPreferences("missing", Context.MODE_PRIVATE);
        if (sharedPref.contains("reortdes")) {
            reportDescription.setText(sharedPref.getString("reportdes", ""));
            Contact.setText(sharedPref.getString("contact", ""));
            RespondTo.setText(sharedPref.getString("respond to", ""));
            missingType.setText(sharedPref.getString("missingtype", ""));
            locationDes.setText(sharedPref.getString("locationdes", ""));
            videoURL.setText(sharedPref.getString("videoURL", ""));
            reward.setText(sharedPref.getString("reward", ""));
            target = sharedPref.getString("target", "");
            reportType = sharedPref.getString("reporttype", "");
        }
        if(reportType.equals("Found"))
            found();
        else if(reportType.equals("Lost"))
            lost();
        if(sharedPref.contains("imageone")) {
            String photoByte1 = sharedPref.getString("imageone", "");
            Image1.setImageDrawable(new BitmapDrawable(getResources(),stringToBitMap(photoByte1)));
        }
        if(sharedPref.contains("imagetwo")) {
            String photoByte2 = sharedPref.getString("imagetwo", "");
            Image2.setImageDrawable(new BitmapDrawable(getResources(),stringToBitMap(photoByte2)));
        }
        if (sharedPref.contains("imageothree")) {
            String photoByte3 = sharedPref.getString("imagethree", "");
            Image3.setImageDrawable(new BitmapDrawable(getResources(),stringToBitMap(photoByte3)));
        }
    }

    private void setHints (){
        target="";
        reportType="";
        missingType.setHint("The object name");
        reportDescription.setHint("Describe the object");
        Contact.setHint("Your phone number");
        RespondTo.setHint("Your name");
        locationDes.setHint("Describe your location");
        videoURL.setHint("Attach your video's url here");
        reward.setHint("Rewards");
        reward.setHeight(0);
        missingType.setHintTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.c));
        reportDescription.setHintTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.c));
        Contact.setHintTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.c));
        RespondTo.setHintTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.c));
        locationDes.setHintTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.c));
        videoURL.setHintTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.c));
        reward.setHintTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.c));
        reward.setVisibility(View.GONE);
        missingType.setBackground(new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.twolines)));
        reportDescription.setScrollbarFadingEnabled(true);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tbFound:
                found();
                reportType = "Found";
                break;
            case R.id.tbLost:
                lost();
                reportType = "Lost";
                break;
            case R.id.btnMiss:
                OnSendButtonPress();
                break;
            case R.id.ivMissPic1:
                PopupMenu popup1 = new PopupMenu(getActivity(), Image1);
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
                        } else if (item.getTitle().equals("Delete")) {
                            if (Image1.getDrawable() == null) {
                                Toast.makeText(getActivity(), "No picture to delete", Toast.LENGTH_SHORT).show();
                            } else if (Image1.getDrawable().getMinimumWidth() == 0) {
                                Toast.makeText(getActivity(), "No picture to delete", Toast.LENGTH_SHORT).show();
                            } else {
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
            case R.id.ivMissPic2:
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
                        } else if (item.getTitle().equals("Delete")) {
                            if (Image2.getDrawable() == null) {
                                Toast.makeText(getActivity(), "No picture to delete", Toast.LENGTH_SHORT).show();
                            } else if (Image2.getDrawable().getMinimumWidth() == 0) {
                                Toast.makeText(getActivity(), "No picture to delete", Toast.LENGTH_SHORT).show();
                            } else {
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
            case R.id.ivMissPic3:
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
                        } else if (item.getTitle().equals("Delete")) {
                            if (Image3.getDrawable() == null) {
                                Toast.makeText(getActivity(), "No picture to delete", Toast.LENGTH_SHORT).show();
                            } else if (Image3.getDrawable().getMinimumWidth() == 0) {
                                Toast.makeText(getActivity(), "No picture to delete", Toast.LENGTH_SHORT).show();
                            }else {
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
            case R.id.ibMissMap:
                Intent map = new Intent(getContext(), MapsActivity.class);
                map.putExtra("type",0);
                startActivity(map);
                break;
        }
    }
    private void OnSendButtonPress(){
        send.setClickable(false);
        Thread t1 = null,t2 = null,t3 = null;
        if (Image1.getDrawable() != null && Image1.getDrawable().getMinimumWidth() != 0) {
            name1 = Calendar.getInstance().get(Calendar.MILLISECOND)+
                    ""+Calendar.getInstance().get(Calendar.DATE)+""+Calendar.getInstance().get(Calendar.DAY_OF_WEEK_IN_MONTH);
            t1 = new Thread(){
                @Override
                public void run() {
                    super.run();
                    uploadImage(Bitmap.createScaledBitmap(pictureOne,200,200,false), "abcd");
                }
            };
            t1.start();
        }
        if (Image2.getDrawable() != null && Image2.getDrawable().getMinimumWidth() != 0) {
            name2 = Calendar.getInstance().get(Calendar.MILLISECOND)+
                    ""+Calendar.getInstance().get(Calendar.DATE)+""+Calendar.getInstance().get(Calendar.DAY_OF_WEEK_IN_MONTH);
            t2 = new Thread(){
                @Override
                public void run() {
                    super.run();
                    uploadImage(Bitmap.createScaledBitmap(pictureTwo,200,200,false), "dcba");
                }
            };
            t2.start();
        }
        if (Image2.getDrawable() != null && Image2.getDrawable().getMinimumWidth() != 0) {
            name3 = Calendar.getInstance().get(Calendar.MILLISECOND)+
                    ""+Calendar.getInstance().get(Calendar.DATE)+""+Calendar.getInstance().get(Calendar.DAY_OF_WEEK_IN_MONTH);
            t3 = new Thread(){
                @Override
                public void run() {
                    super.run();
                    uploadImage(Bitmap.createScaledBitmap(pictureThree,200,200,false), "oooo");
                }
            };
            t3.start();
        }
        SharedPreferences sharedPref =getActivity().getSharedPreferences("Location", Context.MODE_PRIVATE);
        if (sharedPref.contains("mssingLoc"))
            target = sharedPref.getString("missingLoc","");
        SharedPreferences preferences = getActivity().getSharedPreferences("Location", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    upLoadToDB(reportType, missingType.getText().toString(), reportDescription.getText().toString(),
                            RespondTo.getText().toString(), Contact.getText().toString(), name1, name2, name3,
                            videoURL.getText().toString(), target, locationDes.getText().toString(),reward.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        try {
            if(t1!=null)
               t1.join();
            if(t2!=null)
               t2.join();
            if(t3!=null)
               t3.join();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!isSend) {
            send.setClickable(true);
            Toast.makeText(getActivity(), "Upload failed", Toast.LENGTH_LONG).show();
        }else {
            reportType = "";
            reportDescription.setText("");
            Contact.setText("");
            RespondTo.setText("");
            missingType.setText("");
            locationDes.setText("");
            videoURL.setText("");
            Image1.setBackground(null);
            Image2.setBackground(null);
            Image3.setBackground(null);
            if(pictureOne!=null)
               pictureOne.recycle();
            if(pictureTwo!=null)
               pictureTwo.recycle();
            if(pictureThree!=null)
               pictureThree.recycle();
            pictureOne=null;
            pictureTwo=null;
            pictureThree=null;
            find.setChecked(false);
            lost.setChecked(false);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 0:
                    pictureOne = (Bitmap) data.getExtras().get("data");
                    Image1.setImageDrawable(new BitmapDrawable(getResources(),
                            Bitmap.createScaledBitmap(pictureOne,Image1.getWidth(),Image1.getHeight(),false)));
                    break;
                case 1:
                    pictureTwo = (Bitmap) data.getExtras().get("data");
                    Image2.setImageDrawable(new BitmapDrawable(getResources(),
                            Bitmap.createScaledBitmap(pictureTwo,Image2.getWidth(),Image2.getHeight(),false)));
                    break;
                case 2:
                    pictureThree = (Bitmap) data.getExtras().get("data");
                    Image3.setImageDrawable(new BitmapDrawable(getResources(),
                            Bitmap.createScaledBitmap(pictureThree,Image3.getWidth(),Image3.getHeight(),false)));
                    break;
                case 7:
                    Uri selectedImage1 = data.getData();
                    getActivity().getContentResolver().notifyChange(selectedImage1, null);
                    try {
                        pictureOne = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage1);
                        Image1.setImageDrawable(new BitmapDrawable(getResources(),
                                Bitmap.createScaledBitmap(pictureOne,Image1.getWidth(),Image1.getHeight(),false)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    Uri selectedImage2 = data.getData();
                    getActivity().getContentResolver().notifyChange(selectedImage2, null);
                    try {
                        pictureTwo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage2);
                        Image2.setImageDrawable(new BitmapDrawable(getResources(),
                                Bitmap.createScaledBitmap(pictureTwo,Image2.getWidth(),Image2.getHeight(),false)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 9:
                    Uri seletedImage3 = data.getData();
                    getActivity().getContentResolver().notifyChange(seletedImage3, null);
                    try {
                        pictureThree = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), seletedImage3);
                        Image3.setImageDrawable(new BitmapDrawable(getResources(),
                                Bitmap.createScaledBitmap(pictureThree,Image3.getWidth(),Image3.getHeight(),false)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
    private void found(){
        lost.setChecked(false);
        find.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.checkboxOn));
        lost.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.checkboxOff));
        reward.setVisibility(View.GONE);
        reward.setHeight(0);
        missingType.setBackground(new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.twolines)));
    }
    private void lost(){
        find.setChecked(false);
        lost.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.checkboxOn));
        find.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.checkboxOff));
        missingType.setBackground(new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.oneline)));
        reward.setHeight(45);
        reward.setVisibility(View.VISIBLE);
    }
    private void upLoadToDB(String reportType, String missingType, String reportDescription,
                            String responseTo, String contact,String photo1,String photo2, String photo3, String video,
                            String location, String locDes,String rewards) {

        String string = "txtField1="+missingType + "&txtField2=" + reportDescription +"&txtField3="+contact+
                "&photovdo1=" + photo1 + "&photo2=" + photo2 +
                "&photo3=" + photo3+"&location="+location+"&reward="+rewards;
        isSend = true;
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
        } catch (Exception e) {
            e.printStackTrace();
            isSend = false;
        }
    }

    private void sendGet() {
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
        } catch (Exception e) {
            e.printStackTrace();
            isSend = false;
        }
    }

    private void sendPost(String urlParameters) {
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
        } catch (Exception e) {
            e.printStackTrace();
            isSend = false;
        }
    }
    private synchronized void uploadImage(Bitmap image, String name) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("image", encodedImage));
        dataToSend.add(new BasicNameValuePair("name", name));
        HttpParams httpParams = getHttpRequestParams();
        HttpClient client = new DefaultHttpClient(httpParams);
        HttpPost post = new HttpPost(SERVER_ADDRESS + "SavePicture.php");
        try {
            post.setEntity(new UrlEncodedFormEntity(dataToSend));
            client.execute(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private HttpParams getHttpRequestParams(){
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
        HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
        return httpRequestParams;
    }
    @Override
    public void onPause() {
        super.onPause();
        Thread t = new Thread(){
            @Override
            public void run() {
                super.run();
                SharedPreferences sharedPref = getActivity().getSharedPreferences("missing", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("reportdes", reportDescription.getText().toString());
                editor.putString("contact", Contact.getText().toString());
                editor.putString("respond to", RespondTo.getText().toString());
                editor.putString("missingtype", missingType.getText().toString());
                editor.putString("locationdes", locationDes.getText().toString());
                editor.putString("videoURL", videoURL.getText().toString());
                editor.putString("reward",reward.getText().toString());
                editor.putString("target", target + "");
                editor.putString("reporttype", reportType + "");
                if(pictureOne!=null)
                    editor.putString("imageone",bitMapToString(pictureOne));
                if(pictureTwo!=null)
                    editor.putString("imageone",bitMapToString(pictureTwo));
                if(pictureThree!=null)
                    editor.putString("imageone",bitMapToString(pictureThree));
                editor.apply();
            }
        };
        t.start();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences preferences = getActivity().getSharedPreferences("missing", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
    private String bitMapToString(Bitmap bitmap){
        System.gc();
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte [] b=baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    public Bitmap stringToBitMap(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.URL_SAFE);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}

