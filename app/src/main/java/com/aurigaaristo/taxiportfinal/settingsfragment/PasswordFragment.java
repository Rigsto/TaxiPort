package com.aurigaaristo.taxiportfinal.settingsfragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aurigaaristo.taxiportfinal.MainActivity;
import com.aurigaaristo.taxiportfinal.R;
import com.aurigaaristo.taxiportfinal.loader.ChangePasswordLoader;
import com.aurigaaristo.taxiportfinal.preference.Pref;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Integer> {
    private EditText edtOld, edtNew, edtNew2;
    private ProgressBar pbSave;
    private Button btnSave;
    private Pref pref;

    private boolean send = false;

    public PasswordFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtOld = view.findViewById(R.id.edt_settings_old_pass);
        edtNew = view.findViewById(R.id.edt_settings_new_pass);
        edtNew2 = view.findViewById(R.id.edt_settings_retype_pass);
        btnSave = view.findViewById(R.id.btn_save_password);
        btnSave.setOnClickListener(this);
        pbSave = view.findViewById(R.id.pb_save_password);
        pbSave.setVisibility(View.INVISIBLE);
        pref = new Pref(getActivity());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save_password){
            if (checkAns()){
                btnSave.setVisibility(View.INVISIBLE);
                pbSave.setVisibility(View.VISIBLE);

                String pass = edtNew.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("password", pass);

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
        if (edtOld.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), getString(R.string.req_old_pass), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtNew.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), getString(R.string.req_new_pass), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtNew2.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), getString(R.string.req_new_pass), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!edtOld.getText().toString().equals(pref.getPasswordPreference())){
            Toast.makeText(getActivity(), getString(R.string.inv_old_pass), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!edtNew.getText().toString().equals(edtNew2.getText().toString())){
            Toast.makeText(getActivity(), getString(R.string.pass_mis), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtOld.getText().toString().equals(edtNew.getText().toString())){
            Toast.makeText(getActivity(), getString(R.string.pass_must_diff), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @NonNull
    @Override
    public Loader<Integer> onCreateLoader(int i, @Nullable Bundle bundle) {
        String pass = "";
        if (bundle != null){
            pass = bundle.getString("password");
        }

        return new ChangePasswordLoader(getActivity(), pass);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Integer> loader, Integer code) {
        switch (code){
            case 0:
                Toast.makeText(getActivity(), getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
                reEntry();
                break;
            case 1:
                Toast.makeText(getActivity(), getString(R.string.password_changed), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("x", "settings");
                startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
                break;
        }
    }

    private void reEntry(){
        btnSave.setVisibility(View.VISIBLE);
        pbSave.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Integer> loader) {

    }
}
