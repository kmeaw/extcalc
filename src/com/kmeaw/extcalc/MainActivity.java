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

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.content.ContentValues;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends Activity implements OnClickListener {

  EditText etNum1;

  Button btnOK;

  TextView tvResult;

  int num1, num2, op, result;
  Random rand = new Random();

  final int MAGIC = 31337;

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
    String oper = "???";
    do {
      op = rand.nextInt(4);
      num1 = rand.nextInt(500);
      num2 = rand.nextInt(500);
      switch(op)
      {
	case 0:
	  oper = "+"; 
	  result = num1 + num2;
	  break;
	case 1:
	  oper = "-"; 
	  if (num1 < num2)
	  {
	    result = num1;
	    num1 = num2;
	    num2 = result;
	  }
	  result = num1 - num2;
	  break;
	case 2:
	  oper = "*";
	  result = num1 * num2;
	  break;
	case 3:
	  oper = "/"; 
	  result = num1;
	  num1 *= num2;
	  break;
      }
    } while (result < 0 || result > 1000);

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

    DbOpenHelper dbOpenHelper = new DbOpenHelper(MainActivity.this);
    SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

    if (user_result != MAGIC) {
      ContentValues cv = new ContentValues();
      cv.put("num1", num1);
      cv.put("num2", num2);
      cv.put("user", user_result);
      cv.put("result", result);
      cv.put("op", op);
      
      db.insert(DbOpenHelper.TABLE_NAME,null,cv);
    } else {
      try 
      {
	HttpClient httpclient = new DefaultHttpClient();
	HttpPost httppost = new HttpPost("http://extcalc.kmeaw.com/accept");
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	Cursor cursor = db.rawQuery("SELECT 'k' || id, num1 || '-' || num2 || '-' || user || '-' || op || '-' || created_at FROM " + DbOpenHelper.TABLE_NAME + " WHERE done = 0", null);
	cursor.moveToFirst();
	while (!cursor.isAfterLast()) {
	  nameValuePairs.add(new BasicNameValuePair(cursor.getString(0), cursor.getString(1)));
	  cursor.moveToNext();
	}
	cursor.close();
	httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	HttpResponse response = httpclient.execute(httppost);
	Log.i("postData", response.getStatusLine().toString());
	ContentValues done1 = new ContentValues();
	done1.put("done", 1);
	db.update(DbOpenHelper.TABLE_NAME, done1, null, null);
      }
      catch (Exception e)
      {
	Log.e("http", e.toString());
      }
      db.close();
    }

    if (user_result == MAGIC)
      gen();
    else if (user_result == result)
    {
      Toast.makeText(context, "Всё правильно!", 4).show();
      gen();
    }
    else
      Toast.makeText(context, "Неправильно.", 4).show();
  }
}
