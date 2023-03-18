package com.example.travellor;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class loginActivity extends MainActivity {
    MyReceiver receiver;
    IntentFilter intentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new MyReceiver();
        intentFilter = new IntentFilter("com.travellor.AUTHENTICATION");
        registerReceiver(receiver,intentFilter);

        File file = new File(this.getFilesDir(), "user.txt");
        if (file.exists())
            goToHomePage();

        setContentView(R.layout.activity_login);

        Button button = findViewById(R.id.loginButton);
        button.setOnClickListener(view -> {
            String username = String.valueOf(((EditText) findViewById(R.id.loginUsername)).getText());
            String password = String.valueOf(((EditText) findViewById(R.id.loginPassword)).getText());
//            username = "AnsarButt";
//            password = "1234";
            if (operationsBL.authenticateUser(username, password)) {
                Intent intent = new Intent();
                intent.setAction("com.travellor.AUTHENTICATION");
                intent.putExtra("username",username);
                sendBroadcast(intent);

                try {
                    File cacheFile = new File(this.getFilesDir(), "user.txt");
                    FileWriter myWriter = new FileWriter(cacheFile);
                    myWriter.write(username + "\n" + password);
                    myWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                goToHomePage();
            } else {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });

        TextView text = findViewById(R.id.signupRedirect);
        text.setOnClickListener(view -> goToSignUp());
    }

    private void goToSignUp() {
        Intent intent = new Intent(this, signUpActivity.class);
        startActivity(intent);
        setResult(RESULT_OK);
    }

    protected void goToHomePage() {
        Intent intent = new Intent(this, homeActivity.class);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
