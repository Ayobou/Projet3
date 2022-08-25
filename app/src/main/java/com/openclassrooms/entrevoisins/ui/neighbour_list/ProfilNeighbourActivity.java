package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import java.io.Serializable;

public class ProfilNeighbourActivity extends AppCompatActivity implements Serializable {

    private ImageView mAvatar;
    private TextView mName1, mName2, mCity, mPhone, mAbout, mFbUrl;
    private FloatingActionButton mFloatingActionButton;

    private NeighbourApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_neighbour);

        mAvatar = findViewById(R.id.profil_neighbour_image);
        mName1 = findViewById(R.id.profil_neighbour_name1);
        mName2 = findViewById(R.id.profil_neighbour_name2);
        mCity = findViewById(R.id.profil_neighbour_city);
        mPhone = findViewById(R.id.profil_neighbour_phone);
        mAbout = findViewById(R.id.profil_neighbour_about);
        mFbUrl = findViewById(R.id.profil_neighbour_fbUrl);
        mFloatingActionButton = findViewById(R.id.floatingActionButton);

        mApiService = DI.getNeighbourApiService();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getIncomingIntent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void getIncomingIntent() {

        if (getIntent().hasExtra("neighbour")) {
            Neighbour intentNeighbour = (Neighbour) getIntent().getSerializableExtra("neighbour");
            displayData(intentNeighbour);
        }
    }

    private void displayData(Neighbour neighbour) {
        Glide.with(this).asBitmap().load(neighbour.getAvatarUrl()).fitCenter().into(mAvatar);
        mName1.setText(neighbour.getName());
        mName2.setText(neighbour.getName());
        mCity.setText(neighbour.getAddress());
        mPhone.setText(neighbour.getPhoneNumber());
        mFbUrl.setText(neighbour.getFbUrl() + neighbour.getName());
        mAbout.setText(neighbour.getAboutMe());

        if (neighbour.isFavorite()) {
            mFloatingActionButton.setImageResource(android.R.drawable.btn_star_big_on);
        }
        else {
            mFloatingActionButton.setImageResource(R.drawable.ic_baseline_star_24);
        }

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!neighbour.isFavorite()) {
                    mFloatingActionButton.setImageResource(android.R.drawable.btn_star_big_on);
                } else {
                    mFloatingActionButton.setImageResource(R.drawable.ic_baseline_star_24);
                }

                neighbour.setFavorite(!neighbour.isFavorite());
                mApiService.createFavoriteNeighbour(neighbour);
            }
        });
    }
}