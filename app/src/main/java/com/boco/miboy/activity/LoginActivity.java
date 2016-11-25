package com.boco.miboy.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.boco.miboy.other.Storage;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.boco.miboy.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    @BindView(R.id.login_facebook_btn)
    LoginButton facebookLoginBtn;
    private CallbackManager callbackManager;
    private ProgressDialog progressDialog;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        facebookInit();
        googleInit();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {
            Log.i(TAG, "onCreate: user uid " + user.getUid());
            Log.i(TAG, "onCreate: user name " + user.getDisplayName());
            Log.i(TAG, "onCreate: user email " + user.getEmail());
            Log.i(TAG, "onCreate: user photo " + user.getPhotoUrl());
            authSuccessful();
        } else {
            Log.e(TAG, "onCreate: not authorized");
            LoginManager.getInstance().logOut(); //Facebook
            if (mGoogleApiClient.isConnected()) {//Google
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            }
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sign in...");
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user == null) {
            signOut();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////// Facebook /////////////////////////////////////////////
    private void facebookInit() {
        Log.i(TAG, "facebookInit: ");
        callbackManager = CallbackManager.Factory.create();
        facebookLoginBtn.setReadPermissions("email");
        facebookLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: AccessToken: " + loginResult.getAccessToken().getToken());
                facebookLoginProcessing(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "onError: onCancel:");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "onError: AccessToken: " + error.getMessage());
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT);
                if (!isNetworkConnected(getApplicationContext())) {
                    Toast.makeText(getBaseContext(), "No Internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void facebookLoginProcessing(AccessToken token) {
        Log.i(TAG, "facebookLoginProcessing: ");
        progressDialog.show();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "facebookLoginProcessing: signInWithCredential", task.getException());
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(LoginActivity.this, "User has account from different social by this email", Toast.LENGTH_SHORT).show();
                                authSuccessful();
                            } else {
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "signInWithCredential: onComplete:" + task.isSuccessful());
                            authSuccessful();
                        }
                    }
                });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////// Google ///////////////////////////////////////////////
    private void googleInit() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
    }

    @OnClick(R.id.login_google_btn)
    public void signIn(View view) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private void googleLoginProcessing(GoogleSignInAccount acct) {
        Log.d(TAG, "googleLoginProcessing:" + acct.getId());
        progressDialog.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        } else {
                            authSuccessful();
                        }
                    }
                });
    }

    public void signOut() {
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////// Other /////////////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.i(TAG, "onActivityResult: " + result.getStatus());
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                googleLoginProcessing(account);
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void authSuccessful() {
        Intent intent;
        if (Storage.getInstance(this).isQueryRequired()) {
            intent = new Intent(this, QueryActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }

    public boolean isNetworkConnected(Context context) {
        boolean result = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null) {
            result = true;
        }
        return result;
    }
}