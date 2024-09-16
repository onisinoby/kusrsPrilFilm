package com.example.prilogeniefilmov;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AutoriseActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, signUpButton;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autorise);

        emailEditText =     findViewById(R.id.emailEditText);
        passwordEditText =  findViewById(R.id.passwordEditText);
        loginButton =       findViewById(R.id.loginButton);
        signUpButton =      findViewById(R.id.signUpButton);
        firebaseAuth =      FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email =      emailEditText.getText().toString();
                String password =   passwordEditText.getText().toString();

                signUp(email, password);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email =      emailEditText.getText().toString();
                String password =   passwordEditText.getText().toString();

                signIn(email, password);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Toast.makeText(this, "Вы вошли как " + user.getEmail(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void signUp(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(AutoriseActivity.this, "Заполните поля", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    assert user != null;
                    sendEmailVerification();
                } else {
                    Toast.makeText(AutoriseActivity.this, "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signIn(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(AutoriseActivity.this, "Заполните поля", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    assert user != null;
                    if (user.isEmailVerified()) {
                        Toast.makeText(AutoriseActivity.this, "Вы успешно вошли", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AutoriseActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AutoriseActivity.this, "Подтвердите вашу почту для входа", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AutoriseActivity.this, "Ошибка входа", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AutoriseActivity.this, "На вашу почту было отправлено письмо для подтверждения", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AutoriseActivity.this, "Ошибка подтверждения адреса", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}