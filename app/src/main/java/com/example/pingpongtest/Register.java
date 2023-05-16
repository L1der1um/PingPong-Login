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

public class Register extends AppCompatActivity {

    EditText mEmailEditText;
    EditText mPasswordEditText;
    EditText mConfirmPasswordEditText;
    Button mRegisterButton;
    TextView mLoginTextView;

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailEditText = findViewById(R.id.register_email);
        mPasswordEditText = findViewById(R.id.register_password);
        mConfirmPasswordEditText = findViewById(R.id.register_confirm_password);
        mRegisterButton = findViewById(R.id.register_button);
        mLoginTextView = findViewById(R.id.login_text);

        mDatabaseHelper = new DatabaseHelper(this);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();
                String confirmPassword = mConfirmPasswordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Por favor preencha o campo e-mail", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Por favor preencha o campo senha", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Por favor preencha o campo de confirmar senha", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "As senhas não conferem", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verifica se o usuário ja existe no banco de dados
                boolean userExists = mDatabaseHelper.checkUser(email, password);

                if (userExists) {
                    Toast.makeText(getApplicationContext(), "Um usuário associado a este e-mail ja existe!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Cria um novo usuário no banco de dados
                long id = mDatabaseHelper.createUser(email, password);

                if (id > 0) {
                    Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();

                    // Inicializa a tela de login
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                    // Fecha a tela de cadastro
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Não foi possível realizar o cadastro!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        mLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicializa a tela de login
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);

                // Fecha a tela de cadastro
                finish();
            }
        });
    }
}
