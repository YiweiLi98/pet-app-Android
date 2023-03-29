package comp5216.sydney.edu.au.finalproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;


import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.imagepipeline.common.SourceUriType;
import com.squareup.picasso.Picasso;

import java.util.List;

import comp5216.sydney.edu.au.finalproject.HomeFragment;
import comp5216.sydney.edu.au.finalproject.MainActivity;
import comp5216.sydney.edu.au.finalproject.R;
import comp5216.sydney.edu.au.finalproject.pojo.Post;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.HomeViewHolder> {

    private static final int TYPE_NORMAL_ITEM = 1;
    private static final int TYPE_BOTTOM_REFRESH_ITEM = 0;
    private LayoutInflater inflater = null;
    private HomeFragment context;
    private List<Post> posts;
    private ItemClickListener itemClickListener;
    public HomeListAdapter(HomeFragment context, List<Post> postList, ItemClickListener itemClickListener){
        this.context = context;
        this.posts = postList;
        this.itemClickListener = itemClickListener;
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView post_text;
        public ImageView post_like;
        public ImageView user_photo;
        public TextView user_name;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.post_iv);
            post_like = itemView.findViewById(R.id.post_like);
            post_text = itemView.findViewById(R.id.post_content);
            user_photo = itemView.findViewById(R.id.user_photo);
            user_name = itemView.findViewById(R.id.post_username);
        }
    }

    @NonNull
    @Override
    public HomeListAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // set list item layout and set click listener to start detail page
        if(inflater == null){
            inflater = LayoutInflater.from(parent.getContext());
        }
        View view = inflater.inflate(R.layout.item_home, parent, false);
        return new HomeViewHolder(view);
    }

    /**
     * Change dp = px
     */
    public static int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    public interface ItemClickListener{
        void onItemClick(Post post);
    }
    int minHeight = 150;
    int maxHeight = 150;


    @Override
    public void onBindViewHolder(@NonNull HomeListAdapter.HomeViewHolder holder, int position) {


        int height = (int) (minHeight + Math.random() * (maxHeight - minHeight + 1));
        View view = holder.imageView;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = dp2px(height);
        view.setLayoutParams(layoutParams);

        holder.itemView.setOnClickListener(view1 -> {
            itemClickListener.onItemClick(this.posts.get(position));
        });
        if(this.posts.get(position).getImageUrlList().size() > 0){
            Picasso.get().load(this.posts.get(position).getImageUrlList().get(0)).into(holder.imageView);
        }
        Uri avator = Uri.parse(this.posts.get(position).getUserAvatar());
        holder.user_photo.setImageURI(avator);
//        Picasso.get().load(this.posts.get(position).getUserAvatar()).into(holder.user_photo);
        holder.post_text.setText(this.posts.get(position).getTopic());
        holder.user_name.setText(this.posts.get(position).getNickName());
//        holder.like_num.setText(String.valueOf(this.posts.get(position).getLove()));
        Integer postId = this.posts.get(position).getPostId();
        if(this.posts.get(position).isLoved()){
            holder.post_like.setSelected(true);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position < posts.size()) {
            return TYPE_NORMAL_ITEM;
        }else{
            return TYPE_BOTTOM_REFRESH_ITEM;
        }
    }
    private class BottomRefreshViewHolder extends  RecyclerView.ViewHolder{

        public BottomRefreshViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
