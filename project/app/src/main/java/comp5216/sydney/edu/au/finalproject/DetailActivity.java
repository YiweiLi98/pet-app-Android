package comp5216.sydney.edu.au.finalproject;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import comp5216.sydney.edu.au.finalproject.adapter.CommentListAdapter;
import comp5216.sydney.edu.au.finalproject.pojo.Comment;
import comp5216.sydney.edu.au.finalproject.pojo.Post;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class DetailActivity extends AppCompatActivity {
    private Context context;
    private RecyclerView recyclerView;
    private CommentListAdapter commentListAdapter;
    private ImageView user_photo;
    private TextView user_name;
    private TextView main_title;
    private TextView sub_title;
    private ImageView back_button;
    private ImageSlider imageSlider;
    private List<Comment> comments;
    private Button btn_comment;
    private MainActivity mainActivity;
    private EditText newComment;
    private ImageView post_like;
    private TextView like_num;
    private Post post;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        context = this;
        initView();
    }

    private void initView() {
        user_photo = findViewById(R.id.user_photo);
        user_name = findViewById(R.id.user_name);
        main_title = findViewById(R.id.mainTitle);
        sub_title = findViewById(R.id.subtitle);
        back_button = findViewById(R.id.iv_back);
        post = (Post) getIntent().getSerializableExtra("postData");
        if(post.getUserAvatar() != null){
            Uri avatar = Uri.parse(post.getUserAvatar());
            user_photo.setImageURI(avatar);
        }
        user_photo.setOnClickListener(v -> OtherActivity.launch(DetailActivity.this,post.getUserId()));
        user_name.setOnClickListener(v -> OtherActivity.launch(DetailActivity.this,post.getUserId()));
        user_name.setText(post.getNickName());
        main_title.setText(post.getTopic());
        sub_title.setText(post.getPostContent());
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // set image slider
        imageSlider = findViewById(R.id.slider);
        List<SlideModel> slideModels = new ArrayList<>();
        for(int i = 0; i < post.getImageUrlList().size(); i++){
            slideModels.add(new SlideModel(post.getImageUrlList().get(i), ScaleTypes.FIT));
        }
        imageSlider.setImageList(slideModels);

        // list
        comments = (List<Comment>) getIntent().getSerializableExtra("comments");
        recyclerView = findViewById(R.id.rv);
        commentListAdapter = new CommentListAdapter(this, comments);
        recyclerView.setAdapter(commentListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // like
        post_like = findViewById(R.id.post_like);
        if(post.isLoved()){
            post_like.setSelected(true);
        }else{
            post_like.setSelected(false);
        }
        post_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_like.setSelected(!post_like.isSelected());
                if(post_like.isSelected()){
                    love(post.getPostId());
                    int temp = Integer.parseInt((String) like_num.getText());
                    like_num.setText(String.valueOf(temp + 1));

                }else{
                    cancelLove(post.getPostId());
                    int temp = Integer.parseInt((String) like_num.getText());
                    like_num.setText(String.valueOf(temp - 1));
                }
            }
        });
        like_num = findViewById(R.id.post_like_num);
        like_num.setText(String.valueOf(post.getLove()));

        // comment
        newComment = findViewById(R.id.getComment);
        btn_comment = findViewById(R.id.btn_comment);

        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newCommentLine = newComment.getText().toString();
                newComment.setText("");

                Map<String, String> map = new HashMap<>();
                if(newCommentLine.length() > 0){
                    map.put("commentContent", newCommentLine);
                    addComment(post.getPostId(), map, post);
                }

            }
        });
    }

    private JSONObject toJsonObj(Map<String, Comment> map, JSONObject resultJson){
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            try {
                resultJson.put(key, map.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return resultJson;
    }

    private void addComment(int postId, Map map, Post post) {
        String url = "http://35.189.24.208:8080/api/Post/addComment/";
        url = url+postId;
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).build();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        JSONObject test = new JSONObject();
        JSONObject json = toJsonObj(map,test);

        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
        Request request = new Request.Builder()
                .url(url)
                .addHeader("cookie", ToolManager.getINSTANCE().getSessionId())
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv = findViewById(R.id.tv);
                        tv.setText("Comment invalid");
                    }
                });            }

            @Override
            public void onResponse(Call call, Response response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(response.code()==200)
                            Toast.makeText(context, "Commented", Toast.LENGTH_LONG).show();
                            getCommentsByPostId(postId);
                    }
                });
            }
        });
    }

    public void getCommentsByPostId(int postId){
        String url = "http://35.189.24.208:8080/api/getCommentsByPostId/";
        url = url+postId;
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("cookie", ToolManager.getINSTANCE().getSessionId())
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv = findViewById(R.id.tv);
                        tv.setText("No such post");
                    }
                });            }

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
                    comments.removeAll(comments);
                    List<Comment> commentList = gson.fromJson(data.toString(), type);
                    for(Comment comment: commentList){
                        comments.add(comment);
                    }

                    runOnUiThread(() -> commentListAdapter.notifyDataSetChanged());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void love(int postId) {
        String url = "http://35.189.24.208:8080/api/love/";
        url = url+postId;
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("cookie", ToolManager.getINSTANCE().getSessionId())
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv = findViewById(R.id.tv);
                        tv.setText("add love fail");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) {
                if(response.code() == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Loved!", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });
    }

    public void cancelLove(int postId) {
        String url = "http://35.189.24.208:8080/api/love/";
        url = url+postId;
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
                        tv.setText("cancel love fail");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response)  {
                if(response.code() == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Love Canceled!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

}