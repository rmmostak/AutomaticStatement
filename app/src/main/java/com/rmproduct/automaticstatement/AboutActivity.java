package com.rmproduct.automaticstatement;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AboutActivity extends AppCompatActivity {

    FloatingActionButton call, email;
    TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        call = findViewById(R.id.call);
        email = findViewById(R.id.email);
        version = findViewById(R.id.version);

        version.setText(BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE);

        call.setOnClickListener(v -> makeCall());
        email.setOnClickListener(v -> makeMail());
    }

    private void makeMail() {
        String sendTo = "rmmostak@gmail.com";
        String sendSub = "Type your email subject";
        String sendBody = "Write your message here..";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{sendTo});
        intent.putExtra(Intent.EXTRA_SUBJECT, sendSub);
        intent.putExtra(Intent.EXTRA_TEXT, sendBody);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(Intent.createChooser(intent, "Choose an email client..."));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(AboutActivity.this, "Sorry, email address is not found!", Toast.LENGTH_SHORT).show();
        }

    }

    private void makeCall() {
        String no="+8801780891662";
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + no));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (AboutActivity.this.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(context, "Unissued call permission!", Toast.LENGTH_LONG).show();
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 10);
                return;
            }
            ActivityOptions options = ActivityOptions.makeCustomAnimation(AboutActivity.this, R.anim.fade_in, R.anim.fade_out);
            startActivity(intent, options.toBundle());
            Log.d("check",no);
        } else {
            ActivityOptions options = ActivityOptions.makeCustomAnimation(AboutActivity.this, R.anim.fade_in, R.anim.fade_out);
            startActivity(intent, options.toBundle());
        }

    }
}