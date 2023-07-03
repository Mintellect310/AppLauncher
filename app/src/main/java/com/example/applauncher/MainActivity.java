package com.example.applauncher;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private static String[] APPS = {
            "com.brave.browser",
            "com.android.chrome",
            "com.dropbox.android",
            "com.spotify.music",
            "com.truecaller",
            "it.vfsfitvnm.vimusic",
            "com.google.android.youtube",
            "com.samsung.android.app.notes",
            "com.samsung.android.spay",
            "com.google.android.apps.nbu.paisa.user",
            "com.ubercab",
            "com.rapido.passenger",
            "com.adobe.scan.android",
            "com.kms.free",
            "com.sec.android.app.popupcalculator",
            "com.sec.android.app.clockpackage",
            "com.google.android.apps.docs",
            "com.digilocker.android",
            "com.sec.android.app.myfiles",
            "com.google.android.gm",
            "com.android.vending"
    };

    private ListView appListView;
    private List<ApplicationInfo> appInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PackageManager packageManager = getPackageManager();
        String[] appNames = new String[APPS.length];
        for(int i = 0; i < APPS.length; i++) {
            String packageName = APPS[i];
            try {
                ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
                String appName = packageManager.getApplicationLabel(appInfo).toString();
                Drawable appIcon = packageManager.getApplicationIcon(appInfo);

                // Use the appName as needed
                appNames[i] = appName;
            } catch (PackageManager.NameNotFoundException e) {
                appNames[i] = packageName;
                e.printStackTrace();
            }
        }

        appListView = findViewById(R.id.appListView);
        appInfoList = new ArrayList<>();
        for (String packageName : APPS) {
            try {
                ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
                appInfoList.add(appInfo);
            } catch (PackageManager.NameNotFoundException e) {
                // Package not found, handle as needed
                Toast.makeText(getApplicationContext(), packageName, Toast.LENGTH_SHORT).show();
            }
        }
        ArrayAdapter<ApplicationInfo> adapter = new ArrayAdapter<ApplicationInfo>(this, R.layout.list_item_app, R.id.appNameTextView, appInfoList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ImageView appIconImageView = view.findViewById(R.id.appIconImageView);
                TextView appNameTextView = view.findViewById(R.id.appNameTextView);

                ApplicationInfo appInfo = appInfoList.get(position);
                Drawable appIcon = packageManager.getApplicationIcon(appInfo);
                String appName = packageManager.getApplicationLabel(appInfo).toString();

                appIconImageView.setImageDrawable(appIcon);
                appNameTextView.setText(appName);

                return view;
            }
        };


        appListView.setAdapter(adapter);
        appListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String packageName = appInfoList.get(position).packageName;
                launchApp(packageName);
            }
        });
    }

    private void launchApp(String appName) {
        PackageManager packageManager = getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(appName);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            // App not found, handle the error
            Uri playStoreUri = Uri.parse("market://details?id=" + appName);
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, playStoreUri);
            startActivity(marketIntent);
        }
    }
}
