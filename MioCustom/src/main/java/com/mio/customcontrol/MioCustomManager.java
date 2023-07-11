package com.mio.customcontrol;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aof.mcinabox.gamecontroller.codes.Translation;
import com.aof.mcinabox.gamecontroller.definitions.id.key.KeyEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.mio.mclauncher.customcontrol.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MioCustomManager implements OnTouchListener,CompoundButton.OnCheckedChangeListener,OnClickListener {
	private boolean 是否已初始化=false;
	private String 储存路径;
	private Context context;
	private Gson mGson;
	public static int screenHeight,scrennWidth;

	private ViewGroup 容器;

	private Map<String,MioCustomButton> 自定义按键集合;
	private boolean 自定义开关=false;
	private Translation 键值转换器;

	private AlertDialog 自定义按键对话框;
	private AlertDialog 颜色选择对话框;
	private View 自定义按键菜单布局容器;
	private EditText alert_miocustom_main_edit_name;
	private TextView alert_miocustom_main_tv_height_left;
	private EditText alert_miocustom_main_edit_height;
	private TextView alert_miocustom_main_tv_height_right;
	private TextView alert_miocustom_main_tv_width_left;
	private EditText alert_miocustom_main_edit_width;
	private TextView alert_miocustom_main_tv_width_right;
	private TextView alert_miocustom_main_tv_textsize_left;
	private EditText alert_miocustom_main_edit_textsize;
	private TextView alert_miocustom_main_tv_textsize_right;
	private TextView alert_miocustom_main_tv_round_left;
	private EditText alert_miocustom_main_edit_round;
	private TextView alert_miocustom_main_tv_round_right;
	private EditText alert_miocustom_main_edit_strokecolor;
	private EditText alert_miocustom_main_edit_textcolor;
	private RadioButton alert_miocustom_main_radio_normalbtn;
	private RadioButton alert_miocustom_main_radio_cmdbtn;
	private RadioButton alert_miocustom_main_radio_vsbbtn;
	private LinearLayout alert_miocustom_main_layout_normalbtn;
	private EditText alert_miocustom_main_edit_key1;
	private EditText alert_miocustom_main_edit_key2;
	private EditText alert_miocustom_main_edit_key3;
	private CheckBox alert_miocustom_main_check_autokeep;
	private CheckBox alert_miocustom_main_check_mousectrl;
	private LinearLayout alert_miocustom_main_layout_cmdbtn;
	private EditText alert_miocustom_main_edit_cmd;
	private LinearLayout alert_miocustom_main_layout_vsbbtn;
	private Button alert_miocustom_main_btn_btnselect;
	private Button alert_miocustom_main_btn_ok;
	private Button alert_miocustom_main_btn_cancle;
	private View alert_miocustom_main_view_strokecolor;
	private View alert_miocustom_main_view_textcolor;

	private boolean 按键选择模式=false;

	private 自定义按键回调 自定义按键回调;

	private boolean 按键修改模式=false;

	private int 指针横坐标;
	private int 指针纵坐标;

	private MioDialogSelectKey 键值选择对话框;

	private boolean 控制鼠标指针修复=false;

	public interface 自定义按键回调 {
		void 命令接收事件(String 命令);
		void 键值接收事件(int 键值, boolean 按下);
		void 控制鼠标指针移动事件(int x, int y);
		void 鼠标回调(int 键值, boolean 按下);
		void 按下();
		void 抬起();
	}

	public void set控制鼠标指针修复(boolean 控制鼠标指针修复){
		this.控制鼠标指针修复=控制鼠标指针修复;
	}
	public void 控制鼠标指针修复(){
		if(this.控制鼠标指针修复){
			this.控制鼠标指针修复=false;
		} else {
			this.控制鼠标指针修复=true;
		}
	}
	public boolean get控制鼠标指针修复(){
		return this.控制鼠标指针修复;
	}
	public void set储存路径(String 储存路径) {
		this.储存路径 = 储存路径;
	}

	public String get储存路径() {
		return 储存路径;
	}

	public void 初始化(Context context, ViewGroup 容器, String 储存路径) {
		if (!是否已初始化) {
			this.context = context;
			this.容器 = 容器;
			键值转换器 = new Translation(KeyEvent.KEYMAP_TO_X);
			set储存路径(储存路径);
			反序列化();
			添加按键至容器();
			是否已初始化 = true;
			目标按键标识 = new ArrayList<>();
		}


	}
	public void 设置自定义按键回调(自定义按键回调 自定义按键回调) {
		this.自定义按键回调 = 自定义按键回调;
	}

	public static void 设置屏幕高宽(int g, int k) {
		scrennWidth = k;
		screenHeight = g;
	}
	private void 反序列化() {
		GsonBuilder builder=new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		builder.registerTypeAdapter(MioCustomButton.class, new MioDeserializer(context));
		mGson = builder.setPrettyPrinting().create();
		自定义按键集合 = new ArrayMap<>();
		try {
			FileReader reader=new FileReader(new File(储存路径, "Miokey.json"));
			BufferedReader bfr=new BufferedReader(reader);
			String tmp=null;
			String result="";
			while ((tmp = bfr.readLine()) != null) {
				result += tmp;
			}
			bfr.close();
			reader.close();
			JSONArray mJSONArray=new JSONArray(result);
			for (int i=0;i < mJSONArray.length();i++) {
				MioCustomButton mMioCustomButton=mGson.fromJson(mJSONArray.get(i).toString(), MioCustomButton.class);
				自定义按键集合.put(mMioCustomButton.get唯一标识(), mMioCustomButton);
				mMioCustomButton.setOnTouchListener(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void 添加按键至容器() {
		if (容器 != null&&自定义按键集合!=null) {
			Set<String> 标识集=自定义按键集合.keySet();
			for (String 标识:标识集) {
				容器.addView(自定义按键集合.get(标识));
				自定义按键集合.get(标识).初始化();
			}
			for (String 标识:标识集) {
				if (自定义按键集合.get(标识).get按键类型().equals("显隐控制按键")) {
					for (String 标识_:自定义按键集合.get(标识).get目标按键标识()) {
						if(获取按键(标识_)!=null){
							获取按键(标识_).setVisibility(View.GONE);
						}
					}
					自定义按键集合.get(标识).setTextColor(Color.RED);
				}
			}
		}
	}
	private List<String> 目标按键标识;
	private boolean 创建or修改=true;//创建true
	public void 创建按键() {
		try {
			JSONObject json=new JSONObject();
			json.put("文本", getText(alert_miocustom_main_edit_name));
			json.put("大小", new JSONArray().put(getText(alert_miocustom_main_edit_height)).put(getText(alert_miocustom_main_edit_width)));
			json.put("文本大小", getText(alert_miocustom_main_edit_textsize));
			json.put("文本颜色", getText(alert_miocustom_main_edit_textcolor));
			json.put("边框颜色", getText(alert_miocustom_main_edit_strokecolor));
			json.put("圆角半径", getText(alert_miocustom_main_edit_round));
			json.put("位置", new JSONArray().put(0.5).put(0.5));
			json.put("唯一标识", String.valueOf(System.currentTimeMillis()));
			json.put("默认隐藏", true);
			if (alert_miocustom_main_radio_normalbtn.isChecked()) {
				json.put("按键类型", "普通按键");
			} else if (alert_miocustom_main_radio_cmdbtn.isChecked()) {
				json.put("按键类型", "命令按键");
			} else if (alert_miocustom_main_radio_vsbbtn.isChecked()) {
				json.put("按键类型", "显隐控制按键");
			}
			json.put("按键自动保持", alert_miocustom_main_check_autokeep.isChecked());
			json.put("控制鼠标指针", alert_miocustom_main_check_mousectrl.isChecked());
			json.put("默认隐藏", true);
			json.put("命令", getText(alert_miocustom_main_edit_cmd));
			JSONArray mJ=new JSONArray();
			if (目标按键标识.size() > 0) {
				for (String s:目标按键标识) {
					mJ.put(s);
				}
			}
			json.put("目标按键标识", mJ);
			json.put("键值组", new JSONArray().put(getText(alert_miocustom_main_edit_key1)).put(getText(alert_miocustom_main_edit_key2)).put(getText(alert_miocustom_main_edit_key3)));
			MioCustomButton tmp=mGson.fromJson(json.toString(), MioCustomButton.class);
			自定义按键集合.put(tmp.get唯一标识(), tmp);
			容器.addView(tmp);
			tmp.初始化();
			tmp.setOnTouchListener(this);
			自定义按键对话框.dismiss();
			if (tmp.get按键类型().equals("显隐控制按键")) {
				for (String 标识:tmp.get目标按键标识()) {
					MioCustomButton b=获取按键(标识);
					b.setTextColor(Color.parseColor(b.get文本颜色()));
				}
			}
		} catch (JSONException e) {

		}

	}
	private boolean vi=true;
	public void 按键显示隐藏(){
		Set<String> 标识集=自定义按键集合.keySet();
		if (vi){
			vi=false;
			for (String 标识:标识集) {
				MioCustomButton b=自定义按键集合.get(标识);
				if(b!=null){
					b.setVisibility(View.INVISIBLE);
				}
			}
		}else{
			vi=true;
			for (String 标识:标识集) {
				MioCustomButton b=自定义按键集合.get(标识);
				if (b!=null){
					b.setVisibility(View.VISIBLE);
				}
			}
		}
	}
	private void 修改按键() {
		自定义按键集合.remove(当前按键.get唯一标识());
		try {
			Toast.makeText(context, "", Toast.LENGTH_LONG).show();
			JSONObject json=new JSONObject();
			json.put("文本", getText(alert_miocustom_main_edit_name));
			json.put("大小", new JSONArray().put(getText(alert_miocustom_main_edit_height)).put(getText(alert_miocustom_main_edit_width)));
			json.put("文本大小", getText(alert_miocustom_main_edit_textsize));
			json.put("文本颜色", getText(alert_miocustom_main_edit_textcolor));
			json.put("边框颜色", getText(alert_miocustom_main_edit_strokecolor));
			json.put("圆角半径", getText(alert_miocustom_main_edit_round));
			json.put("位置", new JSONArray().put(当前按键.get位置().get(0)).put(当前按键.get位置().get(1)));
			json.put("唯一标识", String.valueOf(System.currentTimeMillis()));
			json.put("默认隐藏", true);
			if (alert_miocustom_main_radio_normalbtn.isChecked()) {
				json.put("按键类型", "普通按键");
			} else if (alert_miocustom_main_radio_cmdbtn.isChecked()) {
				json.put("按键类型", "命令按键");
			} else if (alert_miocustom_main_radio_vsbbtn.isChecked()) {
				json.put("按键类型", "显隐控制按键");
			}
			json.put("按键自动保持", alert_miocustom_main_check_autokeep.isChecked());
			json.put("控制鼠标指针", alert_miocustom_main_check_mousectrl.isChecked());
			json.put("默认隐藏", true);
			json.put("命令", getText(alert_miocustom_main_edit_cmd));
			JSONArray mJ=new JSONArray();
			if (目标按键标识.size() > 0) {
				for (String s:目标按键标识) {
					mJ.put(s);
				}
			}
			json.put("目标按键标识", mJ);
			json.put("键值组", new JSONArray().put(getText(alert_miocustom_main_edit_key1)).put(getText(alert_miocustom_main_edit_key2)).put(getText(alert_miocustom_main_edit_key3)));
			MioCustomButton tmp=mGson.fromJson(json.toString(), MioCustomButton.class);
			自定义按键集合.put(tmp.get唯一标识(), tmp);
			容器.addView(tmp);
			tmp.初始化();
			tmp.setOnTouchListener(this);
			自定义按键对话框.dismiss();
			容器.removeView(当前按键);
			if (tmp.get按键类型().equals("显隐控制按键")) {
				for (String 标识:tmp.get目标按键标识()) {
					MioCustomButton b=获取按键(标识);
					b.setTextColor(Color.parseColor(b.get文本颜色()));
				}
			}
		} catch (JSONException e) {

		}
	}
	public void 清除按键() {
		Set<String> 标识集=自定义按键集合.keySet();
		for (String 标识:标识集) {
			容器.removeView(自定义按键集合.get(标识));
		}
		自定义按键集合.clear();
	}
    public void 保存按键() {
		try {
			FileOutputStream out=new FileOutputStream(new File(储存路径, "Miokey.json"));
			int i=1;
			StringBuilder builder=new StringBuilder();
			builder.append("[\n");
			Set<String> 标识集=自定义按键集合.keySet();
			for (String 标识:标识集) {
				if (自定义按键集合.size() > 1 && i > 1) {
					builder.append(",");
				}
				builder.append(mGson.toJson(自定义按键集合.get(标识), MioCustomButton.class));
				builder.append("\n");
				i++;
			}
			builder.append("\n]");
			out.write(builder.toString().getBytes(StandardCharsets.UTF_8));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void 自定义开关() {
		if (自定义开关) {
			自定义开关 = false;
			保存按键();
			Set<String> 所有按键标识=自定义按键集合.keySet();
			for (String 标识:所有按键标识) {
				MioCustomButton 按键=自定义按键集合.get(标识);
				if (按键.get按键类型().equals("显隐控制按键")) {
					for (String 标识2:按键.get目标按键标识()) {
						MioCustomButton b=获取按键(标识2);
						if(b!=null){
							b.setVisibility(View.GONE);
						}
					}
				}
			}
			Toast.makeText(context, "已退出自定义模式", Toast.LENGTH_LONG).show();
		} else {
			自定义开关 = true;
			Set<String> 所有按键标识=自定义按键集合.keySet();
			for (String 标识:所有按键标识) {
				MioCustomButton 按键=自定义按键集合.get(标识);
				if (按键.get按键类型().equals("显隐控制按键")) {
					for (String 标识2:按键.get目标按键标识()) {
						MioCustomButton b=获取按键(标识2);
						if(b!=null){
							b.setVisibility(View.VISIBLE);
						}
					}
				}
			}
            RelativeLayout 触摸板=new RelativeLayout(context);
            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
            容器.addView(触摸板,params);
			Toast.makeText(context, "已进入自定义模式", Toast.LENGTH_LONG).show();
		}
	}
	public void 自定义按键对话框(boolean 创建or修改) {
		if (!自定义开关) {
			return;
		}
		this.创建or修改 = 创建or修改;
		if (alert_miocustom_main_edit_name == null) {
			自定义按键菜单布局容器 = LayoutInflater.from(context).inflate(R.layout.alert_miocustom_main, null);
			初始化自定义按键对话框();
			自定义按键对话框 = new AlertDialog.Builder(context)
				.setView(自定义按键菜单布局容器)
				.create();
			自定义按键对话框.setCancelable(false);
			自定义按键对话框.setCanceledOnTouchOutside(false);
		}
		自定义按键对话框.show();
	}
	private String 当前颜色;
	private View 当前view;
	private EditText 当前编辑框;
	private void 颜色选择(View vi, EditText edit) {
		当前view = vi;
		当前编辑框 = edit;
		if (颜色选择对话框 == null) {
			View v=LayoutInflater.from(context).inflate(R.layout.dialog_colorpicker, null);
			ColorPicker colorpicker=v.findViewById(R.id.picker);
			OpacityBar opacityBar =v.findViewById(R.id.opacitybar);
			SVBar svbar=v.findViewById(R.id.svbar);
			colorpicker.addOpacityBar(opacityBar);
			colorpicker.addSVBar(svbar);
			colorpicker.setShowOldCenterColor(false);

			当前颜色 = "#" + Integer.toHexString(colorpicker.getColor());
			colorpicker.setOnColorChangedListener(p1 -> {
				当前颜色 =Integer.toHexString(p1).length()<8?"10ffffff":Integer.toHexString(p1);
				Log.e("当前颜色",当前颜色);
			});
			颜色选择对话框 = new AlertDialog.Builder(context)
				.setTitle("颜色选择")
				.setView(v)
				.setPositiveButton("确定",(dialog, which) -> {
							当前编辑框.setText("#"+当前颜色);
							当前view.setBackgroundColor(Color.parseColor("#"+当前颜色));
						}).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface di, int p2) {

					}
				}).create();
		}
		颜色选择对话框.show();
	}
	private void 初始化自定义按键对话框() {
		alert_miocustom_main_edit_name = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_edit_name);
		alert_miocustom_main_tv_height_left = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_tv_height_left);
		alert_miocustom_main_edit_height = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_edit_height);
		alert_miocustom_main_tv_height_right = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_tv_height_right);
		alert_miocustom_main_tv_width_left = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_tv_width_left);
		alert_miocustom_main_edit_width = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_edit_width);
		alert_miocustom_main_tv_width_right = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_tv_width_right);
		alert_miocustom_main_tv_textsize_left = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_tv_textsize_left);
		alert_miocustom_main_edit_textsize = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_edit_textsize);
		alert_miocustom_main_tv_textsize_right = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_tv_textsize_right);
		alert_miocustom_main_tv_round_left = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_tv_round_left);
		alert_miocustom_main_edit_round = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_edit_round);
		alert_miocustom_main_tv_round_right = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_tv_round_right);
		alert_miocustom_main_edit_strokecolor = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_edit_strokecolor);
		alert_miocustom_main_edit_textcolor = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_edit_textcolor);
		alert_miocustom_main_radio_normalbtn = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_radio_normalbtn);
		alert_miocustom_main_radio_cmdbtn = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_radio_cmdbtn);
		alert_miocustom_main_radio_vsbbtn = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_radio_vsbbtn);
		alert_miocustom_main_layout_normalbtn = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_layout_normalbtn);
		alert_miocustom_main_edit_key1 = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_edit_key1);
		alert_miocustom_main_edit_key2 = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_edit_key2);
		alert_miocustom_main_edit_key3 = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_edit_key3);
		alert_miocustom_main_check_autokeep = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_check_autokeep);
		alert_miocustom_main_check_mousectrl = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_check_mousectrl);
		alert_miocustom_main_layout_cmdbtn = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_layout_cmdbtn);
		alert_miocustom_main_edit_cmd = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_edit_cmd);
		alert_miocustom_main_layout_vsbbtn = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_layout_vsbbtn);
		alert_miocustom_main_btn_btnselect = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_btn_btnselect);
		alert_miocustom_main_btn_ok = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_btn_ok);
		alert_miocustom_main_btn_cancle = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_btn_cancle);
		alert_miocustom_main_view_strokecolor = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_view_strokecolor);
		alert_miocustom_main_view_textcolor = 自定义按键菜单布局容器.findViewById(R.id.alert_miocustom_main_view_textcolor);

		alert_miocustom_main_radio_normalbtn.setOnCheckedChangeListener(this);
		alert_miocustom_main_radio_vsbbtn.setOnCheckedChangeListener(this);
		alert_miocustom_main_radio_cmdbtn.setOnCheckedChangeListener(this);
		alert_miocustom_main_tv_height_left.setOnClickListener(this);
		alert_miocustom_main_tv_height_right.setOnClickListener(this);
		alert_miocustom_main_tv_width_left.setOnClickListener(this);
		alert_miocustom_main_tv_width_right.setOnClickListener(this);
		alert_miocustom_main_tv_textsize_left.setOnClickListener(this);
		alert_miocustom_main_tv_textsize_right.setOnClickListener(this);
		alert_miocustom_main_tv_round_left.setOnClickListener(this);
		alert_miocustom_main_tv_round_right.setOnClickListener(this);
		alert_miocustom_main_view_strokecolor.setOnClickListener(this);
		alert_miocustom_main_view_textcolor.setOnClickListener(this);
		alert_miocustom_main_btn_btnselect.setOnClickListener(this);

		alert_miocustom_main_layout_cmdbtn.setVisibility(View.GONE);
		alert_miocustom_main_layout_vsbbtn.setVisibility(View.GONE);

		alert_miocustom_main_btn_ok.setOnClickListener(this);
		alert_miocustom_main_btn_cancle.setOnClickListener(this);

		alert_miocustom_main_edit_key1.setOnClickListener(this);
		alert_miocustom_main_edit_key2.setOnClickListener(this);
		alert_miocustom_main_edit_key3.setOnClickListener(this);


	}
	private String getText(EditText edit) {
		return edit.getText().toString();
	}
	private void 重置自定义按键对话框() {
		alert_miocustom_main_edit_name.setText("");
		alert_miocustom_main_edit_key1.setText("");
		alert_miocustom_main_edit_key2.setText("");
		alert_miocustom_main_edit_key3.setText("");
		alert_miocustom_main_edit_cmd.setText("");
		alert_miocustom_main_radio_normalbtn.setChecked(true);
		alert_miocustom_main_view_strokecolor.setBackgroundColor(Color.WHITE);
		alert_miocustom_main_view_textcolor.setBackgroundColor(Color.WHITE);
		目标按键标识.clear();
		按键修改模式 = false;
	}

	@Override
	public void onClick(View v) {
		if (v == alert_miocustom_main_tv_height_left) {
			int tmp=Integer.parseInt(getText(alert_miocustom_main_edit_height));
			alert_miocustom_main_edit_height.setText(String.valueOf(tmp -= 1));
		} else if (v == alert_miocustom_main_tv_height_right) {
			int tmp=Integer.parseInt(getText(alert_miocustom_main_edit_height));
			alert_miocustom_main_edit_height.setText(String.valueOf(tmp += 1));
		} else if (v == alert_miocustom_main_tv_width_left) {
			int tmp=Integer.parseInt(getText(alert_miocustom_main_edit_width));
			alert_miocustom_main_edit_width.setText(String.valueOf(tmp -= 1));
		} else if (v == alert_miocustom_main_tv_width_right) {
			int tmp=Integer.parseInt(getText(alert_miocustom_main_edit_width));
			alert_miocustom_main_edit_width.setText(String.valueOf(tmp += 1));
		} else if (v == alert_miocustom_main_tv_textsize_left) {
			int tmp=Integer.parseInt(getText(alert_miocustom_main_edit_textsize));
			alert_miocustom_main_edit_textsize.setText(String.valueOf(tmp -= 1));
		} else if (v == alert_miocustom_main_tv_textsize_right) {
			int tmp=Integer.parseInt(getText(alert_miocustom_main_edit_textsize));
			alert_miocustom_main_edit_textsize.setText(String.valueOf(tmp += 1));
		} else if (v == alert_miocustom_main_tv_round_left) {
			int tmp=Integer.parseInt(getText(alert_miocustom_main_edit_round));
			alert_miocustom_main_edit_textsize.setText(String.valueOf(tmp -= 1));
		} else if (v == alert_miocustom_main_tv_round_right) {
			int tmp=Integer.parseInt(getText(alert_miocustom_main_edit_round));
			alert_miocustom_main_edit_textsize.setText(String.valueOf(tmp += 1));
		} else if (v == alert_miocustom_main_btn_ok) {
			if (!alert_miocustom_main_edit_name.getText().toString().trim().equals("")){
				if (创建or修改) {
					创建按键();
				} else {
					修改按键();
				}
			}else{
				Toast.makeText(context,"属性不能为空。",Toast.LENGTH_LONG).show();
			}
			重置自定义按键对话框();
		} else if (v == alert_miocustom_main_btn_cancle) {
			自定义按键对话框.dismiss();
			重置自定义按键对话框();
		} else if (v == alert_miocustom_main_view_strokecolor) {
			颜色选择(v, alert_miocustom_main_edit_strokecolor);
		} else if (v == alert_miocustom_main_view_textcolor) {
			颜色选择(v, alert_miocustom_main_edit_textcolor);
		} else if (v == alert_miocustom_main_btn_btnselect) {
			自定义按键对话框.dismiss();
			按键选择模式 = true;

			if (当前按键!=null&&当前按键.get按键类型().equals("显隐控制按键") && 当前按键.is默认隐藏()) {
				for (String 标识:当前按键.get目标按键标识()) {
					MioCustomButton b=获取按键(标识);
					if (b!=null){
						b.setTextColor(Color.RED);
						b.setVisibility(View.VISIBLE);
					}
				}
			}
			Toast.makeText(context, "请点击需要隐藏的按键。点击黑色区域退出结束按键选择。", Toast.LENGTH_LONG).show();
		} else if (v == alert_miocustom_main_edit_key1 || v == alert_miocustom_main_edit_key2 || v == alert_miocustom_main_edit_key3) {
			EditText tmpEdit=(EditText)v;
			if (键值选择对话框 == null) {
				键值选择对话框 = new MioDialogSelectKey(context);
			}
			键值选择对话框.setEdit(tmpEdit);
			键值选择对话框.show();
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton v, boolean isChecked) {
		if (isChecked) {
			if (alert_miocustom_main_radio_normalbtn == v) {
				alert_miocustom_main_layout_normalbtn.setVisibility(View.VISIBLE);
				alert_miocustom_main_layout_cmdbtn.setVisibility(View.GONE);
				alert_miocustom_main_layout_vsbbtn.setVisibility(View.GONE);
			} else if (alert_miocustom_main_radio_cmdbtn == v) {
				alert_miocustom_main_layout_normalbtn.setVisibility(View.GONE);
				alert_miocustom_main_layout_cmdbtn.setVisibility(View.VISIBLE);
				alert_miocustom_main_layout_vsbbtn.setVisibility(View.GONE);
			} else if (alert_miocustom_main_radio_vsbbtn == v) {
				alert_miocustom_main_layout_normalbtn.setVisibility(View.GONE);
				alert_miocustom_main_layout_cmdbtn.setVisibility(View.GONE);
				alert_miocustom_main_layout_vsbbtn.setVisibility(View.VISIBLE);
			}
		}

	}
	private int 横坐标偏移,纵坐标偏移;
	private int 触摸点横坐标,触摸点纵坐标;
	private int 上边距,下边距,左边距,右边距;
	private long 按下时间;
	private final Handler 长按计时器=new Handler();
	private final Runnable 长按计时任务=new Runnable(){
		@Override
		public void run() {
			AlertDialog dialog = new AlertDialog.Builder(context)
				.setTitle("警告")
				.setMessage("是否删除该按键?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int which) {
						容器.removeView(当前按键);
						自定义按键集合.remove(当前按键.get唯一标识());
                    }
				})
				.setNegativeButton("取消", null)
				.create();
			dialog.show();
		}
	};
	public boolean 获取按键选择模式(){
		return 按键选择模式;
	}
	public void 设置按键选择模式(boolean a){
		this.按键选择模式=a;
	}
	public boolean 获取按键修改模式(){
		return 按键修改模式;
	}
	public void 设置按键修改模式(boolean a){
		this.按键修改模式=a;
	}
	public MioCustomButton 获取当前按键(){
		return 当前按键;
	}
	public boolean 获取开关状态(){
		return 自定义开关;
	}
	private MioCustomButton 当前按键;
    private float downX,downY;
	int currentPointerID;
	boolean shouldBeDown;
	int lastPointerCount;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO: Implement this method
		if (!按键选择模式) {
			当前按键 = (MioCustomButton)v;
		}
		if (自定义开关 && !按键选择模式) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				触摸点横坐标 = (int)event.getX();
				触摸点纵坐标 = (int)event.getY();
				按下时间 = System.currentTimeMillis();
				长按计时器.postDelayed(长按计时任务, 500);
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				横坐标偏移 = (int)event.getX() - 触摸点横坐标;
				纵坐标偏移 = (int)event.getY() - 触摸点纵坐标;
				左边距 = v.getLeft() + 横坐标偏移;
				上边距 = v.getTop() + 纵坐标偏移;
				右边距 = v.getRight() + 横坐标偏移;
				下边距 = v.getBottom() + 纵坐标偏移;
				RelativeLayout.LayoutParams lParams;
				lParams = (RelativeLayout.LayoutParams)v.getLayoutParams();
				lParams.leftMargin=左边距;
				lParams.topMargin=上边距;
				v.setLayoutParams(lParams);
				当前按键.刷新();
