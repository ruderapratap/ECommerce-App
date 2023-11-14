package com.ruderarajput.ecommerce.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ruderarajput.ecommerce.R;
import com.ruderarajput.ecommerce.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(this,googleSignInOptions);

        binding.googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getsignInIntent= googleSignInClient.getSignInIntent();
                launcher.launch(getsignInIntent);
            }
        });

        binding.dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.etEmail.getText().toString().trim();
                String password = binding.etPassword.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                    binding.etEmail.requestFocus();
                } else if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                    binding.etPassword.requestFocus();
                } else {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Sign In Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        getSupportActionBar().hide();
    }
    private ActivityResultLauncher<Intent> launcher=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result->{
                if (result.getResultCode()== Activity.RESULT_OK){
                    Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    if (task.isSuccessful()){
                        GoogleSignInAccount account=task.getResult();
                        AuthCredential credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
                        auth.signInWithCredential(credential).addOnCompleteListener(this,singInTask->{
                            if (singInTask.isSuccessful()){
                                startActivity(new Intent(this,MainActivity.class));
                                Toast.makeText(this, "Google Sign In Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(this, "Google Sign In Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(this, "Google Sign In Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
}