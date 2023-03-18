package com.example.travellor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;

public abstract class MainActivity extends AppCompatActivity {
    protected IOperations operationsBL = new Operations();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        operationsBL = new Operations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                goToHome();
                return true;
            case R.id.logOut:
                goToLogIn();
                return true;
            case R.id.myBooking:
                goToMyBooking();
                return true;
            case R.id.changePassword:
                goToChangePassword();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void goToChangePassword() {
        Intent intent = new Intent(this, passwordActivity.class);
        //handleBL(intent);
        startActivity(intent);
    }

    protected void goToMyBooking() {
        Intent intent = new Intent(this, bookingActivity.class);
        //handleBL(intent);
        startActivity(intent);
    }

    protected void goToLogIn() {
        Intent intent = new Intent(this, loginActivity.class);
        File file = new File(this.getFilesDir(), "user.txt");
        file.delete();

        startActivity(intent);
        //handleBL(intent);
    }

    protected void goToHome() {
        Intent intent = new Intent(this, homeActivity.class);
        startActivity(intent);
        //handleBL(intent);
    }

//    protected void handleBL(Intent intent) {
//        Bundle appConfiguration = new Bundle();
//        appConfiguration.putSerializable("Operation", operationsBL);
//        intent.putExtras(appConfiguration);
//        startActivity(intent);
//    }

//    protected void getBL() {
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        operationsBL = (IOperations) bundle.getSerializable("Operation");
//    }
}