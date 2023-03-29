package comp5216.sydney.edu.au.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson2.JSON;
import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import comp5216.sydney.edu.au.finalproject.databinding.ActivityOtherBinding;
import comp5216.sydney.edu.au.finalproject.pojo.Comment;
import comp5216.sydney.edu.au.finalproject.pojo.Pet;
import comp5216.sydney.edu.au.finalproject.pojo.Post;
import comp5216.sydney.edu.au.finalproject.pojo.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OtherActivity extends AppCompatActivity {
    private static final String KEY_ID = "id";
    private ActivityOtherBinding binding;
    private Integer mId = -1;

    public static void launch(Context context, int id) {
        Intent intent = new Intent(context, OtherActivity.class);
        intent.putExtra(KEY_ID, id);
        context.startActivity(intent);
    }

    private final BaseQuickAdapter<Pet, BaseViewHolder> mPetAdapter = new BaseQuickAdapter<Pet, BaseViewHolder>(R.layout.rv_pet_item) {
        @Override
        protected void convert(@NonNull BaseViewHolder helper, Pet item) {
            ImageView ivPet = helper.getView(R.id.ivPet);
            String url = item.getPetImageAddress();
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.ic_default)
                    .fallback(R.mipmap.ic_default)
                    .error(R.mipmap.ic_default);

            Glide.with(ivPet).load(url).apply(options).into(ivPet);
            helper.setText(R.id.tvCategory, item.getCategory());
            helper.setText(R.id.tvName, item.getPetName());
        }
    };
    private final BaseQuickAdapter<Post, BaseViewHolder> mPostAdapter = new BaseQuickAdapter<Post, BaseViewHolder>(R.layout.rv_post_item) {
        @Override
        protected void convert(@NonNull BaseViewHolder helper, Post item) {
            ImageView ivPost = helper.getView(R.id.ivPost);
            String url = "";
            if (CollectionUtils.isNotEmpty(item.getImageUrlList())) {
                url = item.getImageUrlList().get(0);
            }
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.ic_default)
                    .fallback(R.mipmap.ic_default)
                    .error(R.mipmap.ic_default);
            Glide.with(ivPost).load(url).apply(options).into(ivPost);
            //helper.itemView.setOnClickListener(v -> getCommentsByPostId(item.getPostId(), item));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mId = getIntent().getIntExtra(KEY_ID, -1);
        if (mId == -1) {
            ToastUtils.showShort("id illegal!");
            finish();
        }
        getFriendshipStatus(mId);
        initView();
        getMyProfile();

    }

    private void initView() {
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.btnAdd.getText().equals("Delete")){
                    deleteFriendFromList(mId);
                }else if(binding.btnAdd.getText().equals("Add")){
                    sendFriendRequest(mId);
                }

            }
        });

        binding.btnBack.setOnClickListener(v -> finish());
        initRecyclerView();
    }

    private void initRecyclerView() {
        mPostAdapter.bindToRecyclerView(binding.rvPosts);
        binding.rvPets.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mPetAdapter.bindToRecyclerView(binding.rvPets);
    }


    private void getMyProfile() {
        String url = "http://35.189.24.208:8080/api/profile/" + mId;
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
                    try {
                        body = new JSONObject(result);
                        JSONObject data = (JSONObject) body.get("data");
                        Gson gson = new Gson();
                        User user = gson.fromJson(data.toString(), User.class);
                        //LogUtils.i("user = " + user);
                        List<Post> posts = user.getPostList();
                        if (CollectionUtils.isEmpty(posts)) {
                            posts = new ArrayList<>();
                        }

                        List<Pet> pets = user.getPetList();
                        if (CollectionUtils.isEmpty(pets)) {
                            pets = new ArrayList<>();
                        }
                        mPostAdapter.setNewData(posts);
                        mPetAdapter.setNewData(pets);
                        binding.tvName.setText(user.getNickName());
                        binding.tvID.setText(String.valueOf(user.getUserName()));
                        binding.tvDescription.setText(user.getDescription());
                        Glide.with(binding.ivAvatar).load(user.getUserImageAddress()).into(binding.ivAvatar);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }


    private void getFriendshipStatus(int toId) {

        String url = "http://35.189.24.208:8080/api/friends/status/";
        url = url + toId;
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
            public void onResponse(Call call, Response response) {
                     if(response.code()==201){
                         OtherActivity.this.runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                binding.btnAdd.setText("Not add yourself");
                                binding.btnAdd.setEnabled(false);
                             }
                         });
                     }else {


                         try {

                             JSONObject jsonObject = new JSONObject(response.body().string());
                             final String data = (String) jsonObject.get("data");
                             response.body().close();
                             OtherActivity.this.runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     if(data.equals("Friend")){
                                         binding.btnAdd.setText("Delete");
                                         binding.btnAdd.setEnabled(true);
                                     }else if(data.equals("Requesting")){
                                         binding.btnAdd.setText("Pending");
                                         binding.btnAdd.setEnabled(false);
                                     }else if(data.equals("Nothing")) {
                                         binding.btnAdd.setText("Add");
                                         binding.btnAdd.setEnabled(true);
                                     }

                                 }
                             });
                         } catch (JSONException e) {
                             e.printStackTrace();
                         } catch (IOException e) {
                             e.printStackTrace();
                         }

                     }

            }
        });

    }

    private void sendFriendRequest(int toId) {
        String url = "http://35.189.24.208:8080/api/friends/";
        url = url + toId;
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
                ToastUtils.showShort("Success to send friend request");
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
                ToastUtils.showShort("delete success");
            }
        });
    }
}