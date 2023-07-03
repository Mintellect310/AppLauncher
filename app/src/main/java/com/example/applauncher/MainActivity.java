package com.example.applauncher;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.HashMap;
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
            "com.amazon.mShop.android.shopping",
            "com.kms.free",
            "com.sec.android.app.popupcalculator",
            "com.sec.android.app.clockpackage",
            "com.google.android.apps.docs",
            "com.digilocker.android",
            "com.sec.android.app.myfiles",
            "com.google.android.gm",
            "com.android.vending"
    };

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

                // Use the appName as needed
                appNames[i] = appName;
            } catch (PackageManager.NameNotFoundException e) {
                appNames[i] = packageName;
                e.printStackTrace();
            }
        }

        ListView appListView = findViewById(R.id.appListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, appNames);

        appListView.setAdapter(adapter);
        appListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchApp(APPS[position]);
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
