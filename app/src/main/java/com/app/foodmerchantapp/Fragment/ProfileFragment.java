package com.app.foodmerchantapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.foodmerchantapp.API.LoginAPI;
import com.app.foodmerchantapp.Activity.LoginActivity;
import com.app.foodmerchantapp.Model.Store;
import com.app.foodmerchantapp.Model.User;
import com.app.foodmerchantapp.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    ImageView imageProfile;  // Hình ảnh của cửa hàng
    TextView tvNameStore, tvAddressStore;  // Tên và địa chỉ cửa hàng
    TextView tvGmailOwner, tvNameOwner, tvAddressOwner, tvPhone;  // Thông tin của chủ cửa hàng
    Button btnLogout;
    private LoginAPI apiService;

    public ProfileFragment() {
        // Constructor trống bắt buộc
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout cho fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageProfile = view.findViewById(R.id.imageProfile);
        tvNameStore = view.findViewById(R.id.tvNameStore);
        tvAddressStore = view.findViewById(R.id.tvAddressStore);
        tvGmailOwner = view.findViewById(R.id.tvGmailOwner);
        tvNameOwner = view.findViewById(R.id.tvNameOwner);
        tvAddressOwner = view.findViewById(R.id.tvAddressOwner);
        tvPhone = view.findViewById(R.id.tvPhone);
        btnLogout = view.findViewById(R.id.btnLogout);
        apiService = LoginAPI.loginAPI;

        // Thiết lập sự kiện cho nút đăng xuất
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });

        // Lấy thông tin người dùng và cửa hàng từ SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MerchantAppPrefs", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("user_info", null);
        String storeJson = sharedPreferences.getString("store_info", null);

        if (userJson != null && storeJson != null) {
            Gson gson = new Gson();
            User user = gson.fromJson(userJson, User.class);
            Store store = gson.fromJson(storeJson, Store.class);

            // Hiển thị thông tin lên UI
            Picasso.get().load(store.getStoreImage()).into(imageProfile);  // Hình ảnh cửa hàng
            tvNameStore.setText(store.getStoreName());
            tvAddressStore.setText(store.getAddress());

            tvGmailOwner.setText(user.getEmail());
            tvNameOwner.setText(user.getUserName());

        } else {
            Toast.makeText(getActivity(), "Không thể tải thông tin người dùng hoặc cửa hàng", Toast.LENGTH_SHORT).show();
        }
    }

    private void logoutUser() {
        // Xóa token đã lưu
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MerchantAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("jwt_token");
        editor.apply();

        // Chuyển người dùng về màn hình đăng nhập
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}