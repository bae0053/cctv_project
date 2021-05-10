package com.example.cctv_tmap.Bookmark;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BookmarkViewModel extends AndroidViewModel {

    private LiveData<List<Bookmark>> bookmarkList;
    private BookmarkDatabase bookmarkDatabase;

    public BookmarkViewModel(@NonNull Application application) {
        super(application);
        bookmarkDatabase = BookmarkDatabase.getINSTANCE(getApplication());
        bookmarkList = bookmarkDatabase.bookmarkDao().getAll();
    }

    public LiveData<List<Bookmark>> getBookmarkList() {
        return bookmarkList;
    }
}
