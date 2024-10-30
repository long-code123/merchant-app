package com.app.foodmerchantapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.foodmerchantapp.Fragment.HomeFragment;
import com.app.foodmerchantapp.Fragment.IncomeFragment;
import com.app.foodmerchantapp.Fragment.OrderFragment;
import com.app.foodmerchantapp.Fragment.ProfileFragment;
import com.app.foodmerchantapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Fragment currentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Ban đầu thay thế bằng HomeFragment
        currentFragment = new HomeFragment();
        replaceFragment(currentFragment);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    if (!(currentFragment instanceof HomeFragment)) {
                        currentFragment = new HomeFragment();
                        replaceFragment(currentFragment);
                    }
                    break;
                case R.id.order:
                    if (!(currentFragment instanceof OrderFragment)) {
                        currentFragment = new OrderFragment();
                        replaceFragment(currentFragment);
                    }
                    break;
                case R.id.income:
                    if (!(currentFragment instanceof IncomeFragment)) {
                        currentFragment = new IncomeFragment();
                        replaceFragment(currentFragment);
                    }
                    break;
                case R.id.profile:
                    if (!(currentFragment instanceof ProfileFragment)) {
                        currentFragment = new ProfileFragment();
                        replaceFragment(currentFragment);
                    }
                    break;
            }
            return true;
        });
    }

    // Phương thức để thay thế fragment
    private void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment);
            fragmentTransaction.addToBackStack(null); // Thêm Fragment vào back stack để hỗ trợ back navigation
            fragmentTransaction.commitAllowingStateLoss(); // Thực hiện FragmentTransaction an toàn
        }
    }

}