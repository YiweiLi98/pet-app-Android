package comp5216.sydney.edu.au.finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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

import comp5216.sydney.edu.au.finalproject.databinding.FragmentMeBinding;
import comp5216.sydney.edu.au.finalproject.pojo.Comment;
import comp5216.sydney.edu.au.finalproject.pojo.Pet;
import comp5216.sydney.edu.au.finalproject.pojo.Post;
import comp5216.sydney.edu.au.finalproject.pojo.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeFragment extends Fragment {
    private static final String TAG = "MeFragment";
    private FragmentMeBinding binding;
    private User mUser;

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
            helper.itemView.setOnClickListener(v -> getPostById(item.getPostId()));
            helper.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Delete pet").setMessage("Are you sure to delete the post?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deletePost(item.getPostId());
                                }
                            }).setNegativeButton("NO", (dialogInterface, i) -> {

                            });
                    builder.create().show();
                    return false;
                }
            });

        }
    };


    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //当前fragment转为可见状态
        if (isVisibleToUser) {
            getMyProfile();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getMyProfile();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeFragment newInstance(String param1, String param2) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMeBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        binding.btnLogOut.setOnClickListener(view -> {
            Log.i(TAG, "initView: log out");
            ToolManager.getINSTANCE().setSessionId(null);
            ToolManager.getINSTANCE().setUserId(0);
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });
        binding.btnEdit.setOnClickListener(view -> {
            if (mUser == null) {
                ToastUtils.showShort("user info is null, can't modify info");
            } else {
                ModifyMeActivity.launch(getActivity(), mUser);
            }
        });
        binding.allPets.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), PetListActivity.class);
            startActivity(intent);
        });
        initRecyclerView();
    }

    private void initRecyclerView() {
        mPostAdapter.bindToRecyclerView(binding.rvPosts);
        binding.rvPets.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mPetAdapter.bindToRecyclerView(binding.rvPets);
    }

    private void getMyProfile() {
        String url = "http://35.189.24.208:8080/api/profile";
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
                String result = response.body().string();
                ThreadUtils.runOnUiThread(() -> {
                    JSONObject body;
                    System.out.println(" SUCCESS");
                    try {
                        body = new JSONObject(result);
                        JSONObject data = (JSONObject) body.get("data");
                        Gson gson = new Gson();
                        User user = gson.fromJson(data.toString(), User.class);
                        mUser = user;
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

    public void getCommentsByPostId(int postId, Post post) {
        String url = "http://35.189.24.208:8080/api/getCommentsByPostId/";
        url = url + postId;
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
                Type type = new TypeToken<List<Comment>>() {
                }.getType();
                try {
                    String result = response.body().string();
                    body = new JSONObject(result);
                    Object data = body.get("data");
                    Gson gson = new Gson();
                    List<Comment> commentList = gson.fromJson(data.toString(), type);
                    startDetailActivity(post, commentList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void deletePost(int postId) {
        String url = "http://35.189.24.208:8080/api/post/deletePost/";
        url = url + postId;
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("cookie", ToolManager.getINSTANCE().getSessionId())
                .delete()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showShort("Post Deleted Failure!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = (response.body().string());
                LogUtils.i("result = " + result);
                ToastUtils.showShort("Post Deleted!");
                getMyProfile();
            }
        });
    }

    public void startDetailActivity(Post post, List<Comment> comments) {
        Intent intent = new Intent(this.getActivity(), DetailActivity.class);
        intent.putExtra("postData", post);
        intent.putExtra("comments", (Serializable) comments);
        startActivity(intent);
    }

    private void getPostById(int id) {
        String url = "http://35.189.24.208:8080/api/getPost/";
        url+=id;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("cookie", ToolManager.getINSTANCE().getSessionId())
                .get()
                .build();
        ToolManager.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject body = null;

                try {
                    String result = response.body().string();
                    body = new JSONObject(result);
                    JSONObject data = (JSONObject) body.get("data");
                    Gson gson = new Gson();
                    Post post = gson.fromJson(data.toString(), Post.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getCommentsByPostId(post.getPostId(), post);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}