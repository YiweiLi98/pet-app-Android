package comp5216.sydney.edu.au.finalproject;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import comp5216.sydney.edu.au.finalproject.Utils.FileUtils;
import comp5216.sydney.edu.au.finalproject.databinding.ActivityModifyMeBinding;
import comp5216.sydney.edu.au.finalproject.pojo.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ModifyMeActivity extends AppCompatActivity {
    private static final String TAG = "ModifyMeActivity";
    private static final String KEY_USER = "key_user";
    private static final int MY_PERMISSIONS_REQUEST_READ_PHOTO_TO_POST = 101;
    private ActivityModifyMeBinding binding;
    private final MarshmallowPermission marshmallowPermission = new MarshmallowPermission(this);
    private File mSelectImageFile = null;

    public static void launch(Context context, User user) {
        Intent intent = new Intent(context, ModifyMeActivity.class);
        intent.putExtra(KEY_USER, user);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModifyMeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        PermissionUtils.permission(
                Manifest.permission.READ_EXTERNAL_STORAGE
        ).request();
        binding.btnBack.setOnClickListener(view -> ModifyMeActivity.this.finish());
        User user = (User) getIntent().getSerializableExtra(KEY_USER);
        LogUtils.i("user = " + user);
        binding.etNickName.setText(user.getNickName());
        binding.etDescription.setText(user.getDescription());
        Glide.with(binding.ivAvatar).load(user.getUserImageAddress()).into(binding.ivAvatar);
        binding.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!marshmallowPermission.checkPermissionForReadfiles()) {
                    marshmallowPermission.requestPermissionForReadfiles();
                } else {
                    // Create intent for picking a photo from the gallery
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, MY_PERMISSIONS_REQUEST_READ_PHOTO_TO_POST);
                }
            }
        });
        binding.btnModify.setOnClickListener(view -> {
            String nickName = binding.etNickName.getText().toString();
            if (StringUtils.isEmpty(nickName)) {
                ToastUtils.showShort("please input name!");
                return;
            }
            String description = binding.etDescription.getText().toString();
            if (StringUtils.isEmpty(description)) {
                ToastUtils.showShort("please input description!");
                return;
            }
            updateUserInfo(nickName, description);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHOTO_TO_POST) {
            if (resultCode == RESULT_OK) {
                Uri photoUri = data.getData();

                // Do something with the photo based on Uri
                Bitmap selectedImage;
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(), photoUri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                   // mSelectImageFile = getFileByUri(photoUri);
                    mSelectImageFile = FileUtils.getFileByUri(photoUri,this);
                    Glide.with(binding.ivAvatar).load(selectedImage).into(binding.ivAvatar);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateUserInfo(String nickName, String description) {
        String url = "http://35.189.24.208:8080/api/android/edit";
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (mSelectImageFile != null) {
            builder.addFormDataPart("avatar", mSelectImageFile.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), mSelectImageFile));//文件名,请求体里的文件
        }
        builder.addFormDataPart("nickName", nickName);//传递键值对参数
        builder.addFormDataPart("description", description);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("cookie", ToolManager.getINSTANCE().getSessionId())
                .post(builder.build())
                .build();
        ToolManager.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showShort("modify failed");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ToastUtils.showShort("modify success");
            }
        });
    }
}