package com.skhu.cse.promiss;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skhu.cse.promiss.database.BasicDB;
import com.skhu.cse.promiss.keyboard.SoftKeyboard;
import com.skhu.cse.promiss.server.GetJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText editText_ID;
    EditText editText_PW;
    EditText editText_PW_check;
    TextView textView_ID_check;
    TextView textView_PW_condition;
    TextView textView_PW_check;
    InputMethodManager inputManager;
    RelativeLayout loginView;
    View center_view;
    SoftKeyboard softKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        center_view = findViewById(R.id.login_center_view);
//        inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        loginView=findViewById(R.id.login);
//        softKeyboard =new SoftKeyboard(loginView,inputManager);
//        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {
//            @Override
//            public void onSoftKeyboardHide() {
//
//            }
//            @Override
//            public void onSoftKeyboardShow() {
//                RegisterActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        center_view.setVisibility(View.GONE);
//                    }
//                });
//            }
//        });
        editText_ID = findViewById(R.id.register_edit_id);
        textView_ID_check = findViewById(R.id.register_id_check);
        textView_ID_check.setVisibility(View.GONE);  // 이미 존재하는 ID입니다. GONE
        editText_PW = findViewById(R.id.register_edit_password);
        textView_PW_condition = findViewById(R.id.register_pw_condition); //비밀번호는 6~18자
        editText_PW_check = findViewById(R.id.register_edit_password_check);
        textView_PW_check = findViewById(R.id.register_pw_check);
        Button button_OK = findViewById(R.id.register_button_ok);
        button_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String id = editText_ID.getText().toString();
                final String pw = editText_PW.getText().toString();
                final String pw_check = editText_PW_check.getText().toString();
                if (id != null && id.equals("")) {
                    Toast.makeText(getApplicationContext(),"아이디를 입력하십시오.",Toast.LENGTH_LONG).show();
                }
                if(pw!=null&&pw.equals("")){
                    Toast.makeText(getApplicationContext(),"비밀번호를 입력하십시오.",Toast.LENGTH_LONG).show();
                }
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        GetJson getJson = GetJson.getInstance();
                        getJson.requestPost("api/User/register", callback, "id", id, "pw", pw);
                    }
                }.run();
            }
        });
        editText_PW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editText_PW.getText().toString().length()>=6&&editText_PW.getText().toString().length()<=18) {
                    textView_PW_condition.setVisibility(View.GONE);
                }
            }
        });

        editText_PW_check.addTextChangedListener(new TextWatcher() { //키보드 입력받으면서 비밀번호 확인
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable editable) {
                if(editText_PW_check.getText().toString().equals(editText_PW.getText().toString())){
                    textView_PW_check.setText("일치 ");
                    textView_PW_check.setTextColor(getResources().getColor(R.color.same));
                }
            }
        });
    }

    private Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) { // 통신 실패

            RegisterActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RegisterActivity.this, "네트워크 연결 실패", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException { // 통신 성공
            String result = response.body().string();
            Log.d("server response:", result);
            try {
                JSONObject object = new JSONObject(result);

                if (object.getInt("result")==2000) { //성공
                   object =object.getJSONObject("data");
                    BasicDB.setUserInfo(getApplicationContext(),object.getString("user_name"),object.getString("user_pw"),object.getInt("id"));

                    BasicDB.setAppoint(getApplicationContext(),-1);

                    Intent intent = new Intent(RegisterActivity.this, MapActivity.class);
                    startActivity(intent);
                    finish();
                } else { //실패
                    final String message = object.getString("message");
                    if(message.equals("ID중복")){
                        RegisterActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView_ID_check.setVisibility(View.VISIBLE); //외부 스레드에서 UI 변경할때
                            }
                        });
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // JSONArray array;
        }
    };
}