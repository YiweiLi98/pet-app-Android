package comp5216.sydney.edu.au.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResetActivity extends AppCompatActivity {
    private Button btnOk;
    private Button btnSendCode;
    private EditText etUsername;
    private EditText etEmail;
    private EditText etVerification;
    private EditText etNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        etUsername = (EditText) findViewById(R.id.inputResetUsername);
        etEmail = (EditText) findViewById(R.id.inputResetEmail);
        etVerification = (EditText) findViewById(R.id.inputVerification);
        etNewPassword = (EditText) findViewById(R.id.inputNewPassword);

        btnSendCode = (Button) findViewById(R.id.btnSendCode);
        btnOk = (Button) findViewById(R.id.btnOk);

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                sendEmailByUsername(userName, email);

            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = etUsername.getText().toString();
                String password = etNewPassword.getText().toString();
                String code = etVerification.getText().toString();

                validateCode(userName, password, code);

            }
        });

        Button btn = findViewById(R.id.btnCancel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetActivity.this, LoginActivity.class));
            }
        });

    }

    private void sendEmailByUsername(String userName, String email) {
        String url = "http://35.189.24.208:8080/api/email";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，

        JSONObject json = new JSONObject();
        try {
            json.put("userName", userName);
            json.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        ToolManager.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    runOnUiThread(() -> Toast.makeText(ResetActivity.this, "Send code successfully！", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> {
                        ///send code失败  弹出失败提示
                        Toast.makeText(ResetActivity.this, "Fail to send the code", Toast.LENGTH_SHORT).show();
                    });

                }
            }
        });
    }

    private void validateCode(String userName, String password, String code) {
        String url = "http://35.189.24.208:8080/api/validate";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        JSONObject json = new JSONObject();
        try {
            json.put("userName", userName);
            json.put("password", password);
            json.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        ToolManager.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ResetActivity.this, "reset password successfully！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ResetActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ///reset失败  弹出失败提示
                            Toast.makeText(ResetActivity.this, "Fail to reset password", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}

