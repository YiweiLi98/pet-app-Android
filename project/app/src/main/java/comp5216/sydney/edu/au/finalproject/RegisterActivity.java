package comp5216.sydney.edu.au.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import comp5216.sydney.edu.au.finalproject.Utils.ToastUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password;
    private EditText conformPassword;
    private Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.inputUsername);
        email = (EditText) findViewById(R.id.inputEmail);
        password = (EditText) findViewById(R.id.inputPassword);
        conformPassword = (EditText) findViewById(R.id.inputConformPassword);
        register = (Button) findViewById(R.id.btnRegister);
        register.setEnabled(false);

        TextView btn = findViewById(R.id.alreadyHaveAccount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        name.setOnFocusChangeListener((view, b) -> {
            if (b) {

            } else {
                usernameCheck(name.getText().toString());
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strUserName = name.getText().toString().trim();
                String strEmail = email.getText().toString().trim();
                String strPassWord = password.getText().toString().trim();
                String strPassWordAgain = conformPassword.getText().toString().trim();


                if (TextUtils.isEmpty(strPassWord)) {
                    Toast.makeText(RegisterActivity.this, "password can not be blank", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!TextUtils.equals(strPassWord, strPassWordAgain)) {
                    Toast.makeText(RegisterActivity.this, "different passwords", Toast.LENGTH_LONG).show();
                    return;
                }

                if (strUserName == null || strUserName.length() == 0 || strUserName.length() > 10) {
                    Toast.makeText(RegisterActivity.this, "invalid username length", Toast.LENGTH_LONG).show();
                    return;
                }
                signup(strUserName, strPassWord, strEmail);


            }
        });


    }

    private void signup(String userName, String password, String email) {
        String url = "http://35.189.24.208:8080/api/signup";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("userName", userName);
            jsonObject.put("password", password);
            jsonObject.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = ToolManager.getClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("LMY FAIL");
            }

            @Override
            public void onResponse(Call call, Response response) {
                System.out.println("----------------------");
                if (response.code() == 200) {
                    runOnUiThread(() -> {
                        Toast.makeText(RegisterActivity.this, "Register successfully！", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ///注册失败  弹出失败提示
                            Toast.makeText(RegisterActivity.this, "Fail Register", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });
    }

    private void usernameCheck(String userName) {
        String url = "http://35.189.24.208:8080/api/username";
        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
        builder.addQueryParameter("userName", userName);
        Request request = new Request.Builder()
                .url(builder.build())
                .get()
                .build();
        ToolManager.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 200) {
                    runOnUiThread(() -> {
                        register.setEnabled(true);
                    });
                } else {
                    runOnUiThread(() -> {
                        register.setEnabled(false);
                        ToastUtil.toastLong(RegisterActivity.this, "Username is occupied");
                    });
                }
            }
        });
    }
}
