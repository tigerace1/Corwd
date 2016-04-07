package com.example.chengen.crowdsafes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
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

public class LoginPage extends AppCompatActivity implements View.OnClickListener{

    private static EditText user,password;
    private final String USER_AGENT = "Mozilla/5.0";
    private final static String Url = "https://www.crowdsafes.com/login";
    static private boolean isSend;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        user = (EditText)findViewById(R.id.etusername);
        password =(EditText)findViewById(R.id.etpassword);
        Button login = (Button) findViewById(R.id.btnLogin);
        TextView forget = (TextView) findViewById(R.id.tvforget);
        TextView signUp = (TextView) findViewById(R.id.tvsignup);
        login.setOnClickListener(this);
        forget.setOnClickListener(this);
        signUp.setOnClickListener(this);
        user.setHint("UserName/e-mail");
        password.setHint("Password");
        user.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.c));
        password.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.c));
        if(savedInstanceState!=null) {
           user.setText(savedInstanceState.getString("username"));
           password.setText(savedInstanceState.getString("password"));
        }
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                login();
                break;
            case R.id.tvforget:
                break;
            case R.id.tvsignup:
                startActivity(new Intent(LoginPage.this,SignUpPage.class));
                break;
        }
    }
    private void login(){
        final String use = user.getText().toString();
        final String pass = password.getText().toString();
        if(password.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
        }else if(user.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please enter your username",Toast.LENGTH_SHORT).show();
        }else {
            Thread t = new Thread() {
                @Override
                public void run() {
                    super.run();
                    upLoadToDB(use, pass);
                }
            };
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!isSend) {
                Toast.makeText(this, "Invaild username or password", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void upLoadToDB(String username, String password){
        String string = "username="+username+"&password="+password;
        isSend=true;
        try {
            InputStream is = new BufferedInputStream(getAssets().open("dst_root_ca_x3.pem"));
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
            sendPost(string);
        }catch (Exception e){
            e.printStackTrace();
            isSend=false;
        }
    }
    private void sendGet(String username){
        try {
            URL obj = new URL("https://crowdsafes.com/getLoginInfo" + "/" + username);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            String userInfo = response.toString();
            JSONObject jsonObject = new JSONObject(userInfo);
            username = jsonObject.getString("username");
            String firstName = jsonObject.getString("firstName");
            String lastName = jsonObject.getString("lastName");
            System.out.println(username + firstName + lastName);
            SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("username", username);
            editor.putString("firstName", firstName);
            editor.putString("lastName", lastName);
            editor.apply();
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
            if(isSend = response.toString().equals("Success")){
                sendGet(user.getText().toString());
                startActivity(new Intent(LoginPage.this, NavigationMenu.class));
                finish();
            }
            System.out.println(response.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, NavigationMenu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("count",0);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, NavigationMenu.class);
        intent.putExtra("count",0);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
}
