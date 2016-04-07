package com.example.chengen.crowdsafes;

import android.content.Intent;
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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

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

public class SignUpPage extends AppCompatActivity implements View.OnClickListener{
    private EditText firstName,lastName,email,password,repassword;
    private Button signup;
    private final String USER_AGENT = "Mozilla/5.0";
    private final static String Url = "https://www.crowdsafes.com/signup";
    private boolean isSend;
    private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        firstName = (EditText)findViewById(R.id.etFirstName);
        lastName = (EditText)findViewById(R.id.etLastName);
        email = (EditText)findViewById(R.id.etEmail);
        password = (EditText)findViewById(R.id.etSignUpPass);
        repassword = (EditText)findViewById(R.id.etSignUpRePass);
        signup = (Button)findViewById(R.id.btnSignUp);
        TextView signIn = (TextView) findViewById(R.id.tvSignIn);
        signIn.setOnClickListener(this);
        signup.setOnClickListener(this);
        firstName.setHint("First Name");
        lastName.setHint("Last Name");
        email.setHint("Email");
        password.setHint("Password");
        repassword.setHint("Retype your password");
        firstName.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.c));
        lastName.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.c));
        email.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.c));
        password.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.c));
        repassword.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.c));
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnSignUp){
            signup.setClickable(false);
            String pass = password.getText().toString();
            String repass = repassword.getText().toString();
            if (firstName.getText().toString().equals("")) {
                firstName.setHint("Enter your first name");
                firstName.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.checkboxOn));
                signup.setClickable(true);
            } else if (lastName.getText().toString().equals("")) {
                lastName.setHint("Enter your last name");
                lastName.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.checkboxOn));
                signup.setClickable(true);
            } else if (email.getText().toString().equals("")) {
                email.setHint("Enter your email");
                email.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.checkboxOn));
                signup.setClickable(true);
            } else if(pass.equals("")||repass.equals("")){
                Toast.makeText(getApplication(), "Both passwords cannot be empty!", Toast.LENGTH_LONG).show();
            }else if (!pass.equals(repass)) {
                Toast.makeText(getApplication(), "The passwords is not match!", Toast.LENGTH_LONG).show();
                password.selectAll();
                repassword.selectAll();
                signup.setClickable(true);
            } else {
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        upLoadToDB(firstName.getText().toString(), lastName.getText().toString(),
                                email.getText().toString(), password.getText().toString());
                    }
                };
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isSend) {
                    signup.setClickable(true);
                    Toast.makeText(getApplication(), "Sign up failed", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplication(), "Sign up success", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this,LoginPage.class));
                }
            }
        }else if(v.getId()==R.id.tvSignIn){
            startActivity(new Intent(this,LoginPage.class));
        }
    }
    private void upLoadToDB(String first, String last, String email,
                            String password){
        String string = "firstName="+first+"&lastName="+last+
                "&username="+email+"&password="+password;
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
            if (response.toString().equals("Success")) {
                isSend=true;
                Intent succeed = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(succeed);
            }
        }catch (Exception e){
            e.printStackTrace();
            isSend=false;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, LoginPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
