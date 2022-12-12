package com.tungsten.fcl.onlytest;

import android.graphics.Rect;
import android.os.Build;
import androidx.annotation.RequiresApi;

import android.util.Log;
import android.view.View;
import android.view.View.OnCapturedPointerListener;
import android.view.MotionEvent;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;

import com.tungsten.fcl.activity.JVMActivity;
import com.tungsten.fcl.onlytest.codes.AndroidKeyMap;
import com.tungsten.fcl.onlytest.codes.Translation;
import com.tungsten.fcl.onlytest.definitions.map.KeyMap;
import com.tungsten.fclauncher.bridge.FCLBridge;


public class MioMouseKeyboard {
    private final JVMActivity context;
	private final View mouseCursor;
	private final Runnable mRunnable;
	private final OnCapturedPointerListener mPointerListener;
	private final AndroidKeyMap androidKeyMap;
	private final Translation translation;
	private final View focusView;

	private FCLBridge fclBridge;
	
	private int baseX,baseY;
	public MioMouseKeyboard(JVMActivity context, View mouse, View focusView){
		this.context=context;
		this.mouseCursor=mouse;
		this.focusView=focusView;
		androidKeyMap=new AndroidKeyMap();
		translation=new Translation(com.tungsten.fcl.onlytest.definitions.id.key.KeyEvent.KEYMAP_TO_X);
		mPointerListener=new OnCapturedPointerListener(){
			@Override
			public boolean onCapturedPointer(View view, MotionEvent event) {
				baseX+=event.getX()*1;
				baseY+=event.getY()*1;
				mouseCursor.setX(baseX);
				mouseCursor.setY(baseY);
				fclBridge.pushEventPointer(baseX,baseY);
				mio(event);
				return true;
			}
		};
		mRunnable=new Runnable(){
			@Override
			public void run() {
				focusView.requestPointerCapture();
			}
		};
		focusView.setOnKeyListener(new OnKeyListener(){
				@Override
				public boolean onKey(View view, int keyCode, KeyEvent event) {
					if(event.getAction()==MotionEvent.ACTION_DOWN){
						if (keyCode==KeyEvent.KEYCODE_ENTER){
//							if(context.cursorMode==FCLBridge.CursorEnabled&&!isSoftShowing()){
//								context.showKeyboard();
//							}else {
//
//							}
						} else if (keyCode==KeyEvent.KEYCODE_BACK){
							fclBridge.pushEventKey(translation.trans((String) androidKeyMap.translate(KeyEvent.KEYCODE_ESCAPE)), 0, true);
						} else {
							fclBridge.pushEventKey(translation.trans((String) androidKeyMap.translate(keyCode)), 0, true);
						}
					}else if(event.getAction()==MotionEvent.ACTION_UP){
						if (keyCode==KeyEvent.KEYCODE_ENTER){

						} else if (keyCode==KeyEvent.KEYCODE_BACK){
							fclBridge.pushEventKey(translation.trans((String) androidKeyMap.translate(KeyEvent.KEYCODE_ESCAPE)), 0, false);
						} else {
							fclBridge.pushEventKey(translation.trans((String)androidKeyMap.translate(keyCode)), 0, false);
						}
					}
					return false;
				}
			});
		focusView.setOnCapturedPointerListener(mPointerListener);

	}
	public void setFCLBridge(FCLBridge fclBridge){
		this.fclBridge=fclBridge;
	}
	private void mio(MotionEvent event){
		if (event.getAction() == MotionEvent.ACTION_BUTTON_PRESS)
		{
			if (event.getActionButton() == MotionEvent.BUTTON_PRIMARY)
			{
				fclBridge.pushEventMouseButton(FCLBridge.Button1, true);
			}
			if(event.getActionButton() == MotionEvent.BUTTON_SECONDARY||event.getActionButton() == MotionEvent.BUTTON_BACK)
			{
				fclBridge.pushEventMouseButton(FCLBridge.Button3, true);
			}
			if(event.getActionButton() == MotionEvent.BUTTON_TERTIARY){
				fclBridge.pushEventMouseButton(FCLBridge.Button2, true);
			}
		}
		if (event.getAction() == MotionEvent.ACTION_BUTTON_RELEASE)
		{
			if (event.getActionButton() == MotionEvent.BUTTON_PRIMARY)
			{
				fclBridge.pushEventMouseButton(FCLBridge.Button1, false);
			}
			if(event.getActionButton() == MotionEvent.BUTTON_SECONDARY||event.getActionButton() == MotionEvent.BUTTON_BACK)
			{
				fclBridge.pushEventMouseButton(FCLBridge.Button3, false);
			}
			if(event.getActionButton() == MotionEvent.BUTTON_TERTIARY){
				fclBridge.pushEventMouseButton(FCLBridge.Button2, false);
			}
		}
		if (event.getActionMasked()==MotionEvent.ACTION_SCROLL){
			if( event.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0.0f){
				fclBridge.pushEventMouseButton(5,true);
				fclBridge.pushEventMouseButton(5,false);
			}
			else{
				fclBridge.pushEventMouseButton(4,true);
				fclBridge.pushEventMouseButton(4,false);
			}

		}
	}
    public void catchPointer(){
		focusView.requestFocus();
		focusView.postDelayed(mRunnable,300);
	}
	public void releasePointer(){
		focusView.releasePointerCapture();
	}

	private boolean isSoftShowing() {
		//获取当屏幕内容的高度
		int screenHeight = context.getWindow().getDecorView().getHeight();
		//获取View可见区域的bottom
		Rect rect = new Rect();
		//DecorView即为activity的顶级view
		context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		//考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
		//选取screenHeight*2/3进行判断
		Log.i("isSoftShowing","screenHigh: " + screenHeight + " rectViewBom : " + rect.bottom );
		return screenHeight * 2 / 3 > rect.bottom;
	}
}
