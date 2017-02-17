package com.tron.huanxindemo.controller.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.tron.huanxindemo.R;
import com.tron.huanxindemo.controller.activity.LoginActivity;
import com.tron.huanxindemo.model.Model;
import com.tron.huanxindemo.utils.ShowToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Tron on 2017/2/15.
 */

public class SettingsFragment extends Fragment {

    @BindView(R.id.setting_btn_exit)
    Button settingBtnExit;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_setting, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        settingBtnExit.setText("退出登录(" + EMClient.getInstance().getCurrentUser() + ")");
    }

    @OnClick(R.id.setting_btn_exit)
    public void onClick() {
        // 思路: 网络 -> 本地 -> 界面
        Model.getInstance().getGlobalThread().execute(new Runnable() {
            @Override
            public void run() {
                // 网络: 告诉环信服务器要退出了
                EMClient.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        // 推出后的其他操作
                        Model.getInstance().exitLogin();

                        // 跳转到登陆界面
                        ShowToast.showUI(getActivity(), "退出成功!");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);

                        // 结束当前页面
                        getActivity().finish();
                    }

                    @Override
                    public void onError(int i, String s) {
                        ShowToast.showUI(getActivity(), "退出失败" + s);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 解绑
        unbinder.unbind();
    }
}
