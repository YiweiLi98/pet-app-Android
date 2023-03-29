package comp5216.sydney.edu.au.finalproject.adapter;



import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import comp5216.sydney.edu.au.finalproject.DetailActivity;
import comp5216.sydney.edu.au.finalproject.R;
import comp5216.sydney.edu.au.finalproject.pojo.Comment;

/**
 * Created by Leon on 2022/10/11
 */
public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    LayoutInflater inflater = null;
    private List<Comment> commentList;
    public CommentListAdapter(DetailActivity detailActivity, List<Comment> comments) {
        commentList = comments;
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView user_photo;
        private TextView username;
        private TextView comment;
        private TextView comment_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user_photo = itemView.findViewById(R.id.comment_user_photo);
            username = itemView.findViewById(R.id.comment_username);
            comment = itemView.findViewById(R.id.comment_content);
            comment_time = itemView.findViewById(R.id.comment_time);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        View view = inflater.inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri uri = Uri.parse(commentList.get(position).getUserAvatar());
        holder.user_photo.setImageURI(uri);
//        Picasso.get().load(commentList.get(position).getUserAvatar()).into(holder.user_photo);
        holder.username.setText(commentList.get(position).getNickName());
        holder.comment.setText(commentList.get(position).getCommentContent());
        String res = stampToDate(commentList.get(position).getCommentTime());
        holder.comment_time.setText(res);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static String stampToDate(Long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }
}
