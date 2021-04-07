package com.example.sysinfo.activities;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;

public class AboutActivity extends MaterialAboutActivity {

    @NonNull
    @Override
    protected MaterialAboutList getMaterialAboutList(@NonNull Context context) {
        MaterialAboutCard card = new MaterialAboutCard.Builder()
                .title("Author")
                .build();

        //TODO Add cards from the librart @link https://github.com/daniel-stoneuk/material-about-library

        return new MaterialAboutList.Builder()
                .addCard(card)
                .build();
    }

    @Nullable
    @Override
    protected CharSequence getActivityTitle() {
        return null;
    }


}