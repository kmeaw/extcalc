package com.kmeaw.extcalc;

import android.app.Activity;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.view.View.OnClickListener;
import android.text.TextUtils;
import android.view.View;

import java.util.Random;

import android.content.Context;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

  EditText etNum1;

  Button btnOK;

  TextView tvResult;

  int num1, num2, result;
  Random rand = new Random();

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    // find the elements
    etNum1 = (EditText) findViewById(R.id.etNum1);

    btnOK = (Button) findViewById(R.id.btnOK);

    tvResult = (TextView) findViewById(R.id.tvResult);
    
    gen();

    // set a listener
    btnOK.setOnClickListener(this);

  }

  void gen() {
    String oper = "+";
    num1 = rand.nextInt(50);
    num2 = rand.nextInt(50);
    result = num1 + num2;

    tvResult.setText(num1 + " " + oper + " " + num2 + " = ?");
    etNum1.setText("");
  }

  @Override
  public void onClick(View v) {
    int user_result = 0;
    Context context = getApplicationContext();

    // check if the fields are empty
    if (TextUtils.isEmpty(etNum1.getText().toString())) {
      return;
    }

    // read EditText and fill variables with numbers
    user_result = Integer.parseInt(etNum1.getText().toString());

    if (user_result == result)
    {
      Toast.makeText(context, "Всё правильно!", 4).show();
      gen();
    }
    else
      Toast.makeText(context, "Неправильно.", 4).show();
  }
}
