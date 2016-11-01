package com.zsmarter.keyboard;

//
//import com.zsmarter.wangxf.R;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by wangxf on 2016/10/25.
// */
//
//public class KeyBoardActivity extends Activity implements View.OnClickListener{
//    Button btn_num_00, btn_num_01, btn_num_02, btn_num_03, btn_num_04, btn_num_05,
//            btn_num_06, btn_num_07, btn_num_08, btn_num_09;
//    ImageButton btn_num_del, btn_num_back;
//    ImageView password01, password02, password03, password04, password05, password06;
//    Map passwords;
//    RelativeLayout rl_fa,rl_dark_bg;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        String type = getIntent().getStringExtra("type");
//        if(type.equals("full")){
//            setTheme(R.style.FullTransparent);
//        }
//        if(type.equals("float")){
//            setTheme(R.style.FloatTransparent);
//        }
//        setContentView(R.layout.keyboard);
//        initView();
//        initListener();
//        initData();
//    }
//
//    private void initData() {
//        passwords = new HashMap();
////        rl_fa.getBackground().setAlpha(80);
//    }
//
//    private void initView() {
////        rl_fa = (RelativeLayout) findViewById(R.id.rl_fuather);
////        rl_dark_bg = (RelativeLayout) findViewById(R.id.rl_dark_bg);
//        btn_num_00 = (Button) findViewById(R.id.btn_num_00);
//        btn_num_01 = (Button) findViewById(R.id.btn_num_01);
//        btn_num_02 = (Button) findViewById(R.id.btn_num_02);
//        btn_num_03 = (Button) findViewById(R.id.btn_num_03);
//        btn_num_04 = (Button) findViewById(R.id.btn_num_04);
//        btn_num_05 = (Button) findViewById(R.id.btn_num_05);
//        btn_num_06 = (Button) findViewById(R.id.btn_num_06);
//        btn_num_07 = (Button) findViewById(R.id.btn_num_07);
//        btn_num_08 = (Button) findViewById(R.id.btn_num_08);
//        btn_num_09 = (Button) findViewById(R.id.btn_num_09);
//        btn_num_del = (ImageButton) findViewById(R.id.btn_num_del);
////        btn_num_back = (ImageButton) findViewById(R.id.btn_num_back);
//        password01 = (ImageView) findViewById(R.id.password_1);
//        password02 = (ImageView) findViewById(R.id.password_2);
//        password03 = (ImageView) findViewById(R.id.password_3);
//        password04 = (ImageView) findViewById(R.id.password_4);
//        password05 = (ImageView) findViewById(R.id.password_5);
//        password06 = (ImageView) findViewById(R.id.password_6);
//    }
//
//    private void initListener() {
//        btn_num_00.setOnClickListener(this);
//        btn_num_01.setOnClickListener(this);
//        btn_num_02.setOnClickListener(this);
//        btn_num_03.setOnClickListener(this);
//        btn_num_04.setOnClickListener(this);
//        btn_num_05.setOnClickListener(this);
//        btn_num_06.setOnClickListener(this);
//        btn_num_07.setOnClickListener(this);
//        btn_num_08.setOnClickListener(this);
//        btn_num_09.setOnClickListener(this);
//        btn_num_del.setOnClickListener(this);
////        rl_dark_bg.setOnClickListener(this);
////        rl_fa.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btn_num_00:
//                addPassword(0);
//                break;
//            case R.id.btn_num_01:
//                addPassword(1);
//                break;
//            case R.id.btn_num_02:
//                addPassword(2);
//                break;
//            case R.id.btn_num_03:
//                addPassword(3);
//                break;
//            case R.id.btn_num_04:
//                addPassword(4);
//                break;
//            case R.id.btn_num_05:
//                addPassword(5);
//                break;
//            case R.id.btn_num_06:
//                addPassword(6);
//                break;
//            case R.id.btn_num_07:
//                addPassword(7);
//                break;
//            case R.id.btn_num_08:
//                addPassword(8);
//                break;
//            case R.id.btn_num_09:
//                addPassword(9);
//                break;
//            case R.id.btn_num_del:
//                delPassword();
//                break;
////            case R.id.rl_fuather:
////                finish();
////                break;
////            case R.id.btn_num_back:
////                this.finish();
////                break;
//        }
//    }
//
//    public void showPass(){
//        StringBuilder password = new StringBuilder();
//        for(int i=0;i<passwords.size();i++){
//            password.append(passwords.get(i));
//        }
//        Toast.makeText(this,password.toString(), Toast.LENGTH_SHORT).show();
//    }
//
//    private void delPassword() {
//        if(passwords.size()==0){
//            return;
//        }
//        passwords.remove(passwords.size()-1);
//        delPasswordIcon();
//        showPass();
//    }
//
//    private void delPasswordIcon() {
//        switch (passwords.size()){
//            case 0:
//                password01.setVisibility(View.INVISIBLE);
//                break;
//            case 1:
//                password02.setVisibility(View.INVISIBLE);
//                break;
//            case 2:
//                password03.setVisibility(View.INVISIBLE);
//                break;
//            case 3:
//                password04.setVisibility(View.INVISIBLE);
//                break;
//            case 4:
//                password05.setVisibility(View.INVISIBLE);
//                break;
//            case 5:
//                password06.setVisibility(View.INVISIBLE);
//                break;
//            case 6:
//                break;
//        }
//    }
//
//    private void addPassword(int num) {
//        if(passwords.size()>=6){
//            return;
//        }
//        passwords.put(passwords.size(),num);
//        addPasswordIcon();
//        showPass();
//    }
//
//    private void addPasswordIcon() {
//       switch (passwords.size()){
//           case 0:
//           break;
//           case 1:
//               password01.setVisibility(View.VISIBLE);
//               break;
//           case 2:
//               password02.setVisibility(View.VISIBLE);
//               break;
//           case 3:
//               password03.setVisibility(View.VISIBLE);
//               break;
//           case 4:
//               password04.setVisibility(View.VISIBLE);
//               break;
//           case 5:
//               password05.setVisibility(View.VISIBLE);
//               break;
//           case 6:
//               password06.setVisibility(View.VISIBLE);
//               break;
//       }
//    }
//
//    @Override
//    public void finish() {
//        // TODO Auto-generated method stub
//        super.finish();
//    }
//}
