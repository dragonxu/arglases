package com.panxsoft.xiaojingxiuxiu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.panxsoft.xiaojingxiuxiu.adapter.NameAdapter;
import com.panxsoft.xiaojingxiuxiu.app.MyApp;
import com.panxsoft.xiaojingxiuxiu.litemolder.ProductMolder;
import com.panxsoft.xiaojingxiuxiu.litemolder.ProductlibMolder;
import com.panxsoft.xiaojingxiuxiu.utils.Gmethod;
import com.panxsoft.xiaojingxiuxiu.utils.Namepopuwindow;
import com.panxsoft.xiaojingxiuxiu.utils.Picepopuwindow;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NameActivity extends Activity {
    private static String TAG = "Name" + Gmethod.TAG_plus;
    @BindView(R.id.recy)
    RecyclerView recy;
    private MyApp myApp;

    /**
     * 返回
     */
    private Button btnReturn;

    //   private ListView listView;
    private ListAdapter listAdapter;
    // 显示的列表
    ArrayList<Map<String, String>> list = null;

    List<ProductlibMolder> proall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        ButterKnife.bind(this);
        Log.d("ACTIVITY：", getClass().getSimpleName());
        myApp = (MyApp) getApplication();

        proall = LitePal.findAll(ProductlibMolder.class);

        btnReturn = (Button) findViewById(R.id.btn_back);
        // 返回到主菜单
        btnReturn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              /*  // 返回之前，刷新所有轨道商品的信息
                for(int i=0;i<myApp.getGoodsBeanArray().length;i++){
                    String code = myApp.getGoodsBeanArray()[i].goodscode;
                    String name = myApp.getGoodsBeanArray()[i].goodsname;
                    int price = myApp.getGoodsBeanArray()[i].price;

                    for(int k=0;k<myApp.getTrackMainGeneral().length;k++){
                        if(myApp.getTrackMainGeneral()[k].getGoodscode().equals(code)){
                            myApp.getTrackMainGeneral()[k].setGoodsname(name);
                            myApp.getTrackMainGeneral()[k].setPrice(price);
                        }
                    }
                }

                //  goodscode,goodsname,price
                SharedPreferences sp =
                        NameActivity.this.getSharedPreferences("vmsetting", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();//获取编辑器
                editor.putString("track11",  getSPStr(0));  //  goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                editor.putString("track14",  getSPStr(1));
                editor.putString("track15",  getSPStr(2));
                editor.putString("track21",  getSPStr(3));
                editor.putString("track24",  getSPStr(4));
                editor.putString("track27",  getSPStr(5));
                editor.putString("track31",  getSPStr(6));
                editor.putString("track34",  getSPStr(7));
                editor.putString("track37",  getSPStr(8));
                editor.putString("track41",  getSPStr(9));
                editor.putString("track44",  getSPStr(10));
                editor.putString("track47",  getSPStr(11));
                editor.putString("track51",  getSPStr(12));
                editor.putString("track54",  getSPStr(13));
                editor.putString("track57",  getSPStr(14));
                editor.putString("track61",  getSPStr(15));
                editor.putString("track64",  getSPStr(16));
                editor.putString("track67",  getSPStr(17));
                editor.putString("track71",  getSPStr(18));
                editor.putString("track74",  getSPStr(19));
                editor.putString("track77",  getSPStr(20));
                editor.apply();//提交修改
*/

                Intent toMenu = new Intent(NameActivity.this, MenuActivity.class);
                startActivity(toMenu);

                NameActivity.this.finish();
            }
        });

        /*listView = (ListView) findViewById(R.id.list);

        //调用显示的函数，把结果显示出来
        setListAdapter();
*/
        initView();
    }

    private void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(NameActivity.this);
        recy.setLayoutManager(manager);
        final NameAdapter adapter = new NameAdapter(NameActivity.this,proall);
        recy.setAdapter(adapter);
        adapter.SetItemclike(new NameAdapter.Onclike() {
            @Override
            public void nameonclike(final int position) {
                Namepopuwindow namepopuwindow = new Namepopuwindow(NameActivity.this);
                namepopuwindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_name, null),Gravity.CENTER,0,-300);
                namepopuwindow.SetOnclike(new Namepopuwindow.Onclike() {
                    @Override
                    public void okclike(String s) {
                        if(!TextUtils.isEmpty(s)){
                            ProductlibMolder molder = new ProductlibMolder();
                            molder.setName(s);
                            molder.updateAll("post = ?",position+"");
                            proall = LitePal.findAll(ProductlibMolder.class);
                            adapter.update(proall,position);
                            ProductMolder molders = new ProductMolder();
                            molders.setName(s);
                            molders.updateAll("post = ?",position+"");
                        }
                    }
                });
            }

            @Override
            public void pricclike(final int position) {
                Picepopuwindow picepopuwindow = new Picepopuwindow(NameActivity.this);
                picepopuwindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_name, null),Gravity.CENTER,0,-100);
                picepopuwindow.SetOnclike(new Picepopuwindow.Onclike() {
                    @Override
                    public void okclike(String s) {
                        if(!TextUtils.isEmpty(s)){
                            ProductlibMolder molder = new ProductlibMolder();
                            molder.setPrice(s);
                            molder.updateAll("post = ?",position+"");
                            proall = LitePal.findAll(ProductlibMolder.class);
                            adapter.update(proall,position);
                            ProductMolder molders = new ProductMolder();
                            molders.setName(s);
                            molders.updateAll("post = ?",position+"");
                        }
                    }
                });
            }
        });
    }

    private String getSPStr(int i) {
        return myApp.getTrackMainGeneral()[i].getGoodscode() + ","
                + myApp.getTrackMainGeneral()[i].getGoodsname() + ","
                + myApp.getTrackMainGeneral()[i].getPrice() + ","
                + myApp.getTrackMainGeneral()[i].getNummax() + ","
                + myApp.getTrackMainGeneral()[i].getNumnow() + ","
                + myApp.getTrackMainGeneral()[i].getErrorcode() + ","
                + myApp.getTrackMainGeneral()[i].getErrortime();
    }

    private void setListAdapter() {
        // 显示的列表
        list = new ArrayList<>();

        // 计算有多少条需要显示的，准备内容
        Map<String, String> map;

        for (int i = 0; i < myApp.getGoodsBeanArray().length; i++) {
            map = new HashMap<>();

            map.put("no", "" + (i + 1));

            Log.d("zlc", myApp.getGoodsBeanArray()[i].goodscode + "," + i);
            Log.d("zlc", myApp.getGoodsBeanArray()[i].goodsname + "," + i);
            map.put("code", myApp.getGoodsBeanArray()[i].goodscode);
            map.put("name", myApp.getGoodsBeanArray()[i].goodsname);
            map.put("price", Gmethod.tranFenToSimple(myApp.getGoodsBeanArray()[i].price));

            list.add(map);
        }


       // listView.removeAllViewsInLayout();
        listAdapter = new ListAdapter(this, list);
      //  listView.setAdapter(listAdapter);
    }

    public class ListAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<Map<String, String>> localList;
        //这就是adapter关联的List，用来存储数据的ArrayList　要往里传参数吗？　传的也是这个类型啊．呵呵

        public ListAdapter(Context context, ArrayList<Map<String, String>> list) {
            this.context = context;
            this.localList = list;
        }

        public int getCount() {
            return localList.size();
        }

        public Object getItem(int position) {
            return localList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Map<String, String> map = localList.get(position);
            return new adapterView(this.context, map);
        }

    }

    class adapterView extends LinearLayout {
        //public static final String LOG_TAG = "adapterView";

        public adapterView(Context context, Map<String, String> map) {
            super(context);

            this.setOrientation(HORIZONTAL);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 2, 2, 2);

            LinearLayout mLineLayout = new LinearLayout(context);

            TextView notextControl = new TextView(context);
            LayoutParams notextparams = new LayoutParams(30, LayoutParams.WRAP_CONTENT);
            notextparams.setMargins(0, 34, 0, 4);
            notextControl.setTextColor(Color.BLACK);
            notextControl.setTextSize(26);
            notextControl.setText(map.get("no"));
            notextControl.setLayoutParams(notextparams);
            mLineLayout.addView(notextControl);

            ImageView imageView = new ImageView(context);
            LayoutParams viewparams = new LayoutParams(128, 128);//标准:360*440 的 16分之1
            viewparams.setMargins(10, 4, 0, 4);
            imageView.setLayoutParams(viewparams);
            if (map.get("no").equals("2")) {
                imageView.setImageResource(R.drawable.g03042);
            } else if (map.get("no").equals("3")) {
                imageView.setImageResource(R.drawable.g03044);
            } else if (map.get("no").equals("4")) {
                imageView.setImageResource(R.drawable.g03051);
            } else if (map.get("no").equals("5")) {
                imageView.setImageResource(R.drawable.g03060);
            } else if (map.get("no").equals("6")) {
                imageView.setImageResource(R.drawable.g05222);
            } else if (map.get("no").equals("7")) {
                imageView.setImageResource(R.drawable.g05223);
            } else if (map.get("no").equals("8")) {
                imageView.setImageResource(R.drawable.g05224);
            } else if (map.get("no").equals("9")) {
                imageView.setImageResource(R.drawable.g05226);
            } else if (map.get("no").equals("10")) {
                imageView.setImageResource(R.drawable.g11020);
            } else if (map.get("no").equals("11")) {
                imageView.setImageResource(R.drawable.g11021);
            } else if ((map.get("no").equals("1"))) {
                imageView.setImageResource(R.drawable.g093);
            } else if ((map.get("no").equals("12"))) {
                imageView.setImageResource(R.drawable.go1);
            } else {
                imageView.setImageResource(R.drawable.noimage);
            }
            mLineLayout.addView(imageView);

            TextView codetextControl = new TextView(context);
            LayoutParams codetextparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            codetextparams.setMargins(10, 34, 0, 0);
            codetextControl.setTextColor(Color.BLUE);
            codetextControl.setTextSize(26);
            codetextControl.setText(map.get("code"));
            codetextControl.setLayoutParams(codetextparams);
            mLineLayout.addView(codetextControl);

            TextView nametextControl = new TextView(context);
            LayoutParams nametextparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            nametextparams.setMargins(10, 34, 0, 0);
            nametextControl.setTextColor(Color.BLACK);
            nametextControl.setTextSize(26);
            nametextControl.setText(map.get("name"));
            nametextControl.setLayoutParams(nametextparams);
            mLineLayout.addView(nametextControl);


            TextView pricetextControl = new TextView(context);
            LayoutParams pricetextparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            pricetextparams.setMargins(40, 34, 0, 0);
            pricetextControl.setTextColor(Color.RED);
            pricetextControl.setTextSize(26);
            pricetextControl.setText(map.get("price"));
            pricetextControl.setLayoutParams(pricetextparams);
            mLineLayout.addView(pricetextControl);

            Button namebtn = new Button(context);
            LayoutParams nameparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            nameparams.setMargins(20, 6, 0, 6);
            nameparams.gravity = Gravity.CENTER;
            namebtn.setText(" 名称 ");
            namebtn.setTextSize(26);
            namebtn.setId(Integer.parseInt(map.get("no")));
            namebtn.setLayoutParams(nameparams);
            namebtn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    //do something
                    final int v_id = v.getId();

                    String title = "请输入新的名称";

                    final EditText inputprice = new EditText(NameActivity.this);

                    inputprice.setText(myApp.getGoodsBeanArray()[v_id - 1].goodsname);

                    inputprice.setMaxEms(24);
                    inputprice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(24)});
                    inputprice.setTextSize(20);

                    //光标停到最后的位置
                    CharSequence text = inputprice.getText();
                    if (text != null) {
                        Spannable spanText = (Spannable) text;
                        Selection.setSelection(spanText, text.length());
                    }

                    new AlertDialog.Builder(NameActivity.this)
                            .setTitle(title)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setView(inputprice)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String inputtext = inputprice.getText().toString();
                                    Log.i(TAG, "nameindex:" + (v_id - 1) + ";原来的名称:" + inputtext);

                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(inputprice.getWindowToken(), 0); //强制隐藏键盘


                                    if (inputtext.contains(",")) {
                                        Log.i(TAG, "您输入的名称有误:" + inputtext);

                                        Toast.makeText(NameActivity.this, "您输入的名称含有非法的，字符！", Toast.LENGTH_LONG).show();

                                    } else {

                                        Map<String, String> map = list.get(v_id - 1);
                                        map.put("name", inputtext);
                                        myApp.getGoodsBeanArray()[v_id - 1].goodsname = inputtext;

                                        //显示出来
                                        listAdapter.notifyDataSetChanged();

                                        //  goodscode,goodsname,price
                                        SharedPreferences sp =
                                                NameActivity.this.getSharedPreferences("vmsetting", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();//获取编辑器
                                        editor.putString("goods" + (v_id - 1), map.get("code") + "," + map.get("name") + "," + map.get("price"));  // 0表示没有麦克风
                                        editor.apply();//提交修改
                                    }


                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {

                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(inputprice.getWindowToken(), 0); //强制隐藏键盘

                                }
                            })
                            .show();
                }
            });
            mLineLayout.addView(namebtn);


            Button pricebtn = new Button(context);
            LayoutParams priceparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            priceparams.setMargins(20, 6, 0, 6);
            priceparams.gravity = Gravity.CENTER;
            pricebtn.setText(" 价格 ");
            pricebtn.setTextSize(26);
            pricebtn.setId(Integer.parseInt(map.get("no")));
            pricebtn.setLayoutParams(priceparams);
            pricebtn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    //do something
                    final int v_id = v.getId();

                    String title = "请输入新的价格";

                    final EditText inputprice = new EditText(NameActivity.this);
                    inputprice.setText(Gmethod.tranFenToSimple(myApp.getGoodsBeanArray()[v_id - 1].price));
                    inputprice.setMaxEms(24);
                    inputprice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(24)});
                    inputprice.setTextSize(20);
                    inputprice.setKeyListener(new NumberKeyListener() {
                        @Override
                        protected char[] getAcceptedChars() {
                            return new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.'};
                        }

                        @Override
                        public int getInputType() {
                            return InputType.TYPE_CLASS_PHONE;
                        }
                    });

                    //光标停到最后的位置
                    CharSequence text = inputprice.getText();
                    if (text != null) {
                        Spannable spanText = (Spannable) text;
                        Selection.setSelection(spanText, text.length());
                    }

                    new AlertDialog.Builder(NameActivity.this)
                            .setTitle(title)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setView(inputprice)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String inputtext = inputprice.getText().toString();
                                    Log.i(TAG, "nameindex:" + (v_id - 1) + ";原来的价格:" + inputtext);

                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(inputprice.getWindowToken(), 0); //强制隐藏键盘

                                    boolean ispriceok = true;//价格合法？
                                    int newprice = 100;
                                    // 说明没有“#”，那就是一个价格
                                    if (!inputtext.contains(".")) {
                                        //表示没有小数点
                                        inputtext = inputtext + ".00";
                                    }
                                    if (inputtext.indexOf(".") == inputtext.length() - 1) {
                                        //最后一位是.
                                        inputtext = inputtext + "00";
                                    }
                                    if (inputtext.indexOf(".") == inputtext.length() - 2) {
                                        //最后第二位是.
                                        inputtext = inputtext + "0";
                                    }
                                    Log.i(TAG, "规整后的价格(相同价格):" + inputtext);
                                    if (inputtext.length() - inputtext.indexOf(".") > 3) {
                                        //123.045:7-3=4
                                        ispriceok = false;
                                    } else {
                                        if (Integer.parseInt(inputtext.replace(".", "")) == 0) {
                                            ispriceok = false;
                                        } else {
                                            newprice = Gmethod.tranYuanToFen(inputtext);
                                        }
                                    }


                                    if (ispriceok) {
                                        Map<String, String> map = list.get(v_id - 1);
                                        map.put("price", "" + Gmethod.tranFenToSimple(newprice));
                                        myApp.getGoodsBeanArray()[v_id - 1].price = newprice;
                                        //显示出来
                                        listAdapter.notifyDataSetChanged();

                                        //  goodscode,goodsname,price
                                        SharedPreferences sp =
                                                NameActivity.this.getSharedPreferences("vmsetting", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();//获取编辑器
                                        editor.putString("goods" + (v_id - 1), map.get("code") + "," + map.get("name") + "," + newprice);  // 0表示没有麦克风
                                        editor.apply();//提交修改


                                    } else {
                                        Log.i(TAG, "您输入的价格有误:" + inputtext);

                                        Toast.makeText(NameActivity.this, "您输入的价格有误！应该为3或3.5或5.01的格式", Toast.LENGTH_LONG).show();
                                    }


                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {

                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(inputprice.getWindowToken(), 0); //强制隐藏键盘

                                    //dialog.dismiss();
                                }
                            })
                            .show();
                }
            });
            mLineLayout.addView(pricebtn);

            addView(mLineLayout, params);
        }
    }


    @Override
    public void onBackPressed() {
        Log.v(TAG, "onBackPressed()");

        // 返回按钮的处理，不让程序退出的话，可以注解下面这行代码
        //super.onBackPressed();

    }
}
