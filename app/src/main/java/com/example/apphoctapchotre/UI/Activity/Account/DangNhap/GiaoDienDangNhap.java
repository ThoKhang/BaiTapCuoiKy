package com.example.apphoctapchotre.UI.Activity.Account.DangNhap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.apphoctapchotre.DATA.model.NguoiDung;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Activity.Account.DangKy.DangKy;
import com.example.apphoctapchotre.UI.Activity.Account.QuenMatKau.QuenMatKhauOTP;
import com.example.apphoctapchotre.UI.Activity.GioiThieu.OnboardingActivity;
import com.example.apphoctapchotre.UI.ViewModel.GiaoDienDangNhapViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;

// Facebook SDK
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

public class GiaoDienDangNhap extends AppCompatActivity {

    private EditText eTextEmail, eTextMatKhau;
    private Button btnDangNhap;
    private TextView textQuenMatKhau, textDangKyNgay;

    private MaterialCardView btnGoogle;    // from layout
    private MaterialCardView btnFacebook;  // from layout

    private GiaoDienDangNhapViewModel viewModel;
    private String currentEmail = "";

    private GoogleSignInClient googleSignInClient;

    // Facebook
    private CallbackManager callbackManager;

    private final ActivityResultLauncher<Intent> googleLoginLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        if (account != null) {
                            String idToken = account.getIdToken();
                            viewModel.loginWithGoogle(idToken);
                        } else {
                            Toast.makeText(this, "Không lấy được tài khoản Google", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ApiException e) {
                        Toast.makeText(this, "Google sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Hủy đăng nhập Google", Toast.LENGTH_SHORT).show();
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_dien_dang_nhap);
        // Bind views
        eTextEmail = findViewById(R.id.eTextEmail);
        eTextMatKhau = findViewById(R.id.eTextMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        textQuenMatKhau = findViewById(R.id.textQuenMatKhau);
        textDangKyNgay = findViewById(R.id.textDangKyNgay);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnFacebook = findViewById(R.id.btnFacebook);
        viewModel = new ViewModelProvider(this).get(GiaoDienDangNhapViewModel.class);
        // ===================== GOOGLE =====================
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        // ===================== FACEBOOK =====================
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accessToken = loginResult.getAccessToken().getToken();
                viewModel.loginWithFacebook(accessToken);
            }
            @Override
            public void onCancel() {
                Toast.makeText(GiaoDienDangNhap.this, "Hủy đăng nhập Facebook", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException error) {
                Toast.makeText(GiaoDienDangNhap.this,
                        "Facebook login failed: " + (error != null ? error.getMessage() : ""),
                        Toast.LENGTH_SHORT).show();
            }
        });
        // ===================== OBSERVE =====================
        viewModel.toastMessage.observe(this, msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(GiaoDienDangNhap.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        // Email/mật khẩu -> sang OTP
        viewModel.loginSuccess.observe(this, success -> {
            if (success != null && success) {
                openOtpScreen(currentEmail);
            }
        });
        viewModel.googleUser.observe(this, user -> {
            if (user != null) {
                onSocialLoginSuccess(user);
            }
        });
        viewModel.facebookUser.observe(this, user -> {
            if (user != null) {
                onSocialLoginSuccess(user);
            }
        });
        // ===================== CLICK =====================
        textQuenMatKhau.setOnClickListener(v -> {
            String email = eTextEmail.getText().toString().trim();
            Intent intent = new Intent(GiaoDienDangNhap.this, QuenMatKhauOTP.class);
            if (!email.isEmpty()) {
                intent.putExtra("EMAIL", email);
            }
            startActivity(intent);
        });

        textDangKyNgay.setOnClickListener(v ->
                startActivity(new Intent(GiaoDienDangNhap.this, DangKy.class))
        );
        btnDangNhap.setOnClickListener(v -> {
            String email = eTextEmail.getText().toString().trim();
            String password = eTextMatKhau.getText().toString().trim();
            currentEmail = email;
            viewModel.login(email, password);
        });

        btnGoogle.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            googleLoginLauncher.launch(signInIntent);
        });

        btnFacebook.setOnClickListener(v -> {
            // Xin quyền cơ bản (email có thể null nếu Facebook account không public email)
            LoginManager.getInstance().logInWithReadPermissions(
                    GiaoDienDangNhap.this,
                    Arrays.asList("email", "public_profile")
            );
        });
    }
    private void openOtpScreen(String email) {
        Intent intent = new Intent(GiaoDienDangNhap.this, DangNhapOTP.class);
        intent.putExtra("EMAIL", email);
        startActivity(intent);
        finish();
    }

    // Dùng chung cho Google/Facebook (vì backend trả NguoiDung như nhau)
    private void onSocialLoginSuccess(NguoiDung nguoiDung) {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        prefs.edit()
                .putBoolean("isLoggedIn", true)
                .putString("userEmail", nguoiDung.getEmail())
                .putString("MA_NGUOI_DUNG", nguoiDung.getMaNguoiDung())
                .apply();

        Intent intent = new Intent(GiaoDienDangNhap.this, OnboardingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
