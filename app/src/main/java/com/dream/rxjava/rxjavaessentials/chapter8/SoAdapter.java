package com.dream.rxjava.rxjavaessentials.chapter8;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dream.rxjava.R;
import com.dream.rxjava.rxjavaessentials.chapter8.api.github.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/25 下午12:50
 * Description: RxJavaDemo
 */
public class SoAdapter extends RecyclerView.Adapter<SoAdapter.ViewHolder> {

    private List<User> mUsers = new ArrayList<>();

    public SoAdapter(List<User> users) {
        mUsers = users;
    }

    public void updateUsers(List<User> users) {
        mUsers.clear();
        mUsers.addAll(users);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.so_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < mUsers.size()) {
            holder.setUser(mUsers.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mUsers == null ? 0 : mUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final View mView;

        @Bind(R.id.name)
        TextView name;

        @Bind(R.id.city)
        TextView city;

        @Bind(R.id.reputation)
        TextView reputation;

        @Bind(R.id.user_image)
        ImageView user_image;

        @Bind(R.id.city_image)
        ImageView city_image;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }

        public void setUser(User user) {
            name.setText(user.getDisplayName());
            city.setText(user.getLocation());
            reputation.setText(String.valueOf(user.getReputation()));
            ImageLoader.getInstance().displayImage(user.getProfileImage(), user_image);


        }
    }
}
