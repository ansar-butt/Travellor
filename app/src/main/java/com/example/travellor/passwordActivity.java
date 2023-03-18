package com.example.travellor;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class passwordActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_passwrod);
        setView();
    }

    private void setView() {
        TextView userName = findViewById(R.id.changePasswordUsername);
        userName.setText(operationsBL.getUsername());

        Button button = findViewById(R.id.changePasswordButton);
        button.setOnClickListener(view -> changePassword());
    }

    private void changePassword() {
        String oldPassword = String.valueOf(((EditText) findViewById(R.id.oldPassword)).getText());
        String newPassword = String.valueOf(((EditText) findViewById(R.id.newPassword)).getText());
        String newPassword2 = String.valueOf(((EditText) findViewById(R.id.newPasswordConfirmed)).getText());
        String text = "Error Updating Password";
        String userName = "";

        String oldPass = "";
        File myObj = new File(this.getFilesDir(), "user.txt");
        Scanner myReader = null;
        try {
            myReader = new Scanner(myObj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (myReader != null && myReader.hasNextLine()) {
            userName = myReader.nextLine();
            if (myReader.hasNextLine()) {
                oldPass = myReader.nextLine();
            }
            myReader.close();
        }

        if (newPassword.equals(newPassword2))
            if (oldPass.equals(oldPassword)) {
                if (operationsBL.updatePassword(userName, newPassword))
                    text = "Password Updates Successfully";
            }


        makeToast(text);
    }

    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}