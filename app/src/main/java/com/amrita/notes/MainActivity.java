package com.amrita.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.WindowManager;

import com.amrita.notes.authentication.login.LoginFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getWindow()).setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        addFragment(new LoginFragment(), "Login Fragment");
    }

    public void setFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.home_container, fragment);
            fragmentTransaction.detach(fragment);
            fragmentTransaction.attach(fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 2 ) {
            this.finish();
        } else {
            popFragment("Add Fragment");
        }
    }

    @SuppressLint("WrongConstant")
    public void addFragment(Fragment fragment, String title) {
        try {
            if(!fragment.isAdded()) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();//
                fragmentTransaction.replace(R.id.home_container, fragment);
                fragmentTransaction.detach(fragment);
                fragmentTransaction.attach(fragment);
                fragmentTransaction.addToBackStack(title);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
                fragmentTransaction.commit();

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void popFragment(String fragment_name){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(fragment_name,FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
