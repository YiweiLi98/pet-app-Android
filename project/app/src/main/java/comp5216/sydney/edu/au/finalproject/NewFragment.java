package comp5216.sydney.edu.au.finalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.blankj.utilcode.util.ActivityUtils;

public class NewFragment extends Fragment implements View.OnClickListener{


    ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 1) {
                    String updatedResult = (String) result.getData().getSerializableExtra("item");
                    Log.d("MAIN ACTIVITY", updatedResult);
                }
            }
    );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new, container, false);

        Button post = (Button) view.findViewById(R.id.btnNewPost);
        post.setOnClickListener(this);
        Button pet = (Button) view.findViewById(R.id.btnNewPet);
        pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("PageActivity", "Go to Add Pet page");
                ActivityUtils.startActivity(NewPetActivity.class);

            }
        });

        return inflater.inflate(R.layout.fragment_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button post = (Button) view.findViewById(R.id.btnNewPost);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("PageActivity", "Go to Add Post page");
                Intent intent = new Intent(getActivity(), NewPostActivity.class);
                mLauncher.launch(intent);
//                ActivityUtils.startActivity(NewPostActivity.class);

            }
        });

        Button pet = (Button) view.findViewById(R.id.btnNewPet);
        pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("PageActivity", "Go to Add Pet page");
                Intent intent = new Intent(getActivity(), NewPetActivity.class);
                mLauncher.launch(intent);
//                ActivityUtils.startActivity(NewPetActivity.class);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNewPost:
                Log.i("PageActivity", "Go to Add Post page");
                ActivityUtils.startActivity(NewPostActivity.class);

                break;
        }
    }

    public void onPostClick(View view) {
        Log.i("PageActivity", "Go to Add Post page");
        ActivityUtils.startActivity(NewPostActivity.class);
    }

}