package com.boco.miboy.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boco.miboy.backend.Message;
import com.boco.miboy.backend.RequestService;
import com.boco.miboy.other.CircleTransform;
import com.boco.miboy.R;
import com.boco.miboy.backend.Urls;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    private FirebaseUser user;
    @BindView(R.id.container)
    RelativeLayout container;

    @OnClick(R.id.post)
    public void onPost(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestService service = retrofit.create(RequestService.class);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.img_20161122_164420);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        Message message = new Message("lvncksnvsv", "img_20161122_164420.jpg", encodedImage);
        Call<ResponseBody> call = service.post(message);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: onPost: ");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showSnackBar(getString(R.string.text_server_connection_error));
                Log.e(TAG, "onFailure: onPost: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        user = FirebaseAuth.getInstance().getCurrentUser();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        initDrawer();
    }

    private void initDrawer() {
        TextView userName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.name);
        TextView userEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email);
        ImageView avatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);

        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            if (name != null && !name.isEmpty()) {
                userName.setText(name);
            } else {
                userName.setVisibility(View.GONE);
            }
            if (email != null && !email.isEmpty()) {
                userEmail.setText(email);
            } else {
                userEmail.setVisibility(View.GONE);
            }
            if (photoUrl != null) {
                Picasso.with(getApplicationContext())
                        .load(photoUrl)
                        .transform(new CircleTransform())
                        .into(avatar);
            } else {
                Log.e(TAG, "initDrawer: " + photoUrl);
                avatar.setVisibility(View.GONE);
            }
        } else {
            userName.setVisibility(View.GONE);
            userEmail.setVisibility(View.GONE);
            avatar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_history:
                break;
            case R.id.nav_sign_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            default:
                break;
        }
        drawer.closeDrawers();
        return true;
    }

    private void showSnackBar(String text) {
        Snackbar.make(container, text, Snackbar.LENGTH_SHORT).show();
    }
}