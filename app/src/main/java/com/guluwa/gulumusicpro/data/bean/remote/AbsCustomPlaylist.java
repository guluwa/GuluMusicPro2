package com.guluwa.gulumusicpro.data.bean.remote;

import android.content.Context;
import android.os.Parcel;
import androidx.annotation.NonNull;
import io.reactivex.Observable;

import java.util.ArrayList;

public abstract class AbsCustomPlaylist extends Playlist {
    public AbsCustomPlaylist(int id, String name) {
        super(id, name);
    }

    public AbsCustomPlaylist() {
    }

    public AbsCustomPlaylist(Parcel in) {
        super(in);
    }

    @NonNull
    public abstract Observable<ArrayList<Song>> getSongs(Context context);
}
