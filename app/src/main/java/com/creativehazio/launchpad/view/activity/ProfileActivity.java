package com.creativehazio.launchpad.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creativehazio.launchpad.R;
import com.creativehazio.launchpad.model.User;
import com.creativehazio.launchpad.mutual_functions.CustomToast;
import com.creativehazio.launchpad.viewmodel.ProfileActivityViewModel;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class ProfileActivity extends AppCompatActivity {

    private static final int GOOGLE_REQUEST_CODE = 111;

    private ProfileActivityViewModel viewModel;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isNightMode;

    private Toolbar toolbar;
    private LinearLayout animationView;
    private Button signOutBtn;
    private Button darkModeBtn;
    private Button getPremiumBtn;
    private Button syncToCloudBtn;
    private Button downloadFromCloudBtn;
    private ShapeableImageView creativeProfilePicture;
    private TextView creativeName;
    private TextView creativeEmail;

    private FirebaseUser user;
    private BeginSignInRequest signUpRequest;
    private SignInClient oneTapClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViewsAndConfigs();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.profile));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.signOut();
                observeUserData();
                CustomToast.showShortToast(ProfileActivity.this,
                        getString(R.string.sign_out_success));
            }
        });

        darkModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableDarkMode();
            }
        });

        getPremiumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,GetPremiumActivity.class));
            }
        });

        syncToCloudBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user == null) {
                    signInToGoogle();
                } else {
                    viewModel.syncNotesToUserCloud();
                }
            }
        });

        downloadFromCloudBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user == null) {
                    signInToGoogle();
                } else {
                    viewModel.downloadNotesFromUserCloud();
                }
            }
        });

        observeUserData();

    }

    private void signInToGoogle() {
        animationView.setVisibility(View.VISIBLE);
        oneTapClient.beginSignIn(signUpRequest)
                .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        try {
                            animationView.setVisibility(View.GONE);
                            startIntentSenderForResult(
                                    result.getPendingIntent().getIntentSender(), GOOGLE_REQUEST_CODE,
                                    null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {}
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        animationView.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GOOGLE_REQUEST_CODE:

                try {
                    animationView.setVisibility(View.VISIBLE);
                    SignInCredential googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = googleCredential.getGoogleIdToken();
                    if (idToken !=  null) {
                        AuthCredential firebaseCredential = GoogleAuthProvider
                                .getCredential(idToken, null);
                        FirebaseAuth firebaseAuth = viewModel.getFirebaseAuth();
                        firebaseAuth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            animationView.setVisibility(View.GONE);
                                            observeUserData();
                                            FirebaseUser user = viewModel.getCurrentUser();
                                            addUserDataToFireStore(user);
                                        } else {
                                            animationView.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    animationView.setVisibility(View.GONE);
                }
                break;

            default:
                break;
        }
    }

    private void addUserDataToFireStore(FirebaseUser user) {
        viewModel.addUserToFireStore(user);
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = viewModel.getCurrentUser();
        if (user != null) {
            updateUI(user);
        }
    }

    private void initViewsAndConfigs() {
        viewModel = new ViewModelProvider(this).
                get(ProfileActivityViewModel.class);
        toolbar = findViewById(R.id.profile_toolbar);
        animationView = findViewById(R.id.animation_view);
        signOutBtn = findViewById(R.id.sign_out_btn);
        darkModeBtn = findViewById(R.id.dark_mode_btn);
        getPremiumBtn = findViewById(R.id.get_premium_btn);
        syncToCloudBtn = findViewById(R.id.sync_to_cloud_btn);
        downloadFromCloudBtn = findViewById(R.id.download_notes_from_cloud_btn);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        isNightMode = sharedPreferences.getBoolean("night",false);
        creativeProfilePicture = findViewById(R.id.profile_picture);
        creativeName = findViewById(R.id.creative_username);
        creativeEmail = findViewById(R.id.creative_email);
        oneTapClient = Identity.getSignInClient(getApplicationContext());
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();
    }

    private void updateUI(FirebaseUser user) {
        Glide.with(this).load(user.getPhotoUrl()).
                placeholder(R.drawable.nav_drawer_header_img).into(creativeProfilePicture);
        creativeName.setText(user.getDisplayName());
        creativeEmail.setText(user.getEmail());
    }

    private void updateUI(User user) {
        Glide.with(this).load(user).
                placeholder(R.drawable.nav_drawer_header_img).into(creativeProfilePicture);
        creativeName.setText(user.getName());
        creativeEmail.setText(user.getEmail());
    }

    private void observeUserData() {
        viewModel.getUserData().observe(ProfileActivity.this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                updateUI(user);
            }
        });
    }

    private void enableDarkMode() {
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor = sharedPreferences.edit();
            editor.putBoolean("night", false);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor = sharedPreferences.edit();
            editor.putBoolean("night", true);
        }
        editor.apply();
    }
}