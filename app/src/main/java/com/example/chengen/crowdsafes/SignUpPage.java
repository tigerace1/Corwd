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

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class SignUpPage extends AppCompatActivity implements View.OnClickListener{
    private EditText firstName,lastName,email,password,repassword;
    private Button signup;
    private final String USER_AGENT = "Mozilla/5.0";
    private final static String Url = "https://www.crowdsafes.com/signup";
    private boolean isSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);
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
            boolean isEmail = true;
            boolean hasEmail = true;
            boolean hasFirstName = true;
            boolean hasLastName = true;
            boolean hasPassword = true;
            boolean matchPasswords = true;
            if (firstName.getText().toString().equals("")) {
                firstName.setText("");
                firstName.setHint("Enter your first name");
                firstName.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.checkboxOn));
                signup.setClickable(true);
                hasFirstName = false;
            }
            if (lastName.getText().toString().equals("")) {
                lastName.setText("");
                lastName.setHint("Enter your last name");
                lastName.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.checkboxOn));
                signup.setClickable(true);
                hasLastName = false;
            }
            if (email.getText().toString().equals("")) {
                email.setText("");
                email.setHint("Enter your email");
                email.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.checkboxOn));
                signup.setClickable(true);
                hasEmail = false;
            }else if(!isValidEmailAddress(email.getText().toString())){
                email.setText("");
                email.setHint("Please enter a valid email");
                email.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.checkboxOn));
                signup.setClickable(true);
                isEmail = false;
            }
            if(pass.equals("")||repass.equals("")){
                password.setText("");
                password.setHint("Enter your password");
                password.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.checkboxOn));
                signup.setClickable(true);
                hasPassword = false;
            }else
            if (!pass.equals(repass)) {
                repassword.setText("");
                repassword.setHint("Two passwords must be matched");
                repassword.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.checkboxOn));
                signup.setClickable(true);
                matchPasswords = false;
            }
            if(hasEmail && hasLastName && hasFirstName && hasPassword && isEmail && matchPasswords) {
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
    public boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
