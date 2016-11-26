package com.boco.miboy.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boco.miboy.enums.Screen;
import com.boco.miboy.fragment.HistoryFragment;
import com.boco.miboy.fragment.PhotoFragment;
import com.boco.miboy.other.CircleTransform;
import com.boco.miboy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    private Screen currentScreen;

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
        showFragment(Screen.PHOTO);
    }

    public void showFragment(Screen screen) {
        Log.i(TAG, "showFragment: " + screen);
        if (currentScreen != screen) {
            currentScreen = screen;
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new PhotoFragment();
            switch (screen) {
                case PHOTO:
                    fragment = new PhotoFragment();
                    toolbar.setTitle(R.string.app_name);
                    break;
                case HISTORY:
                    fragment = new HistoryFragment();
                    toolbar.setTitle("History");
                    break;
            }
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss();
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
}