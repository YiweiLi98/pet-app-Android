package comp5216.sydney.edu.au.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import comp5216.sydney.edu.au.finalproject.pojo.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private EditText username;
    private EditText password;
    private MySQLiteOpenHelper mySQLiteOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.inputLoginUsername);
        password = findViewById(R.id.inputLoginPassword);
        login = findViewById(R.id.btnlogin);
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this);

        initData();


        login.setOnClickListener(v -> {

            String strUserName = username.getText().toString();
            String strPassWord = password.getText().toString();


            List<User> userList = mySQLiteOpenHelper.selectByUserNameAndPassword(strUserName, strPassWord);
            System.out.println(userList);

            SharedPreferences spf = getSharedPreferences("spfRecord", MODE_PRIVATE);
            SharedPreferences.Editor edit = spf.edit();
            edit.putString("strUserName", strUserName);
            edit.putString("strPassWord", strPassWord);
            edit.apply();
            User user = new User();
            user.setUserName(strUserName);
            user.setPassword(strPassWord);
            login(user);

        });

        TextView btn = findViewById(R.id.textViewSignUp);
        btn.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
        TextView btn1 = findViewById(R.id.forgotPassword);
        btn1.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ResetActivity.class)));
    }


    private void login(User user) {

        String url = "http://35.189.24.208:8080/api/login";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        JSONObject json = new JSONObject();
        try {
            json.put("userName", user.getUserName());
            json.put("password", user.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = ToolManager.getClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response){


                String session = response.header("Set-Cookie");
                if (session != null) {
                    String newSessionId = session.substring(0, session.indexOf(";"));
                    Message message = new Message();
                    message.what = 1;
                    message.obj = newSessionId;
                    ToolManager.getINSTANCE().setSessionId(newSessionId);

                    runOnUiThread(() -> {
                        if (response.code() == 200 && response.body() != null) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                        } else {
                            Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_LONG).show();
                        }

                    });

                }

            }
        });

    }

    private void initData() {
        SharedPreferences spf = getSharedPreferences("spfRecord", MODE_PRIVATE);

        String strUserName = spf.getString("strUserName", "");
        String strPassWord = spf.getString("strPassWord", "");

        username.setText(strUserName);
        password.setText(strPassWord);

    }
}
