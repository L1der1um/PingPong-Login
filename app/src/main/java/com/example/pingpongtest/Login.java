package com.example.pingpongtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText mEmailEditText;
    EditText mPasswordEditText;
    Button mLoginButton;
    TextView mRegisterTextView;

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailEditText = findViewById(R.id.login_email);
        mPasswordEditText = findViewById(R.id.login_password);
        mLoginButton = findViewById(R.id.login_button);
        mRegisterTextView = findViewById(R.id.register_text);

        mDatabaseHelper = new DatabaseHelper(this);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Insira o campo e-mail", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Insira o campo senha", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if user exists in the database
                boolean userExists = mDatabaseHelper.checkUser(email, password);

                if (userExists) {
                    Toast.makeText(getApplicationContext(), "Você se logou com sucesso!", Toast.LENGTH_SHORT).show();

                    // Launch home activity
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);

                    // Close login activity
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "E-mail ou senha inválidos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mRegisterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicializa a atividade (tela) de cadastro)
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);

                // Fecha a tela de login
                finish();
            }
        });
    }
}
