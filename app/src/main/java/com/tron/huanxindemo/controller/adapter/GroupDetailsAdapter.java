package com.tron.huanxindemo.controller.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.tron.huanxindemo.R;
import com.tron.huanxindemo.model.bean.UserInfo;
import com.tron.huanxindemo.utils.ShowToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：Tronzzb on 2017/2/21 16:19.
 * 邮箱：278042465@qq.com
 */

public class GroupDetailsAdapter extends BaseAdapter {

    // 上下文
    private Context mContext;

    // 是否可以添加和删除好友, 只有群主有此权限
    private boolean mIsCanModify;

    // 是否是删除模式: true:删除模式; false:非删除模式
    private boolean mIsDeleteMode = false;

    // 数据: 所有的群成员
    private List<UserInfo> mUserInfos;

    // 群主
    private String mOwner;

    public GroupDetailsAdapter(Context context, boolean isCanModify, String mOwner) {
        mContext = context;
        mIsCanModify = isCanModify;
        mUserInfos = new ArrayList<>();

        this.mOwner = mOwner;
    }


    // 刷新adapter
    public void refresh(List<UserInfo> userInfos) {

        // 判断是否为空指针
        if (userInfos == null) {
            return;
        }

        // 清除原有数据;  clear() -> 移除此列表中的所有元素。
        mUserInfos.clear();

        // 添加加减号
        initUser();

        // 添加群成员, ArrayList的方法addAll(int index, Collection<? extends E> c); -> 从指定的位置开始，将指定 collection 中的所有元素插入到此列表中
        mUserInfos.addAll(0, userInfos);

        notifyDataSetChanged();
    }

    private void initUser() {
        mUserInfos.add(new UserInfo("remove"));
        mUserInfos.add(0, new UserInfo("add"));
    }

    public boolean getDeleteMode() {
        return mIsDeleteMode;
    }

    public void setDeleteMode(boolean isDeleteMode) {
        mIsDeleteMode = isDeleteMode;
        // 刷新
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mUserInfos == null ? 0 : mUserInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mUserInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.adapter_group_members, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        /**
         *  处理ItemView, 即按情况分别处理加号,减号和群成员
         *
         *  群主: 除群成员外, 加号和减号都可见, 即有权限踢人和加人
         *  群成员: 只可见群成员
         */

        if (EMClient.getInstance().getCurrentUser().equals(mOwner)) {

            /**
             * 当前用户是群主时
             */

            if (position == mUserInfos.size() - 1) {
                // 最后一个位置的itemView: 减号

                if (mIsDeleteMode) {
                    // 隐藏删除模式下的减号
                    convertView.setVisibility(View.GONE);

                } else {
                    // 展示整个减号
                    convertView.setVisibility(View.VISIBLE);
                    // 隐藏群成员上的减号
                    viewHolder.mIvMemberDelete.setVisibility(View.GONE);
                    // 隐藏群成员的名字
                    viewHolder.mTvMemberName.setVisibility(View.INVISIBLE);
                    // 设置图片
                    viewHolder.mIvMemberPhoto.setImageResource(R.mipmap.em_smiley_minus_btn_pressed);

                }

            } else if (position == mUserInfos.size() - 2) {
                // 倒数第二个位置的itemView: 加号

                if (mIsDeleteMode) {
                    // 隐藏删除模式下的加号
                    convertView.setVisibility(View.GONE);

                } else {
                    // 展示整个加号
                    convertView.setVisibility(View.VISIBLE);
                    // 隐藏群成员上的减号
                    viewHolder.mIvMemberDelete.setVisibility(View.GONE);
                    // 隐藏名字
                    viewHolder.mTvMemberName.setVisibility(View.INVISIBLE);
                    // 设置图片
                    viewHolder.mIvMemberPhoto.setImageResource(R.mipmap.em_smiley_add_btn_pressed);
                }

            } else {
                // 其他位置的itemView: 群成员

                convertView.setVisibility(View.VISIBLE);
                viewHolder.mTvMemberName.setVisibility(View.VISIBLE);

                // 根据删除模式决定是否显示群成员上的小减号
                if (mIsDeleteMode) {
                    // 删除模式下可见
                    viewHolder.mIvMemberDelete.setVisibility(View.VISIBLE);
                } else {
                    // 不是删除模式不可见
                    viewHolder.mIvMemberDelete.setVisibility(View.GONE);
                }

                viewHolder.mTvMemberName.setText(mUserInfos.get(position).getUsername());
                viewHolder.mIvMemberPhoto.setImageResource(R.mipmap.em_default_avatar);
            }

            /**
             * 监听事件
             */
            if (position == mUserInfos.size() - 1) {
                // 最后位置的减号

                viewHolder.mIvMemberPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mIsDeleteMode) {
                            // 设置为删除模式
                            mIsDeleteMode = true;
                            // 刷新
                            notifyDataSetChanged();
                        }
                    }
                });

            } else if (position == mUserInfos.size() - 2) {
                // 倒数第二位置的加号

                viewHolder.mIvMemberPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            } else {
                // 群成员上的减号

                viewHolder.mIvMemberDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }

        } else {

            /**
             * 当前用户是群成员时
             */
            if (position == mUserInfos.size() - 1) {
                // 隐藏最后位置的减号
                convertView.setVisibility(View.GONE);

            } else if (position == mUserInfos.size() - 2) {

                if (mIsCanModify) {
                    // 显示加号
                    convertView.setVisibility(View.VISIBLE);
                    // 隐藏群成员上的减号
                    viewHolder.mIvMemberDelete.setVisibility(View.GONE);
                    // 隐藏名字
                    viewHolder.mTvMemberName.setVisibility(View.INVISIBLE);
                    // 设置图片
                    viewHolder.mIvMemberPhoto.setImageResource(R.mipmap.em_smiley_add_btn_pressed);

                    viewHolder.mIvMemberPhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ShowToast.show((Activity) mContext, "邀请成员");
                        }
                    });
                } else {
                    // 隐藏倒数第二位置的加号
                    convertView.setVisibility(View.GONE);
                }

            } else {
                // 其他的群成员
                convertView.setVisibility(View.VISIBLE);
                viewHolder.mTvMemberName.setText(mUserInfos.get(position).getUsername());

                // 隐藏群成员上的减号, 这是群主在删除模式下才可见的
                viewHolder.mIvMemberDelete.setVisibility(View.GONE);
            }

            // 监听

        }

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.iv_member_photo)
        ImageView mIvMemberPhoto;
        @BindView(R.id.tv_member_name)
        TextView mTvMemberName;
        @BindView(R.id.iv_member_delete)
        ImageView mIvMemberDelete;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
