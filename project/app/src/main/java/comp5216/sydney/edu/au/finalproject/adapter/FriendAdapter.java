package comp5216.sydney.edu.au.finalproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import comp5216.sydney.edu.au.finalproject.R;
import comp5216.sydney.edu.au.finalproject.pojo.User;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.LinearViewHolder> {

    private LayoutInflater inflater = null;
    private List<User> users;
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    public FriendAdapter(List<User> userList, OnItemClickListener clistener, OnItemLongClickListener lclistener){
        this.users = userList;
        if (users == null) {
            users = new ArrayList<User>();
        }
        this.clickListener = clistener;
        this.longClickListener = lclistener;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private ImageView imageView;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            //find tv and iv
            textView = itemView.findViewById(R.id.friend_name);
            imageView = itemView.findViewById(R.id.friend_photo);
        }
    }

    @NonNull
    @Override
    public LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_display, parent, false);
        return new LinearViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendAdapter.LinearViewHolder holder, int position) {
        holder.textView.setText("user");          //display text
        holder.imageView.setImageResource(R.mipmap.icon_avatar); //display image

        User user = users.get(position);
        if (user.getNickName() == null){
            holder.textView.setText("nickname");
        } else {
            holder.textView.setText(user.getNickName());          //display text
        }
        Picasso.get().load(user.getUserImageAddress()).into(holder.imageView); //display image

        holder.itemView.setOnClickListener(new View.OnClickListener() {        // click event
            @Override
            public void onClick(View v) {
                System.out.println("click userId: "+user.getId()+" name: "+user.getNickName());
                clickListener.onClick(position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {  // long click
            @Override
            public boolean onLongClick(View v) {
                longClickListener.onLongClick(position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.users.size();                          //set item count
    }

    public interface OnItemClickListener {
        void onClick(int pos);
    }

    public interface OnItemLongClickListener {
        void onLongClick(int pos);
    }

}
