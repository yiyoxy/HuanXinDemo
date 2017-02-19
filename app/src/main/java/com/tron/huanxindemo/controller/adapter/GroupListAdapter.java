package com.tron.huanxindemo.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMGroup;
import com.tron.huanxindemo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：Tronzzb on 2017/2/19 14:21.
 * 邮箱：278042465@qq.com
 */

public class GroupListAdapter extends BaseAdapter {

    private Context context;

    private List<EMGroup> groups;

    public GroupListAdapter(Context context) {
        this.context = context;
        groups = new ArrayList<>();
    }

    public void refresh(List<EMGroup> groups) {

        if (groups == null) {
            return;
        }

        this.groups.clear();
        this.groups.addAll(groups);

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return groups == null ? 0 : groups.size();
    }

    @Override
    public Object getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null){
            convertView = View.inflate(context, R.layout.adapter_group_list, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        EMGroup emGroup = groups.get(position);

        viewHolder.groupTvGroupname.setText(emGroup.getGroupName());

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.group_tv_groupname)
        TextView groupTvGroupname;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
