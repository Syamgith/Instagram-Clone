/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.model.people.Person;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener {
  TextView loginTextView;
  boolean signUpModeActive = true;
  EditText usernameEditText;
  EditText passwordEditText;

  public void showUserList() {
    Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
    startActivity(intent);
  }

  public void signUpClicked (View view) {

    if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches(" ")) {
      Toast.makeText(this,"Username and Password is required",Toast.LENGTH_SHORT).show();
    } else {
      if (signUpModeActive) {

        ParseUser user = new ParseUser();
        user.setUsername(usernameEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if (e == null) {
              Log.i("Login", "Success");
              showUserList();
            } else {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      } else {
        //Login
        ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {
            if (user != null){
              Log.i("Login","Ok!");
              showUserList();
            } else {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
    }
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    usernameEditText = (EditText) findViewById(R.id.usernameEditText);
    passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    loginTextView = (TextView) findViewById(R.id.loginTextView);
    ImageView logoImageView = (ImageView) findViewById(R.id.logoimageView);
    RelativeLayout backgroundLayout = (RelativeLayout)findViewById(R.id.relativelayoutid);
    
    passwordEditText.setOnKeyListener(this);
    logoImageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
      }
    });

    backgroundLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
      }
    });

    loginTextView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Button signUpButton = (Button)findViewById(R.id.signUpButton);
        if (signUpModeActive) {
          signUpButton.setText("Login");
          loginTextView.setText("Or, SignUp");
          signUpModeActive = false;
        } else  {
          signUpButton.setText("SignUp");
          loginTextView.setText("Or, Login");
          signUpModeActive = true;
        }

      }
    });
    if (ParseUser.getCurrentUser() != null) {
      showUserList();
    }


    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

  @Override
  public boolean onKey(View v, int i, KeyEvent event) {

    if (i == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN ) {
      signUpClicked(v);
    }

    return false;
  }
}