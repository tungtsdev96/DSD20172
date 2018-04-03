package com.hust.edu.dsd.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.hust.edu.dsd.BuildConfig;
import com.hust.edu.dsd.R;
import com.hust.edu.dsd.api.ApiFactory;
import com.hust.edu.dsd.api.BaseCallBack;
import com.hust.edu.dsd.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static android.app.Activity.RESULT_OK;

/**
 * Created by tungts on 3/14/2018.
 */

public class DialogStateTree extends DialogFragment implements View.OnClickListener {

    EditText edt_id_tree;
    EditText edt_des;
    ImageView img_tree;
    Button btn_send;
    ImageView btn_close;
    View root;

    Spinner spinner_state_tree; String state;
    ArrayList<String> list_state;
    ArrayAdapter<String> state_adapter;

    public static DialogStateTree newInstance(){
        DialogStateTree dialogStateTree = new DialogStateTree();
        return dialogStateTree;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog.setCanceledOnTouchOutside(false);
        setStyle(STYLE_NO_FRAME, R.style.AppTheme_NoActionBar);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setDimAmount(0.0f);
        dialog.getWindow().getAttributes().windowAnimations = R.anim.fade_in;

        setCancelable(false);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_report_state_tree, container);    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        root = view;
        addControls(view);
        addEvents();
    }

    private void addEvents() {
        btn_close.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        img_tree.setOnClickListener(this);
        spinner_state_tree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               state = (i == 0) ? "Héo": "Chết";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                state = "Héo";
            }
        });
    }

    private void addControls(View view) {
        edt_id_tree = view.findViewById(R.id.edt_id_tree);
        spinner_state_tree = view.findViewById(R.id.spinner_state_tree);
        list_state = new ArrayList<>();
        list_state.add("Héo");
        list_state.add("Chết");
        state_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list_state);
        spinner_state_tree.setAdapter(state_adapter);
        edt_des = view.findViewById(R.id.edt_des);
        img_tree = view.findViewById(R.id.img_tree);
        btn_send = view.findViewById(R.id.btn_send);
        btn_close = view.findViewById(R.id.btn_close);
        progressDialog = new ProgressDialog(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        final Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_close:
                dismissDialog();
                break;
            case R.id.btn_send:
                send();
                break;
            case R.id.img_tree:
                showCamera();
                break;
        }
    }

    boolean isCamera;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100){
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        isCamera = false;
                        return;
                    }
                }
                isCamera = true;
            }
        }
    }

    private final int REQUEST_IMAGE_CAPTURE = 99;
    File fileImage;
    private void showCamera() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            isCamera = true;
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            fileImage = getOutputMediaFile();
            Uri photoURI = FileProvider.getUriForFile(getActivity(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    fileImage);
            cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            //Request camera permission
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);
        }
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "DSD");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Uri imageUri = Uri.parse("file:" + fileImage.getAbsolutePath());
            File file = new File(imageUri.getPath());
            try {
                InputStream ims = new FileInputStream(file);
                img_tree.setImageBitmap(BitmapFactory.decodeStream(ims));
            } catch (FileNotFoundException e) {
                return;
            }
        }
    }

    ProgressDialog progressDialog;
    private void send() {
        //check ki tu trong
        if (edt_id_tree.getText() == null || edt_des.getText() == null || state == null || fileImage == null){
            Toast.makeText(getActivity(), "Điền đầy đủ thông tin đã", Toast.LENGTH_SHORT).show();
            return;
        }
        String tree_id = edt_id_tree.getText().toString();
        String tree_state = state;
        String tree_description = edt_des.getText().toString();

        progressDialog.setMessage("Đang xử lý");
        progressDialog.show();
        final RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),fileImage);
        MultipartBody.Part part = MultipartBody.Part.createFormData("uploaded_file", fileImage.getName(), requestBody);
        ApiFactory.getApiHust().updateTreeState(part,tree_id, tree_state, tree_description).enqueue(new BaseCallBack<ResponseBody>(getActivity()) {
            @Override
            public void onSuccess(ResponseBody result) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(result.string());
                    if (object.getString("result").equals("YES")){
                        Toast.makeText(getActivity(), "Gửi thành công", Toast.LENGTH_SHORT).show();
                        dismissDialog();
                    } else {
                        Toast.makeText(getActivity(), "Đã có lỗi xảy ra hãy gửi lại.", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void dismissDialog() {
        Animation fadeout = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),android.R.anim.fade_out);
        fadeout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        root.startAnimation(fadeout);
    }
}
