package com.aurigaaristo.taxiportfinal.loginfragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.aurigaaristo.taxiportfinal.R;
import com.aurigaaristo.taxiportfinal.loader.ForgotPasswordLoader;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Integer>{
    private EditText edtEmail, edtPassword, edtPassword2;
    private Button btnForgot;
    private ProgressBar pbForgot;

    private boolean send = false;

    public ForgotPasswordFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtEmail = view.findViewById(R.id.edt_forgot_email);
        edtPassword = view.findViewById(R.id.edt_forgot_password);
        edtPassword2 = view.findViewById(R.id.edt_forgot_password_2);
        btnForgot = view.findViewById(R.id.btn_change_password);
        btnForgot.setOnClickListener(this);
        pbForgot = view.findViewById(R.id.pb_change_password);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_change_password){
            if (checkAns()){
                btnForgot.setVisibility(View.INVISIBLE);
                pbForgot.setVisibility(View.VISIBLE);

                Bundle bundle = new Bundle();
                bundle.putString("email", edtEmail.getText().toString());
                bundle.putString("password", edtPassword.getText().toString());

                if (send) {
                    getLoaderManager().restartLoader(0, bundle, this);
                } else {
                    getLoaderManager().initLoader(0, bundle, this);
                    send = true;
                }
            }
        }
    }

    private boolean checkAns(){
        if (edtEmail.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), R.string.req_email, Toast.LENGTH_SHORT).show();
            return false;
        } else if (edtPassword.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), R.string.req_pass, Toast.LENGTH_SHORT).show();
            return false;
        } else if (edtPassword2.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), R.string.req_pass, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!edtPassword.getText().toString().equals(edtPassword2.getText().toString())){
            Toast.makeText(getActivity(), getString(R.string.pass_doesnt_match), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    @Override
    public Loader<Integer> onCreateLoader(int i, @Nullable Bundle bundle) {
        String email = bundle.getString("email");
        String password = bundle.getString("password");

        return new ForgotPasswordLoader(getActivity(), email, password);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Integer> loader, Integer code) {
        switch (code){
            case 0:
                Toast.makeText(getActivity(), getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
                reEntry();
                break;
            case 2:
                Toast.makeText(getActivity(), getString(R.string.email_not_found), Toast.LENGTH_SHORT).show();
                reEntry();
                break;
            case 1:
                Toast.makeText(getActivity(), getString(R.string.password_changed), Toast.LENGTH_SHORT).show();

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_login, new LoginFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }

    private void reEntry(){
        btnForgot.setVisibility(View.VISIBLE);
        pbForgot.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Integer> loader) {

    }
}
