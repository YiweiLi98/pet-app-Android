package comp5216.sydney.edu.au.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import comp5216.sydney.edu.au.finalproject.adapter.RequestAdapter;
import comp5216.sydney.edu.au.finalproject.pojo.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FriendRequestActivity extends Activity {
    private RecyclerView recyclerView;
    private RequestAdapter adapter;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Populate the screen using the layout
        setContentView(R.layout.activity_request);
        recyclerView = findViewById(R.id.rvRequests);
        getRequestList();
        initial();
    }

    public void onBack(View v) {
        setResult(RESULT_OK);
        finish(); // Close the activity then return to parent
    }

    private void acceptFriendRequest(int toId){
        String url = "http://35.189.24.208:8080/api/friends/";
        url = url+toId;
        RequestBody body = RequestBody.create(null, new byte[]{});
        Request request = new Request.Builder()
                .url(url)
                .addHeader("cookie", ToolManager.getINSTANCE().getSessionId())
                .post(body)
                .build();
        ToolManager.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("FAILED", e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200) {
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(FriendRequestActivity.this, "accept success", duration);
                            toast.show();
                            Log.i("SUCCESS", "add friend");
                        } else {
                            Log.i("FAILED", String.valueOf(response.code()));
                        }

                    }
                });
            }
        });
    }

    private void rejectFriendRequest(int toId){
        String url = "http://35.189.24.208:8080/api/friends/";
        url = url + toId;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("cookie", ToolManager.getINSTANCE().getSessionId())
                .delete()
                .build();
        ToolManager.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("FAILED", e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(FriendRequestActivity.this, "reject success", duration);
                        toast.show();
                        Log.i("SUCCESS", "reject user");
                    }
                });
            }
        });

    }

    private void getRequestList(){
        String url = "http://35.189.24.208:8080/api/friends/requests";
        Request request = new Request.Builder()
                .url(url)
                .addHeader("cookie", ToolManager.getINSTANCE().getSessionId())
                .get()
                .build();
        ToolManager.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject body = null;
                System.out.println(" SUCCESS");

                try {
                    String result = response.body().string();
                    Type type = new TypeToken<List<User>>() {
                    }.getType();
                    body = new JSONObject(result);
                    Object data = body.get("data");
                    Gson gson = new Gson();
                    runOnUiThread(() -> {
                        userList = gson.fromJson(data.toString(), type);
                        initial();
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void initial(){
        adapter = new RequestAdapter(this, userList, new RequestAdapter.OnItemClickListener(){
            @Override
            public void onClick(int pos) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FriendRequestActivity.this);
                builder.setTitle("Accept request")
                        .setMessage("Do you want to accept this request?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                acceptFriendRequest(userList.get(pos).getId());
                                userList.remove(pos); // Remove item from the ArrayList
                                adapter.notifyDataSetChanged(); // Notify adapter to update
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User cancelled the dialog
                                // Nothing happens
                            }
                        });
                builder.create().show();
            }
        }, new RequestAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(int pos) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FriendRequestActivity.this);
                builder.setTitle("Reject request")
                        .setMessage("Do you want to Reject this request?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                rejectFriendRequest(userList.get(pos).getId());
                                userList.remove(pos); // Remove item from the ArrayList
                                adapter.notifyDataSetChanged(); // Notify adapter to update
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User cancelled the dialog
                                // Nothing happens
                            }
                        });
                builder.create().show();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL); //VERTICAL
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

}
