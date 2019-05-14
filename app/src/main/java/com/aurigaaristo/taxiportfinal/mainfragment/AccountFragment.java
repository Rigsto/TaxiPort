package com.aurigaaristo.taxiportfinal.mainfragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aurigaaristo.taxiportfinal.BuildConfig;
import com.aurigaaristo.taxiportfinal.LoginActivity;
import com.aurigaaristo.taxiportfinal.MainActivity;
import com.aurigaaristo.taxiportfinal.R;
import com.aurigaaristo.taxiportfinal.SettingsActivity;
import com.aurigaaristo.taxiportfinal.loader.LogoutLoader;
import com.aurigaaristo.taxiportfinal.preference.Pref;
import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Integer> {
    private ImageView imgProfile;
    private TextView tvName;
    private TextView tvEmail;
    private TextView tvPhone;
    private Button btnLogout;
    private ProgressBar pbLogout;

    private Intent intent;
    private Pref pref;

    public AccountFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgProfile = view.findViewById(R.id.img_profile);
        tvName = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);
        tvPhone = view.findViewById(R.id.tv_phone);
        TextView tvEdit = view.findViewById(R.id.tv_edit);

        RelativeLayout rlChangePass = view.findViewById(R.id.rl_change_pass);
        RelativeLayout rlChangeLang = view.findViewById(R.id.rl_change_lang);
        RelativeLayout rlAbout = view.findViewById(R.id.rl_about);
        btnLogout = view.findViewById(R.id.logout);
        pbLogout = view.findViewById(R.id.pb_logout);
        pbLogout.setVisibility(View.INVISIBLE);

        tvEdit.setOnClickListener(this);
        rlChangePass.setOnClickListener(this);
        rlChangeLang.setOnClickListener(this);
        rlAbout.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        pref = new Pref(getActivity());

        tvName.setText(pref.getNamePreference());
        tvEmail.setText(pref.getEmailPreference());
        tvPhone.setText(pref.getPhonePreference());

        String url = BuildConfig.PHP_LOADER;
        Picasso.with(getContext()).load(url + pref.getImagePreference()).into(imgProfile);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_edit:
                intent = new Intent(getActivity(), SettingsActivity.class);
                intent.putExtra("type", "profile");
                startActivity(intent);
                break;
            case R.id.rl_change_pass:
                intent = new Intent(getActivity(), SettingsActivity.class);
                intent.putExtra("type", "pass");
                startActivity(intent);
                break;
            case R.id.rl_change_lang:
                changeLanguage();
                break;
            case R.id.rl_about:
                intent = new Intent(getActivity(), SettingsActivity.class);
                intent.putExtra("type", "about");
                startActivity(intent);
                break;
            case R.id.logout:
                getLoaderManager().initLoader(0, null, this);
                break;
        }
    }

    private void changeLanguage(){
        int checked = pref.getLangPreference();
        String[] lang = new String[]{getResources().getString(R.string.english), getResources().getString(R.string.indonesia)};
        final int[] pil = new int[1];

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(getString(R.string.language));
        builder.setSingleChoiceItems(lang, checked, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pil[0] = which;
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.change), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setLocale(pil[0]);
                pref.setLangPreference(pil[0]);
                dialog.dismiss();
                refresh();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setLocale(int x){
        String[] lang = new String[]{"en", "in"};
        String language = lang[x];
        Locale locale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
    }

    private void refresh(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }


    @NonNull
    @Override
    public Loader<Integer> onCreateLoader(int i, @Nullable Bundle bundle) {
        btnLogout.setVisibility(View.INVISIBLE);
        pbLogout.setVisibility(View.VISIBLE);

        String email = pref.getEmailPreference();

        return new LogoutLoader(getActivity(), email);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Integer> loader, Integer code) {
        if (code == 0) {
            Toast.makeText(getActivity(), getString(R.string.logout_failed), Toast.LENGTH_SHORT).show();
            relogout();
        } else if (code == 2) {
            Toast.makeText(getActivity(), getString(R.string.wrong_login), Toast.LENGTH_SHORT).show();
            relogout();
        } else if (code == 1) {
            Toast.makeText(getActivity(), getString(R.string.logout_successful), Toast.LENGTH_SHORT).show();

            pref.resetPreference();
            intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Integer> loader) {

    }

    private void relogout() {
        btnLogout.setVisibility(View.VISIBLE);
        pbLogout.setVisibility(View.INVISIBLE);
    }
}
