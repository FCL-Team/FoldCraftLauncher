package com.mio.customcontrol;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.aof.mcinabox.gamecontroller.definitions.map.MouseMap;
import com.mio.mclauncher.customcontrol.R;

public class MioDialogSelectKey extends AlertDialog.Builder implements OnClickListener {
    private final Context context;
	private LinearLayout mMainLayout;
	private LinearLayout mKeyboardLayout;
	private AlertDialog mDialog;
	private Button 鼠标左;
	private Button 鼠标右;
	private Button 鼠标中;
	private Button 滚轮上;
	private Button 滚轮下;
	private Button 确定;
	private Button 取消;
	private TextView 键值;
	private EditText edit;
	private OnClickListener 手动按键事件;
    public MioDialogSelectKey(Context context){
		super(context,R.style.FullDialog);
		this.context=context;
		init();
	}
    public void init(){
		mMainLayout=(LinearLayout)LayoutInflater.from(context).inflate(R.layout.dialog_selectkey,null);
		mKeyboardLayout=mMainLayout.findViewById(R.id.selecter_keyboard);
		for(int i=0;i<mKeyboardLayout.getChildCount();i++){
			if(mKeyboardLayout.getChildAt(i) instanceof LinearLayout){
				LinearLayout t=(LinearLayout)mKeyboardLayout.getChildAt(i);
				for(int j=0;j<t.getChildCount();j++){
					if(t.getChildAt(j) instanceof MioSelectButton){
						t.getChildAt(j).setOnClickListener(this);
					}
				}
			}
		}
		setView(mMainLayout);
		手动按键事件 = v -> {
			if(v==鼠标左){
				键值.setText(MouseMap.MOUSEMAP_BUTTON_LEFT);
			}else if(v==鼠标中){
				键值.setText(MouseMap.MOUSEMAP_BUTTON_MIDDLE);
			}else if(v==鼠标右){
				键值.setText(MouseMap.MOUSEMAP_BUTTON_RIGHT);
			}else if(v==确定){
				edit.setText(键值.getText());
				mDialog.dismiss();
			}else if(v==取消){
				mDialog.dismiss();
			}else if(v==滚轮上){
				键值.setText(MouseMap.MOUSEMAP_WHEEL_UP);
			}else if(v==滚轮下){
				键值.setText(MouseMap.MOUSEMAP_WHEEL_DOWN);
			}
		};
		鼠标左=mMainLayout.findViewById(R.id.dialog_selectkey_mouse_left);
		鼠标右=mMainLayout.findViewById(R.id.dialog_selectkey_mouse_right);
		鼠标中=mMainLayout.findViewById(R.id.dialog_selectkey_mouse_center);
		滚轮上=mMainLayout.findViewById(R.id.dialog_selectkey_mouse_wheel_up);
		滚轮下=mMainLayout.findViewById(R.id.dialog_selectkey_mouse_wheel_down);
		确定=mMainLayout.findViewById(R.id.dialog_selectkey_btn_ok);
		取消=mMainLayout.findViewById(R.id.dialog_selectkey_btn_cancel);
		键值=mMainLayout.findViewById(R.id.dialog_selectkey_text_key);
		鼠标左.setOnClickListener(手动按键事件);
		鼠标中.setOnClickListener(手动按键事件);
		鼠标右.setOnClickListener(手动按键事件);
		滚轮上.setOnClickListener(手动按键事件);
		滚轮下.setOnClickListener(手动按键事件);
		确定.setOnClickListener(手动按键事件);
		取消.setOnClickListener(手动按键事件);
		mDialog= super.create();
		mDialog.setCancelable(false);
		mDialog.setCanceledOnTouchOutside(false);
	}
	public void setEdit(EditText edit){
		this.edit=edit;
	}

	@Override
	public AlertDialog show() {
		mDialog.show();
		return null;
	}

	public void dismiss(){
		mDialog.dismiss();
	}
	
	@Override
	public void onClick(View p1)
	{
		MioSelectButton mBtn=(MioSelectButton)p1;
		键值.setText(mBtn.getKey());
	}
}
