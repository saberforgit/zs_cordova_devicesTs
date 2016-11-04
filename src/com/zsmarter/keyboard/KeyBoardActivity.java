package com.zsmarter.keyboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.zsmarter.utils.XFResourcesIDFinder;
import java.util.HashMap;
import java.util.Map;
import cn.cgnb.core.security.Encode;
import static com.zsmarter.utils.XFResourcesIDFinder.*;


/**
 * Created by wangxf on 2016/10/25.
 */

public class KeyBoardActivity extends Activity implements View.OnClickListener {
    Button btn_num_00, btn_num_01, btn_num_02, btn_num_03, btn_num_04, btn_num_05,
            btn_num_06, btn_num_07, btn_num_08, btn_num_09;
    ImageButton btn_num_del, btn_num_back;
    ImageView password01, password02, password03, password04, password05, password06;
    Map passwords;
    RelativeLayout rl_fa, rl_dark_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XFResourcesIDFinder.init(this);
        String type = getIntent().getStringExtra("type");
        if (type.equals("full")) {
            setTheme(getResStyleID("FullTransparent"));
        }
        if (type.equals("float")) {
            setTheme(getResStyleID("FloatTransparent"));
        }
        setContentView(getResLayoutID("keyboard"));
        initView();
        initListener();
        initData();
    }

    private void initData() {
        passwords = new HashMap();
//        rl_fa.getBackground().setAlpha(80);
    }


    private void initView() {
        rl_fa = (RelativeLayout) findViewById(getResIdID("rl_fuather"));
        rl_dark_bg = (RelativeLayout) findViewById(getResIdID("rl_bg"));
        btn_num_00 = (Button) findViewById(getResIdID("btn_num_00"));
        btn_num_01 = (Button) findViewById(getResIdID("btn_num_01"));
        btn_num_02 = (Button) findViewById(getResIdID("btn_num_02"));
        btn_num_03 = (Button) findViewById(getResIdID("btn_num_03"));
        btn_num_04 = (Button) findViewById(getResIdID("btn_num_04"));
        btn_num_05 = (Button) findViewById(getResIdID("btn_num_05"));
        btn_num_06 = (Button) findViewById(getResIdID("btn_num_06"));
        btn_num_07 = (Button) findViewById(getResIdID("btn_num_07"));
        btn_num_08 = (Button) findViewById(getResIdID("btn_num_08"));
        btn_num_09 = (Button) findViewById(getResIdID("btn_num_09"));
        btn_num_del = (ImageButton) findViewById(getResIdID("btn_num_del"));
        btn_num_back = (ImageButton) findViewById(getResIdID("btn_back"));
        password01 = (ImageView) findViewById(getResIdID("password_1"));
        password02 = (ImageView) findViewById(getResIdID("password_2"));
        password03 = (ImageView) findViewById(getResIdID("password_3"));
        password04 = (ImageView) findViewById(getResIdID("password_4"));
        password05 = (ImageView) findViewById(getResIdID("password_5"));
        password06 = (ImageView) findViewById(getResIdID("password_6"));
    }

    private void initListener() {
        btn_num_00.setOnClickListener(this);
        btn_num_01.setOnClickListener(this);
        btn_num_02.setOnClickListener(this);
        btn_num_03.setOnClickListener(this);
        btn_num_04.setOnClickListener(this);
        btn_num_05.setOnClickListener(this);
        btn_num_06.setOnClickListener(this);
        btn_num_07.setOnClickListener(this);
        btn_num_08.setOnClickListener(this);
        btn_num_09.setOnClickListener(this);
        btn_num_del.setOnClickListener(this);
        btn_num_back.setOnClickListener(this);
        rl_dark_bg.setOnClickListener(this);
        rl_fa.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == getResIdID("btn_num_00")) {
            addPassword(0);
        }
        if (v.getId() == getResIdID("btn_num_01")) {
            addPassword(1);
        }
        if (v.getId() == getResIdID("btn_num_02")) {
            addPassword(2);
        }
        if (v.getId() == getResIdID("btn_num_03")) {
            addPassword(3);
        }
        if (v.getId() == getResIdID("btn_num_04")) {
            addPassword(4);
        }
        if (v.getId() == getResIdID("btn_num_05")) {
            addPassword(5);
        }
        if (v.getId() == getResIdID("btn_num_06")) {
            addPassword(6);
        }
        if (v.getId() == getResIdID("btn_num_07")) {
            addPassword(7);
        }
        if (v.getId() == getResIdID("btn_num_08")) {
            addPassword(8);
        }
        if (v.getId() == getResIdID("btn_num_09")) {
            addPassword(9);
        }
        if (v.getId() == getResIdID("btn_num_del")) {
            delPassword();
        }
        if (v.getId() == getResIdID("btn_back")) {
            finish();
        }
    }

    public void showPass() {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < passwords.size(); i++) {
            password.append(passwords.get(i));
        }
//        Toast.makeText(this,password.toString(),Toast.LENGTH_SHORT).show();
        Log.d("password", password.toString());
    }

    private void delPassword() {
        if (passwords.size() == 0) {
            return;
        }
        passwords.remove(passwords.size() - 1);
        delPasswordIcon();
        showPass();
    }

    private void delPasswordIcon() {
        switch (passwords.size()) {
            case 0:
                password01.setVisibility(View.INVISIBLE);
                break;
            case 1:
                password02.setVisibility(View.INVISIBLE);
                break;
            case 2:
                password03.setVisibility(View.INVISIBLE);
                break;
            case 3:
                password04.setVisibility(View.INVISIBLE);
                break;
            case 4:
                password05.setVisibility(View.INVISIBLE);
                break;
            case 5:
                password06.setVisibility(View.INVISIBLE);
                break;
            case 6:
                break;
        }
    }

    private void addPassword(int num) {
        passwords.put(passwords.size(), num);
        addPasswordIcon();
        showPass();
        if (passwords.size() >= 6) {
            Log.d("size", passwords.size() + "");
            encodePassword(passwords.toString());
            return;
        }

    }

    private void encodePassword(String password) {
        String encodePass = Encode.openEncodePlainPass(password);
        Toast.makeText(this, encodePass, Toast.LENGTH_SHORT).show();
        Intent mIntent = new Intent();
        mIntent.putExtra("encodePassword", encodePass);
        // 设置结果，并进行传送
        this.setResult(200, mIntent);
        this.finish();
    }

    private void addPasswordIcon() {
        switch (passwords.size()) {
            case 0:
                break;
            case 1:
                password01.setVisibility(View.VISIBLE);
                break;
            case 2:
                password02.setVisibility(View.VISIBLE);
                break;
            case 3:
                password03.setVisibility(View.VISIBLE);
                break;
            case 4:
                password04.setVisibility(View.VISIBLE);
                break;
            case 5:
                password05.setVisibility(View.VISIBLE);
                break;
            case 6:
                password06.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
    }


}
