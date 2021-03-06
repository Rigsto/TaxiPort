package com.aurigaaristo.taxiportfinal.loginfragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aurigaaristo.taxiportfinal.MainActivity;
import com.aurigaaristo.taxiportfinal.R;
import com.aurigaaristo.taxiportfinal.loader.LoginLoader;
import com.aurigaaristo.taxiportfinal.preference.Pref;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Integer> {
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private ProgressBar pbLogin, pbAutoLogin;
    private ConstraintLayout clLogin;

    private Pref pref;
    private boolean send = false;

    public LoginFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtEmail = view.findViewById(R.id.edt_login_email);
        edtPassword = view.findViewById(R.id.edt_login_password);
        btnLogin = view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        TextView tvForgot = view.findViewById(R.id.tv_forgotPassword);
        tvForgot.setOnClickListener(this);
        pbLogin = view.findViewById(R.id.pb_login);

        clLogin = view.findViewById(R.id.cl_form);
        pbAutoLogin = view.findViewById(R.id.pb_auto_login);

        clLogin.setVisibility(View.GONE);
        pbAutoLogin.setVisibility(View.GONE);

        pref = new Pref(getActivity());

        loginChecker();
    }

    private void loginChecker(){
        String email = pref.getEmailPreference();
        String pass = pref.getPasswordPreference();

        if (email.equals("") || pass.equals("")){
            clLogin.setVisibility(View.VISIBLE);
        } else {
            pbAutoLogin.setVisibility(View.VISIBLE);
            login(email, pass, 1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                if (checkAns()){
                    btnLogin.setVisibility(View.INVISIBLE);
                    pbLogin.setVisibility(View.VISIBLE);

                    String email = edtEmail.getText().toString();
                    String password = edtPassword.getText().toString();

                    login(email, password, 0);
                }
                break;
            case R.id.tv_forgotPassword:
                FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_login, new ForgotPasswordFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }

    private boolean checkAns(){
        if (edtEmail.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), R.string.req_email, Toast.LENGTH_SHORT).show();
            return false;
        } else if (edtPassword.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), R.string.req_pass, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void login(String email, String password, int logincode) {
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("password", password);
        bundle.putInt("login", logincode);

        if (send) {
            getLoaderManager().restartLoader(0, bundle, this);
        } else {
            getLoaderManager().initLoader(0, bundle, this);
            send = true;
        }
    }

    @NonNull
    @Override
    public Loader<Integer> onCreateLoader(int i, Bundle bundle) {
        String email = "", password = "";
        int code = 0;
        if (bundle != null){
            email = bundle.getString("email");
            password = bundle.getString("password");
            code = bundle.getInt("login");
        }

        return new LoginLoader(getActivity(), email, password, code);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Integer> loader, Integer code) {
        if (code == 0){
            Toast.makeText(getActivity(), getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
            pref.resetPreference();
            relogin();
        } else if (code == 2){
            Toast.makeText(getActivity(), getString(R.string.wrong_login), Toast.LENGTH_SHORT).show();
            pref.resetPreference();
            relogin();
        } else if (code == 1){
            Toast.makeText(getActivity(), getString(R.string.login_succesful), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        }
    }

    private void relogin(){
        pbAutoLogin.setVisibility(View.GONE);
        clLogin.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.VISIBLE);
        pbLogin.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Integer> loader) {

    }
}
