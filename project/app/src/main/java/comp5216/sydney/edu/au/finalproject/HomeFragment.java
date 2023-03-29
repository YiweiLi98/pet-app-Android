package comp5216.sydney.edu.au.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import comp5216.sydney.edu.au.finalproject.adapter.HomeListAdapter;
import comp5216.sydney.edu.au.finalproject.pojo.Comment;
import comp5216.sydney.edu.au.finalproject.pojo.Post;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private HomeListAdapter adapter;
    private List<Post> postList;
    private TextView textView;
    private Context context;
    private View viewCheck;
    private String currTimePage;
    private String currLikePage;
    private String pageSize;
    private Integer postOrder;
    private TextView post_sort;
    private ImageView post_refresh;
    private ImageView post_search;
    ActivityResultLauncher<Intent> myLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 1) {
                    String searchResult = (String) result.getData().getSerializableExtra("item");
                    getTrendingPosts(searchResult);
                }
            }
    );


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = this.getActivity();
        postList = new ArrayList<>();
        currTimePage = "1";
        currLikePage = "1";
        pageSize = "10";
        postOrder = 0;
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewCheck = view;
        recyclerView = view.findViewById(R.id.rv);
        post_refresh = view.findViewById(R.id.post_refresh);
        post_sort = view.findViewById(R.id.post_sort);
        post_search = view.findViewById(R.id.search);
        getPosts("0",pageSize, 1);


    }
    public void initView(){
//        get bundle data
//        Bundle bundle = getArguments();
//        postList = (List<Post>) bundle.getSerializable("data");
        System.out.println("postList " + postList);
        adapter = new HomeListAdapter(this, postList, new HomeListAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Post post) {

                if(ToolManager.isFastDoubleClick()){
                    return;
                }
                getPostById(post.getPostId());
            }

        });
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                staggeredGridLayoutManager.invalidateSpanAssignments();
            }
        });
        recyclerView.setAdapter(adapter);

        // refresh
        post_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ToolManager.isFastDoubleClick()){
                    return;
                }
                Toast.makeText(getActivity(), "Refreshed!", Toast.LENGTH_LONG).show();
//                int total = 1;
//                if(postList.size() > 0){
//                    if(postList.get(0).getTotalPosts() == null){
//                        total = postList.get(0).getTotalPosts();
//                    }else {
//                        total = 10;
//                    }
//                }
                Random random = new Random();
                Integer randomNum = random.nextInt(10);
                if(postOrder == 0){
                    getPosts(String.valueOf(randomNum), pageSize, 0);
                }else{
                    getPostsOrderByLove(String.valueOf(randomNum), pageSize);
                }
            }
        });
        post_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SearchActivity.class);
//                view.getContext().startActivity(new Intent(view.getContext(), SearchActivity.class));
                myLauncher.launch(intent);
            }
        });
        post_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View contentView = LayoutInflater.from(requireContext()).inflate(R.layout.sort_layout, null);
                PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                        true);
                popupWindow.showAsDropDown(view);

                View.OnClickListener clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id = view.getId();
                        if(id == R.id.sort1){
                            post_sort.setText("Sort by Time");
                            postOrder = 0;
                            postList.removeAll(postList);
                            getPosts("0", pageSize,1);
                            popupWindow.dismiss();
                            Toast.makeText(getActivity(), "Sort by Time!", Toast.LENGTH_LONG).show();
                        }
                        if(id == R.id.sort2){
                            post_sort.setText("Sort by Like");
                            postOrder = 1;
                            postList.removeAll(postList);
                            getPostsOrderByLove("0", pageSize);
                            popupWindow.dismiss();
                            Toast.makeText(getActivity(), "Sort by Like!", Toast.LENGTH_LONG).show();
                        }
                    }
                };

                contentView.findViewById(R.id.sort1).setOnClickListener(clickListener);
                contentView.findViewById(R.id.sort2).setOnClickListener(clickListener);
            }
        });
    }

    public void updateData(List<Post> posts){
        if(posts.size() == 0){
            Toast.makeText(context, "No relative data!", Toast.LENGTH_LONG).show();
            return;
        }
        postList.removeAll(postList);
        for(Post item: posts){
            postList.add(item);
        }
        adapter.notifyDataSetChanged();
    }
    public void addData(List<Post> posts){
        for(Post item: posts){
            postList.add(item);
        }
        adapter.notifyDataSetChanged();
    }
    public void startDetailActivity(Post post, List<Comment> comments){
        Intent intent = new Intent(viewCheck.getContext(), DetailActivity.class);
        intent.putExtra("postData", post);
        intent.putExtra("comments", (Serializable) comments);
        startActivity(intent);
    }
    public void getPosts(String currPage,String pageSize, Integer firstInit){
            HttpUrl.Builder builder = HttpUrl.parse("http://35.189.24.208:8080/api/getPosts").newBuilder();
            builder.addQueryParameter("currPage", currPage).addQueryParameter("pageSize", pageSize);
            Request request = new Request.Builder()
                    .url(builder.build())
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
                    Type type = new TypeToken<List<Post>>() {
                    }.getType();
                    try {
                        Gson gson = new Gson();
                        JSONObject body = new JSONObject(result);
                        Object data =  body.get("data");
                        List<Post> posts = gson.fromJson(data.toString(), type);
                        for(Post post: posts){
                            postList.add(post);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(firstInit == 1){
                                    initView();
                                }else{
                                    updateData(posts);
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
    }
    public void getTrendingPosts(String searchRes){
        String url = "http://35.189.24.208:8080/api/search/";
        url += searchRes;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("cookie", ToolManager.getINSTANCE().getSessionId())
                .get()
                .build();
        ToolManager.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject body = null;
                System.out.println(" SUCCESS");
                try {
                    String result = response.body().string();
                    Type type = new TypeToken<List<Post>>() {
                    }.getType();
                    body = new JSONObject(result);
                    Object data = body.get("data");
                    Gson gson = new Gson();
                    List<Post> posts = gson.fromJson(data.toString(), type);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateData(posts);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
    public void getCommentsByPostId(int postId, Post post){
        String url = "http://35.189.24.208:8080/api/getCommentsByPostId/";
        url = url+postId;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("cookie", ToolManager.getINSTANCE().getSessionId())
                .get()
                .build();
        ToolManager.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startDetailActivity(post, commentList);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public void getPostsOrderByLove(String currPage,String pageSize){
        HttpUrl.Builder builder = HttpUrl.parse("http://35.189.24.208:8080/api/getPostsOrderByLove").newBuilder();
        builder.addQueryParameter("currPage", currPage).addQueryParameter("pageSize", pageSize);
        Request request = new Request.Builder()
                .url(builder.build())
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
                System.out.println("---------------------------");
                System.out.println(result);
                Type type = new TypeToken<List<Post>>() {
                }.getType();
                Gson gson = new Gson();
                Object o = gson.fromJson(result, Object.class);
                JSONObject body = null;
                try {
                    body = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Object data = null;
                try {
                    data = body.get("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List<Post> posts = gson.fromJson(data.toString(), type);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateData(posts);
                    }
                });
            }
        });
    }

}