//				v.layout(左边距, 上边距, 右边距, 下边距);
				if (Math.abs(event.getX() - 触摸点横坐标) >= 3  || Math.abs(event.getY() - 触摸点纵坐标) >= 3) {
					长按计时器.removeCallbacks(长按计时任务);
				}

			} else if (event.getAction() == MotionEvent.ACTION_UP||event.getAction()==MotionEvent.ACTION_CANCEL) {
				if ((System.currentTimeMillis() - 按下时间) < 100) {
					自定义按键对话框(false);
					按键修改模式 = true;
					alert_miocustom_main_edit_name.setText(当前按键.get文本());
					alert_miocustom_main_edit_cmd.setText(当前按键.get命令());
					alert_miocustom_main_edit_height.setText("" + 当前按键.get大小().get(0));
					alert_miocustom_main_edit_width.setText("" + 当前按键.get大小().get(1));
					alert_miocustom_main_edit_textsize.setText("" + 当前按键.get文本大小());
					alert_miocustom_main_edit_textcolor.setText(当前按键.get文本颜色());
					alert_miocustom_main_edit_strokecolor.setText(当前按键.get边框颜色());
					alert_miocustom_main_edit_round.setText("" + 当前按键.get圆角半径());
					alert_miocustom_main_check_autokeep.setChecked(当前按键.is按键自动保持());
					alert_miocustom_main_check_mousectrl.setChecked(当前按键.is控制鼠标指针());
					alert_miocustom_main_edit_key1.setText(当前按键.get键值组().get(0));
					alert_miocustom_main_edit_key2.setText(当前按键.get键值组().get(1));
					alert_miocustom_main_edit_key3.setText(当前按键.get键值组().get(2));
					目标按键标识.clear();
					目标按键标识.addAll(当前按键.get目标按键标识());
					if (当前按键.get按键类型().equals(alert_miocustom_main_radio_normalbtn.getHint())) {
						alert_miocustom_main_radio_normalbtn.setChecked(true);
					} else if (当前按键.get按键类型().equals(alert_miocustom_main_radio_cmdbtn.getHint())) {
						alert_miocustom_main_radio_cmdbtn.setChecked(true);
					} else if (当前按键.get按键类型().equals(alert_miocustom_main_radio_vsbbtn.getHint())) {
						alert_miocustom_main_radio_vsbbtn.setChecked(true);
					}
					alert_miocustom_main_view_strokecolor.setBackgroundColor(Color.parseColor(当前按键.get边框颜色()));
					alert_miocustom_main_view_textcolor.setBackgroundColor(Color.parseColor(当前按键.get文本颜色()));

				}
				长按计时器.removeCallbacks(长按计时任务);
				RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)当前按键.getLayoutParams();
				params.setMargins(左边距, 上边距, 右边距, 下边距);
				当前按键.setLayoutParams(params);
			}
		} else if (按键选择模式) {
			MioCustomButton 选择按键=(MioCustomButton)v;
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if (目标按键标识.contains(选择按键.get唯一标识())) {
					目标按键标识.remove(选择按键.get唯一标识());
					选择按键.setTextColor(Color.parseColor(选择按键.get文本颜色()));
					Toast.makeText(context, "已取消选择按键:" + 选择按键.get文本(), Toast.LENGTH_LONG).show();
				} else {
					目标按键标识.add(选择按键.get唯一标识());
					选择按键.setTextColor(Color.RED);
					Toast.makeText(context, "已选择按键:" + 选择按键.get文本(), Toast.LENGTH_LONG).show();
				}
			}
		} else {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if (当前按键.is控制鼠标指针()) {
					currentPointerID = event.getPointerId(0);
					downX = (int) event.getX();
					downY = (int) event.getY();
				}
				if (当前按键.get按键类型().equals("普通按键")) {
					for (String key:当前按键.get键值组()) {
						if (当前按键.is控制鼠标指针()){
							自定义按键回调.按下();
						}
						if (!key.equals("")) {
							int 键值=键值转换器.trans(key);
							if (key.contains("MOUSE")) {
								自定义按键回调.鼠标回调(键值, true);
								Log.d("鼠标",key);
							} else {
								自定义按键回调.键值接收事件(键值, true);
							}
						}
					}
				}

				触摸点横坐标 = (int)event.getX();
				触摸点纵坐标 = (int)event.getY();
				按下时间 = System.currentTimeMillis();
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (当前按键.is控制鼠标指针()) {
					int pointerCount = event.getPointerCount();
					int pointerIndex = event.findPointerIndex(currentPointerID);
					if (pointerIndex == -1 || lastPointerCount != pointerCount || !shouldBeDown) {
						shouldBeDown = true;
						currentPointerID = event.getPointerId(0);
						downX = (int) event.getX();
						downY = (int) event.getY();
					} else {
						int deltaX = (int) (event.getX(pointerIndex) - downX);
						int deltaY = (int) (event.getY(pointerIndex) - downY);
						自定义按键回调.控制鼠标指针移动事件(deltaX, deltaY);
						downX = (int) event.getX(pointerIndex);
						downY = (int) event.getY(pointerIndex);
					}
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP||event.getAction()==MotionEvent.ACTION_CANCEL) {
				if (当前按键.is控制鼠标指针()) {
					shouldBeDown = false;
					currentPointerID = -1;
				}
				if (当前按键.get按键类型().equals("普通按键")) {
					if (当前按键.is控制鼠标指针()){
						自定义按键回调.抬起();
					}
					if (当前按键.is按键自动保持()) {
						if (!当前按键.is正在自动保持()) {
							当前按键.set正在自动保持(true);
						} else {
							当前按键.set正在自动保持(false);
							for (String key:当前按键.get键值组()) {
								if (!key.equals("")) {
									int 键值=键值转换器.trans(key);
									if (key.contains("MOUSE")) {
										自定义按键回调.鼠标回调(键值, false);
									} else {
										自定义按键回调.键值接收事件(键值, false);
									}
								}
							}
						}
					} else {
						for (String key:当前按键.get键值组()) {
							if (!key.equals("")) {
								int 键值=键值转换器.trans(key);
								if (key.contains("MOUSE")) {
									自定义按键回调.鼠标回调(键值, false);
								} else {
									自定义按键回调.键值接收事件(键值, false);
								}
							}
						}
					}

				}

				if ((System.currentTimeMillis() - 按下时间) < 100 && Math.abs(event.getX() - 触摸点横坐标) <= 20 && Math.abs(event.getY() - 触摸点纵坐标) <= 20) {
					if (当前按键.get按键类型().equals("命令按键")) {
						if (自定义按键回调 != null) {
							自定义按键回调.命令接收事件(当前按键.get命令());
						}

					} else if (当前按键.get按键类型().equals("显隐控制按键")) {
						if (获取按键(当前按键.get目标按键标识().get(0)).getVisibility() == View.VISIBLE) {
							for (String 标识:当前按键.get目标按键标识()) {
								try {
									获取按键(标识).setVisibility(View.GONE);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							当前按键.setTextColor(Color.RED);
						} else {
							for (String 标识:当前按键.get目标按键标识()) {
								try {
									获取按键(标识).setVisibility(View.VISIBLE);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							当前按键.setTextColor(Color.parseColor(当前按键.get文本颜色()));
						}
					}
				}
			}

		}
		lastPointerCount = event.getPointerCount();
		return true;
	}
	public MioCustomButton 获取按键(String 标识) {
		try {
			if (自定义按键集合.size() != 0) {
				return 自定义按键集合.get(标识);
			}
		} catch (Exception e) {

		}
		Toast.makeText(context, "未找到相应按键", Toast.LENGTH_LONG).show();
		return new MioCustomButton(context);
	}

	private class MioDeserializer implements JsonDeserializer<MioCustomButton> {
		Context mContext;
		@Override
		public MioCustomButton deserialize(JsonElement json, Type typeOfT,
										   JsonDeserializationContext context) throws JsonParseException {
			MioCustomButton target=new MioCustomButton(mContext);
			GsonBuilder builder=new GsonBuilder();
			builder.excludeFieldsWithoutExposeAnnotation();
			Gson mGson=builder.create();
			MioCustomButton tmp=mGson.fromJson(json, MioCustomButton.class);

			return  合并对象(tmp, target);
		}
		public MioDeserializer(Context context) {
			this.mContext = context;
		}
		private MioCustomButton 合并对象(Object sourceBean, Object targetBean) {
			Class sourceBeanClass = sourceBean.getClass();
			Class targetBeanClass = targetBean.getClass();

			Field[] sourceFields = sourceBeanClass.getDeclaredFields();
			Field[] targetFields = sourceBeanClass.getDeclaredFields();
			for (int i = 0; i < sourceFields.length; i++) {
				Field sourceField = sourceFields[i];
				Field targetField = targetFields[i];
				sourceField.setAccessible(true);
				targetField.setAccessible(true);
				try {
					if (!(sourceField.get(sourceBean) == null)) {
						targetField.set(targetBean, sourceField.get(sourceBean));
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			return (MioCustomButton)targetBean;
		}

	}
}


