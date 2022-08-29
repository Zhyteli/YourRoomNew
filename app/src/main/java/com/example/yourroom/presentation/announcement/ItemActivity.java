package com.example.yourroom.presentation.announcement;

import static com.example.yourroom.data.Constant.ANNOUNCEMENT_KEY_ADDRESS;
import static com.example.yourroom.data.Constant.ANNOUNCEMENT_KEY_DESCRIPTION;
import static com.example.yourroom.data.Constant.ANNOUNCEMENT_KEY_EMAIL;
import static com.example.yourroom.data.Constant.ANNOUNCEMENT_KEY_IMAGE_URI;
import static com.example.yourroom.data.Constant.ANNOUNCEMENT_KEY_PHONE;
import static com.example.yourroom.data.Constant.ANNOUNCEMENT_KEY_PRICE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yourroom.R;
import com.squareup.picasso.Picasso;

public class ItemActivity extends AppCompatActivity {
    private TextView priceIm, addressIm, descriptionIm, phoneIm, emailIm, nameIm, personNameIM;
    private ImageView imageIm;
    private ActionBar actionBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        init();
        getIntentMain();
        if (getSupportActionBar() != null){
            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private void init(){
        priceIm = findViewById(R.id.priceItem);
        addressIm = findViewById(R.id.addressItem);
        descriptionIm = findViewById(R.id.descriptionItem);
        phoneIm = findViewById(R.id.phoneItem);
        emailIm = findViewById(R.id.emailItem);
        imageIm = findViewById(R.id.imageItem);


    }
    private void getIntentMain(){
        Intent i = getIntent();
        if(i != null){
            Picasso.get().load(i.getStringExtra(ANNOUNCEMENT_KEY_IMAGE_URI)).into(imageIm);
            priceIm.setText(i.getStringExtra(ANNOUNCEMENT_KEY_PRICE));
            addressIm.setText(i.getStringExtra(ANNOUNCEMENT_KEY_ADDRESS));
            descriptionIm.setText(i.getStringExtra(ANNOUNCEMENT_KEY_DESCRIPTION));
            phoneIm.setText(i.getStringExtra(ANNOUNCEMENT_KEY_PHONE));
            emailIm.setText(i.getStringExtra(ANNOUNCEMENT_KEY_EMAIL));

        }
    }
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        // Hides the status and navigation bar until the user clicks
        // on the screeen.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
