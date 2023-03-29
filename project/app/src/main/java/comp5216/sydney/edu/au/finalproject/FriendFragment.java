package comp5216.sydney.edu.au.finalproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import comp5216.sydney.edu.au.finalproject.adapter.FriendAdapter;
import comp5216.sydney.edu.au.finalproject.pojo.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class FriendFragment extends Fragment {

    private RecyclerView recyclerView;
    private FriendAdapter adapter;
    private List<User> userList;
    ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                getFriendsList();
                if (result.getResultCode() == 1) {
                    Log.d("REQUEST", "update list");
                }
            }
    );

    @Override
    public void onResume() {
        super.onResume();
        initial();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        recyclerView = view.findViewById(R.id.rvFriends);
        initial();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getFriendsList();

        Button request = view.findViewById(R.id.btnToRequest);
        request.setOnClickListener(v -> {
            Log.i("PageActivity", "Go to Friend Request page");
            Intent intent = new Intent(getActivity(), FriendRequestActivity.class);
            mLauncher.launch(intent);

        });

    }

    public void initial(){
        adapter = new FriendAdapter(userList, new FriendAdapter.OnItemClickListener(){
            @Override
            public void onClick(int pos) {
                OtherActivity.launch(getActivity(), userList.get(pos).getId());
            }
        }, new FriendAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(int pos) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete Friend")
                        .setMessage("Do you want to Delete this friend?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteFriendFromList(userList.get(pos).getId());
                                userList.remove(pos); // Remove item from the ArrayList
                                adapter.notifyDataSetChanged(); // Notify adapter to update
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        });
                builder.create().show();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL); //VERTICAL
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void getFriendsList(){
        String url = "http://35.189.24.208:8080/api/friends";
        Request request = new Request.Builder()
                .url(url)
                .addHeader("cookie", ToolManager.getINSTANCE().getSessionId())
                .get()
                .build();
        ToolManager.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("FAILED",e.toString());
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


                    System.out.println("Friend list: "+userList);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            userList = gson.fromJson(data.toString(), type);
                            initial();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void deleteFriendFromList(int toId) {
        String url = "http://35.189.24.208:8080/api/friends/delete/";
        url = url + toId;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("cookie", ToolManager.getINSTANCE().getSessionId())
                .delete()
                .build();
        ToolManager.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showShort("delete fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200){
                    ToastUtils.showShort("delete success");
                }
            }
        });
    }

}