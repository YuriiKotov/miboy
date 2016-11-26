package com.boco.miboy.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boco.miboy.enums.RecipeEvent;
import com.boco.miboy.enums.Screen;
import com.boco.miboy.fragment.HistoryFragment;
import com.boco.miboy.fragment.PhotoFragment;
import com.boco.miboy.model.History;
import com.boco.miboy.other.CircleTransform;
import com.boco.miboy.R;
import com.boco.miboy.other.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.Sort;

public class MainActivity extends PermissionActivity implements NavigationView.OnNavigationItemSelectedListener {
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
    public String imagePath;
    private Fragment currentFragment;
    public ProgressDialog progressDialog;
    public Realm realm;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RecipeEvent recipeEvent) {
        progressDialog.dismiss();
        Log.i(TAG, "onMessageEvent: " + recipeEvent);
        if (recipeEvent == RecipeEvent.SUCCESS) {
            List<History> historyList = realm.where(History.class).findAllSorted("timestamp", Sort.DESCENDING);
            if (!historyList.isEmpty()) {
                Intent intent = new Intent(this, ListActivity.class);
                intent.putExtra(Const.HISTORY_ID_EXTRA, historyList.get(0).getTimestamp());
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        realm.close();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();

        navigationView.setNavigationItemSelectedListener(this);
        initDrawer();
        showFragment(Screen.PHOTO);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait please...");
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && currentFragment != null) {
            currentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void showFragment(Screen screen) {
        Log.i(TAG, "showFragment: " + screen);
        if (currentScreen != screen) {
            currentScreen = screen;
            FragmentManager fragmentManager = getSupportFragmentManager();
            currentFragment = new PhotoFragment();
            switch (screen) {
                case PHOTO:
                    currentFragment = new PhotoFragment();
                    toolbar.setTitle("Photo");
                    break;
                case HISTORY:
                    currentFragment = new HistoryFragment();
                    toolbar.setTitle("History");
                    break;
            }
            fragmentManager.beginTransaction().replace(R.id.container, currentFragment).commitAllowingStateLoss();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_photo:
                showFragment(Screen.PHOTO);
                break;
            case R.id.nav_history:
                showFragment(Screen.HISTORY);
                break;
            case R.id.nav_query:
                startActivity(new Intent(this, QueryActivity.class));
                finish();
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