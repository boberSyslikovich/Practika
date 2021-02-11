package com.pupukaka.practika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthenticationActivity extends AppCompatActivity {
    private static final String TAG = "Firebase";
    private static final int SUPER_SECURITY_CODE = 0000;
    private EditText workspaceNumberEditText;
    private EditText nameEditText;
    private EditText securityCodeEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button logInSignUpButton;
    private TextView registrationSwitch;
    private ImageView modeSwitch;
    private boolean modeFlag = false;
    private boolean regFlag = true;
    private FirebaseAuth auth;
    private ImageView logo;

    private FirebaseDatabase database;
    private DatabaseReference  userDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userDatabaseReference = database.getReference().child("users");

        // инициализация элементов интерфейса
        nameEditText = findViewById(R.id.nameEditText);
        securityCodeEditText = findViewById(R.id.securityCodeEditText);
        emailEditText = findViewById(R.id.emailEditText);
        workspaceNumberEditText = findViewById(R.id.workspaceEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        logInSignUpButton = findViewById(R.id.logInRegistrationButton);
        registrationSwitch = findViewById(R.id.registrationSwitchTextView);
        logo = findViewById(R.id.logoImageView);
        modeSwitch = findViewById(R.id.modeSwitchImageView);
        logInView();


        modeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInView();
            }
        });
        registrationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpView();
            }
        });

        logInSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInSignUp(emailEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim());
            }
        });

        if (auth.getCurrentUser()!=null){
            startActivity(new Intent(AuthenticationActivity.this, TaskActivity.class));
        }

    }

    private void logInSignUp(String email, String password){
        if (!modeFlag && Integer.parseInt(securityCodeEditText.getText().toString().trim())!=SUPER_SECURITY_CODE){
            Toast.makeText(AuthenticationActivity.this, "Не правильний код безпеки!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (regFlag) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser();
                                startActivity(new Intent(AuthenticationActivity.this, TaskActivity.class));
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(AuthenticationActivity.this, "Помилка авторизації!",
                                        Toast.LENGTH_LONG).show();
                                //updateUI(null);
                                // ...
                            }
                            // ...
                        }
                    });
        } else {

            if (emailEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(AuthenticationActivity.this, "Будь ласка, вкажіть Ваш email!",
                             Toast.LENGTH_LONG).show();
            } else if (passwordEditText.getText().toString().trim().length() < 7) {
                        Toast.makeText(AuthenticationActivity.this, "Пароль повинен бути бульше 7 символів!",
                                Toast.LENGTH_LONG).show();
            } else if (nameEditText.getText().toString().trim().equals("")) {
                Toast.makeText(AuthenticationActivity.this, "Будь ласка, вкажіть Ваше призвіще, ім'я, по-батькові!",
                        Toast.LENGTH_LONG).show();
            } else if (workspaceNumberEditText.getText().toString().trim().equals("") && modeFlag) {
                Toast.makeText(AuthenticationActivity.this, "Будь ласка, вкажіть Ваш номер робочого місця!",
                        Toast.LENGTH_LONG).show();
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = auth.getCurrentUser();
                                    createUser(user);
                                    startActivity(new Intent(AuthenticationActivity.this, TaskActivity.class));

                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(AuthenticationActivity.this, "Помилка регістрації!",
                                            Toast.LENGTH_LONG).show();
                                    //updateUI(null);
                                }
                                // ...
                            }
                        });
            }
        }
    }


    private  void createUser (FirebaseUser firebaseUser){
        User user = new User();
        user.setUserId(firebaseUser.getUid());
        user.setEmail(firebaseUser.getEmail());
        user.setName(nameEditText.getText().toString().trim());

        if (modeFlag){
            user.setMode(0);
            user.setWorkplaceNumber(Integer.parseInt(workspaceNumberEditText.getText().toString().trim()));
        }else {
            user.setMode(1);
            user.setWorkplaceNumber(0000);
        }

        userDatabaseReference.push().setValue(user);
    }
    private void logInView(){
        if (modeFlag){
            emailEditText.setText("");
            passwordEditText.setText("");
            workspaceNumberEditText.setText("");
            nameEditText.setText("");
            securityCodeEditText.setText("");

            workspaceNumberEditText.setVisibility(View.GONE);
            nameEditText.setVisibility(View.GONE);
            securityCodeEditText.setVisibility(View.VISIBLE);
            modeSwitch.setImageResource(R.drawable.ic_noun_worker_35931);
            modeFlag = false;

        } else{
            emailEditText.setText("");
            passwordEditText.setText("");
            workspaceNumberEditText.setText("");
            nameEditText.setText("");
            securityCodeEditText.setText("");

            workspaceNumberEditText.setVisibility(View.GONE);
            nameEditText.setVisibility(View.GONE);
            securityCodeEditText.setVisibility(View.GONE);
            modeSwitch.setImageResource(R.drawable.ic_noun_worker_349656);
            modeFlag = true;
        }
    }

    private void signUpView(){
        TextView welcomeTextView = findViewById(R.id.textView);

        if (regFlag){
            if (modeFlag){
                workspaceNumberEditText.setVisibility(View.VISIBLE);
            }else{
                securityCodeEditText.setVisibility(View.VISIBLE);
            }
//            logo.setVisibility(View.GONE);
            welcomeTextView.setVisibility(View.GONE);
            nameEditText.setVisibility(View.VISIBLE);
            logInSignUpButton.setText(R.string.button_text2);
            registrationSwitch.setText(R.string.sign_up);
            modeSwitch.setVisibility(View.GONE);
            regFlag = false;
        } else{
            if (modeFlag){
                workspaceNumberEditText.setVisibility(View.GONE);
            }else{
                securityCodeEditText.setVisibility(View.VISIBLE);
            }
            nameEditText.setVisibility(View.GONE);
//            logo.setVisibility(View.VISIBLE);
            logInSignUpButton.setText(R.string.button_text1);
            registrationSwitch.setText(R.string.login);
            welcomeTextView.setVisibility(View.VISIBLE);
            modeSwitch.setVisibility(View.VISIBLE);
            regFlag= true;
        }

    }

}