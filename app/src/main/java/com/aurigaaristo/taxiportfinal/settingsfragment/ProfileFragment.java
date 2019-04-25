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
import com.aurigaaristo.taxiportfinal.loader.ProfileLoader;
import com.aurigaaristo.taxiportfinal.preference.Pref;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Integer> {
    private EditText edtName, edtPhone;
    private Button btnSave;
    private ProgressBar pbSave;

    private Pref pref;

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtName = view.findViewById(R.id.edt_settings_name);
        edtPhone = view.findViewById(R.id.edt_settings_phone);
        btnSave = view.findViewById(R.id.btn_save_profile);
        btnSave.setOnClickListener(this);
        pbSave = view.findViewById(R.id.pb_save_profile);

        pbSave.setVisibility(View.INVISIBLE);

        if (getActivity().getActionBar() != null){
            getActivity().getActionBar().setTitle(getString(R.string.profil));
        }

        pref = new Pref(getActivity());

        edtName.setText(pref.getNamePreference());
        edtPhone.setText(pref.getPhonePreference());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save_profile){
            if (checkAns()){
                btnSave.setVisibility(View.INVISIBLE);
                pbSave.setVisibility(View.VISIBLE);

                String name = edtName.getText().toString();
                String phone = edtPhone.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("phone", phone);

                getLoaderManager().initLoader(0, bundle, this);
            }
        }
    }

    @Override
    public Loader<Integer> onCreateLoader(int i, @Nullable Bundle bundle) {
        String name = "", phone = "";
        if (bundle != null){
            name = bundle.getString("name");
            phone = bundle.getString("phone");
        }

        return new ProfileLoader(getActivity(), name, phone);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Integer> loader, Integer code) {
        switch (code){
            case 0:
                Toast.makeText(getActivity(), getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
                reEntry();
                break;
            case 1:
                Toast.makeText(getActivity(), getString(R.string.profile_updated), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("x", "settings");
                startActivity(intent);
                getActivity().finish();
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

    private boolean checkAns(){
        if (edtName.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), getString(R.string.req_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtPhone.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), getString(R.string.req_phone), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
