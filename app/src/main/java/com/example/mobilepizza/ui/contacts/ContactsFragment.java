package com.example.mobilepizza.ui.contacts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.mobilepizza.R;

public class ContactsFragment extends Fragment {
    WebView webView;
    WebSettings webSettings;
    Button button;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        webView = view.findViewById(R.id.map_view);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://yandex.ru/maps/?um=constructor%3A70396694c6ae8db683b4c48f7160cd7fe1f9f3d63dd8b60c90892980dfc25175&source=constructorLink");

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        button = view.findViewById(R.id.contact_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_SENDTO)
                        .setData(Uri.parse("mailto:mobile.pizza2022@gmail.com"))
                );
            }
        });

        return view;
    }
}