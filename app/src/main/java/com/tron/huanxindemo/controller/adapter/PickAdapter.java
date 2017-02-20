package com.tron.huanxindemo.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tron.huanxindemo.R;
import com.tron.huanxindemo.model.bean.PickInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：Tronzzb on 2017/2/20 15:54.
 * 邮箱：278042465@qq.com
 */

public class PickAdapter extends BaseAdapter {

    private Context context;

    // 数据集合
    private List<PickInfo> pickInfos;

    public PickAdapter(Context context) {
        this.context = context;
        pickInfos = new ArrayList<>();
    }

    // 刷新适配器
    public void refresh(List<PickInfo> pickInfos) {

        if (pickInfos == null) {
            return;
        }

        this.pickInfos.clear();
        this.pickInfos.addAll(pickInfos);

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pickInfos == null ? 0 : pickInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return pickInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_pick_contact_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PickInfo pickInfo = pickInfos.get(position);

        viewHolder.cbItemPickContacts.setChecked(pickInfo.isChecked());

        viewHolder.tvItemPickContactsName.setText(pickInfo.getUserInfo().getUsername());

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.cb_item_pick_contacts)
        CheckBox cbItemPickContacts;
        @BindView(R.id.tv_item_pick_contacts_name)
        TextView tvItemPickContactsName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}