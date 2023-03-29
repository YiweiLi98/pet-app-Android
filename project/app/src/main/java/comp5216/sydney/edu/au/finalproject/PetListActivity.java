package comp5216.sydney.edu.au.finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import comp5216.sydney.edu.au.finalproject.adapter.HomeListAdapter;
import comp5216.sydney.edu.au.finalproject.adapter.PetListAdapter;
import comp5216.sydney.edu.au.finalproject.pojo.Pet;
import comp5216.sydney.edu.au.finalproject.pojo.Post;
import comp5216.sydney.edu.au.finalproject.pojo.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PetListActivity extends AppCompatActivity {
    private ImageView back;
    private RecyclerView recyclerView;
    private List<Pet> petList;
    private PetListAdapter petListAdapter;
    private Context context;
    private User User_temp;
    private User User_fromMe;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);
        context = this;
        back = findViewById(R.id.pet_list_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.petList);
        getMyProfile();


    }
    public void initview(){
        petListAdapter = new PetListAdapter(this, petList, new PetListAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Pet pet, int position) {
                Intent intent = new Intent(context, PetDetailActivity.class);
                intent.putExtra("pet", pet);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(Pet pet, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PetListActivity.this);
                builder.setTitle("Delete pet").setMessage("Are you sure to delete " + pet.getPetName() + "?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deletePet(pet.getPetId(), position);
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create().show();
            }


        });
        recyclerView.setAdapter(petListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getMyProfile() {
        String url = "http://35.189.24.208:8080/api/profile";
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("cookie", ToolManager.getINSTANCE().getSessionId())
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                ThreadUtils.runOnUiThread(() -> {
                    JSONObject body;
                    System.out.println(" SUCCESS");
                    try {
                        body = new JSONObject(result);
                        JSONObject data = (JSONObject) body.get("data");
                        Gson gson = new Gson();
                        User user = gson.fromJson(data.toString(), User.class);
                        User_temp = user;
                        List<Pet> pets = user.getPetList();
                        if (CollectionUtils.isEmpty(pets)) {
                            pets = new ArrayList<>();
                        }
                        petList = pets;
                        initview();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }


    private void deletePet(int petId, int position){
        String url = "http://35.189.24.208:8080/api/pet/";
        url = url+petId;
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("cookie", ToolManager.getINSTANCE().getSessionId())
                .delete()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv = findViewById(R.id.tv);
                        tv.setText("No such pet found");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = (response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        Object o = gson.fromJson(result, Object.class);
                        JSONObject body = null;
                        try {
                            body = new JSONObject(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Integer status = null;
                        try {
                            status = (Integer) body.get("status");
                            if(status == 200){
                                Toast.makeText(context, "Pet Deleted!", Toast.LENGTH_LONG).show();
                                petList.remove(position);
                                petListAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

}