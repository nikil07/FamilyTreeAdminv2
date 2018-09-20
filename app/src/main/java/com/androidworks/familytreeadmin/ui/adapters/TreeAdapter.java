package com.androidworks.familytreeadmin.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;


import com.androidworks.familytreeadmin.R;
import com.androidworks.familytreeadmin.data.model.Member;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.blox.treeview.BaseTreeAdapter;

public class TreeAdapter extends BaseTreeAdapter<TreeAdapter.ViewHolder> {

    public TreeAdapter(@NonNull Context context, int layoutRes) {
        super(context, layoutRes);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object data, int position) {
//        if (position == 0) {
//            viewHolder.name.setText(data.toString());
//            //viewHolder.nickName.setText("Thatha");
//            viewHolder.birthYear.setText("1932");
//            viewHolder.location.setText("Coimbatore");
//        } else {
            Member member = (Member) data;
            viewHolder.name.setText(member.getName());
            viewHolder.nickName.setText(member.getNickName());
            viewHolder.birthYear.setText(String.valueOf(member.getBirthYear()));
            viewHolder.location.setText(member.getLocation());
//        }
    }

    static class ViewHolder {

        @BindView(R.id.tv_member_name)
        TextView name;
        @BindView(R.id.tv_member_nick_name)
        TextView nickName;
        @BindView(R.id.tv_member_birth_year)
        TextView birthYear;
        @BindView(R.id.tv_member_location)
        TextView location;

        private ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}

