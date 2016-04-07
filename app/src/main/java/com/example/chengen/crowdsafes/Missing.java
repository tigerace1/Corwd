package com.example.chengen.crowdsafes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
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
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class Missing extends Fragment implements View.OnClickListener {
    private EditText reportDescription, Contact, RespondTo, missingType, locationDes, videoURL,reward;
    private static CharSequence[] data;
    private ImageView Image1, Image2, Image3;
    private ToggleButton find, lost;
    private static String target, reportType;
    private static Bitmap pictureOne, pictureTwo, pictureThree;
    private byte[] photoByte1, photoByte2, photoByte3;
    private Button send;
    private boolean isSend;
    private final String USER_AGENT = "Mozilla/5.0";
    private final static String Url = "https://www.crowdsafes.com/reportMissing";
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
        target="";
        reportType="";
        send.setOnClickListener(this);
        missMap.setOnClickListener(this);
        find.setOnClickListener(this);
        lost.setOnClickListener(this);
        Image1.setOnClickListener(this);
        Image2.setOnClickListener(this);
        Image3.setOnClickListener(this);
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
        missingType.setBackground(new BitmapDrawable(BitmapFactory.decodeResource(getResources(),R.drawable.twolines)));
        if(savedInstanceState!=null){
            data = savedInstanceState.getCharSequenceArray("data");
            reportDescription.setText(data[0]);
            missingType.setText(data[1]);
            Contact.setText(data[2]);
            RespondTo.setText(data[3]);
            videoURL.setText(data[4]);
            locationDes.setText(data[5]);
            target=data[6].toString();
            reportType=data[7].toString();
            if(reportType.equals("Found")) {
                find.setChecked(true);
                lost.setChecked(false);
            }else if(reportType.equals("Lost")){
                find.setChecked(false);
                lost.setChecked(true);
            }
            Parcelable[] parcelables=savedInstanceState.getParcelableArray("photos");
            assert parcelables != null;
            if(parcelables[0]!=null) {
                Image1.setImageBitmap(resizeBitmap((Bitmap) parcelables[0], 100, 100));
                System.out.println("PPP");
            }
            if(parcelables[1]!=null)
               Image2.setImageBitmap(pictureTwo);
            if(parcelables[2]!=null)
               Image3.setImageBitmap(resizeBitmap((Bitmap) parcelables[2], 100, 100));
            System.out.println("DDD00");
        }
        reportDescription.setScrollbarFadingEnabled(true);
        return v;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tbFound:
                lost.setChecked(false);
                find.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.checkboxOn));
                lost.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.checkboxOff));
                reward.setVisibility(View.GONE);
                reward.setHeight(0);
                missingType.setBackground(new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.twolines)));
                reportType = "Found";
                break;
            case R.id.tbLost:
                find.setChecked(false);
                lost.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.checkboxOn));
                find.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.checkboxOff));
                missingType.setBackground(new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.oneline)));
                reportType = "Lost";
                reward.setHeight(45);
                reward.setVisibility(View.VISIBLE);
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
                startActivity(map);
                break;
        }
    }
    private void OnSendButtonPress(){
        send.setClickable(false);
        if (Image1.getDrawable() != null && Image1.getDrawable().getMinimumWidth() != 0) {
            photoByte1 = bitMapToString(pictureOne);
        }
        if (Image2.getDrawable() != null && Image2.getDrawable().getMinimumWidth() != 0) {
            photoByte2 = bitMapToString(pictureTwo);
        }
        if (Image2.getDrawable() != null && Image2.getDrawable().getMinimumWidth() != 0) {
            photoByte3 = bitMapToString(pictureThree);
        }
        target = MapsActivity.getTarget();
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    upLoadToDB(reportType, missingType.getText().toString(), reportDescription.getText().toString(),
                            RespondTo.getText().toString(), Contact.getText().toString(), photoByte1, photoByte2, photoByte3,
                            videoURL.getText().toString(), target, locationDes.getText().toString(),reward.getText().toString());
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
                    Image1.setImageBitmap(resizeBitmap(pictureOne,100,100));
                    break;
                case 1:
                    pictureTwo = (Bitmap) data.getExtras().get("data");
                    Image2.setImageBitmap(resizeBitmap(pictureTwo, 100, 100));
                    break;
                case 2:
                    pictureThree = (Bitmap) data.getExtras().get("data");
                    Image3.setImageBitmap(resizeBitmap(pictureThree, 100, 100));
                    break;
                case 7:
                    Uri selectedImage1 = data.getData();
                    getActivity().getContentResolver().notifyChange(selectedImage1, null);
                    try {
                        pictureOne = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage1);
                        Image1.setImageBitmap(resizeBitmap(pictureOne, 100, 100));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    Uri selectedImage2 = data.getData();
                    getActivity().getContentResolver().notifyChange(selectedImage2, null);
                    try {
                        pictureTwo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage2);
                        Image2.setImageBitmap(resizeBitmap(pictureTwo, 100, 100));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 9:
                    Uri seletedImage3 = data.getData();
                    getActivity().getContentResolver().notifyChange(seletedImage3, null);
                    try {
                        pictureThree = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), seletedImage3);
                        Image3.setImageBitmap(resizeBitmap(pictureThree, 100, 100));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private byte[] bitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private void upLoadToDB(String reportType, String missingType, String reportDescription,
                            String responseTo, String contact, byte[] photo1, byte[] photo2, byte[] photo3, String video,
                            String location, String locDes,String rewards) {
        String string = "txtField1="+missingType + "&txtField2=" + reportDescription +"&txtField3="+contact+
                "&photo1=" + Arrays.toString(photo1) + "&photo2=" + Arrays.toString(photo2) +
                "&photo3=" + Arrays.toString(photo3)+"&location="+location+"&reward="+rewards;
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
    public Bitmap resizeBitmap(Bitmap bitmap,int newWidth,int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        return scaledBitmap;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        data=  new CharSequence[8];
        data[0]=reportDescription.getText();
        data[1]=missingType.getText();
        data[2]=Contact.getText();
        data[3]=RespondTo.getText();
        data[4]=videoURL.getText();
        data[5]=locationDes.getText();
        data[6]=target;
        data[7]=reportType;
        Parcelable[] parcelables = new Parcelable[3];
        parcelables[0]=pictureOne;
        parcelables[1]=pictureTwo;
        parcelables[2]=pictureThree;
        outState.putCharSequenceArray("data",data);
        outState.putParcelableArray("photos", parcelables);
        System.out.println("KKK");
    }
}

