package com.tron.huanxindemo.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.tron.huanxindemo.R;
import com.tron.huanxindemo.controller.adapter.PickAdapter;
import com.tron.huanxindemo.model.Model;
import com.tron.huanxindemo.model.bean.PickInfo;
import com.tron.huanxindemo.model.bean.UserInfo;
import com.tron.huanxindemo.utils.ShowToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PickContactActivity extends AppCompatActivity {

    @BindView(R.id.tv_pick_save)
    TextView tvPickSave;
    @BindView(R.id.lv_pick)
    ListView lvPick;

    private PickAdapter pickAdapter;

    private List<PickInfo> pickInfos;

    // 返回选中的联系人
    @OnClick(R.id.tv_pick_save)
    public void onClick() {
        List<String> contactCheck = pickAdapter.getContactsChecked();

        if (contactCheck == null) {
            return;
        }

        Intent intent = new Intent();

        // ArrayList提供了一个将List转为数组的一个非常方便的方法toArray
        intent.putExtra("members", contactCheck.toArray(new String[contactCheck.size()]));

        setResult(RESULT_OK, intent); // ResultCode

        // 结束当前页面
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_contact);
        ButterKnife.bind(this);

        // 初始化布局
        initView();

        // 初始化数据
        initData();

        // 设置监听
        initListener();
    }

    private void initView() {
        // 初始化适配器
        pickAdapter = new PickAdapter(this);

        // ListView设置适配器
        lvPick.setAdapter(pickAdapter);

    }

    private void initData() {

        // 获取联系人, 本地
        List<UserInfo> contacts = Model.getInstance().getDBManager().getContactTableDao().getContacts();

        if (contacts == null) {
            return;
        }

        if (contacts.size() == 0) {
            ShowToast.show(this, "您还没有好友!");
        }

        // 转换数据
        pickInfos = new ArrayList<>();

        for (UserInfo userInfo : contacts) {
            pickInfos.add(new PickInfo(userInfo, false));
        }

        pickAdapter.refresh(pickInfos);
    }

    private void initListener() {

        // item点击事件
        lvPick.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // 获取item里面的checkbox
                CheckBox cbPick = (CheckBox) view.findViewById(R.id.cb_item_pick_contacts);

                // 对当前checkbox状态进行取反
                cbPick.setChecked(!cbPick.isChecked());

                PickInfo pickInfo = pickInfos.get(position);

                // 设置当前的状态
                pickInfo.setChecked(cbPick.isChecked());

                // 刷新适配器
                pickAdapter.refresh(pickInfos);

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 返回事件处理的结果
            finish();

            // 返回true消费掉事件
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
