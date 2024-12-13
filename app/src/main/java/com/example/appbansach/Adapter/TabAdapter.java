package com.example.appbansach.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.appbansach.fragment.DescriptionFragment;
import com.example.appbansach.fragment.ReviewsFragment;

public class TabAdapter extends FragmentStatePagerAdapter {
    private final Integer bookId;
    public TabAdapter(@NonNull FragmentManager fm, int behavior, Integer bookId) {
        super(fm, behavior);
        this.bookId = bookId;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return DescriptionFragment.newInstance(bookId);
            case 1:
                return ReviewsFragment.newInstance(bookId);
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Mô tả";
            case 1:
                return "Đánh giá";
            default:
                return null;
        }
    }
}
