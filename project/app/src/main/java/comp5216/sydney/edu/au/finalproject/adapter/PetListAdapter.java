package comp5216.sydney.edu.au.finalproject.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import comp5216.sydney.edu.au.finalproject.PetListActivity;
import comp5216.sydney.edu.au.finalproject.R;
import comp5216.sydney.edu.au.finalproject.pojo.Pet;
import comp5216.sydney.edu.au.finalproject.pojo.Post;

public class PetListAdapter extends RecyclerView.Adapter<PetListAdapter.ViewHolder> {

    private List<Pet> pets;
    LayoutInflater inflater = null;
    private ItemClickListener itemClickListener;
    public PetListAdapter(PetListActivity petListActivity, List<Pet> petList, ItemClickListener itemClickListener) {
        this.pets = petList;
        this.itemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView pet_photo;
        private TextView pet_name;
        private TextView pet_type;
        private TextView pet_desc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pet_photo = itemView.findViewById(R.id.pet_photo);
            pet_name = itemView.findViewById(R.id.pet_name);
            pet_type = itemView.findViewById(R.id.pet_type);
            pet_desc = itemView.findViewById(R.id.pet_desc);
        }
    }
    @NonNull
    @Override
    public PetListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        View view = inflater.inflate(R.layout.item_pet, parent, false);
        return new PetListAdapter.ViewHolder(view);
    }
    public interface ItemClickListener{
        void onItemClick(Pet pet, int position);
        void onItemLongClick(Pet pet, int position);
    }


    @Override
    public void onBindViewHolder(@NonNull PetListAdapter.ViewHolder holder, int position) {
        if(pets.get(position).getPetImageAddress() != null){
            Uri avatar = Uri.parse(pets.get(position).getPetImageAddress());
            holder.pet_photo.setImageURI(avatar);
        }
        holder.pet_name.setText(pets.get(position).getPetName());
        holder.pet_type.setText(pets.get(position).getCategory());
        holder.pet_desc.setText(pets.get(position).getPetDescription());
        holder.itemView.setOnClickListener(view -> {
            itemClickListener.onItemClick(pets.get(position), position);
        });
        holder.itemView.setOnLongClickListener(view -> {
            itemClickListener.onItemLongClick(pets.get(position), position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }


}
