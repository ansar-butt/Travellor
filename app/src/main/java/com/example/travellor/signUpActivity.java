package com.example.travellor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class signUpActivity extends MainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        Button button = findViewById(R.id.signupButton);
        button.setOnClickListener(view -> {
            String username = String.valueOf(((EditText) findViewById(R.id.signupUsername)).getText());
            String password = String.valueOf(((EditText) findViewById(R.id.signupPassword)).getText());
            String card = String.valueOf(((EditText) findViewById(R.id.cardNumber)).getText());

            if (operationsBL.signUpUser(username, password, card))
                goToLogin();
            else
                makeToast();
        });

        TextView text = findViewById(R.id.loginRedirect);
        text.setOnClickListener(view -> goToLogin());

    }

    private void makeToast() {
        Toast.makeText(this, "Invalid User Name", Toast.LENGTH_SHORT).show();
    }

    private void goToLogin() {
        Intent intent = new Intent(this, loginActivity.class);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
