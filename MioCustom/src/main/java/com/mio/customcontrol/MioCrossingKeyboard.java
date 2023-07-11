package com.mio.customcontrol;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.graphics.drawable.GradientDrawable;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;

public class MioCrossingKeyboard extends RelativeLayout implements OnTouchListener {

	private MioListener mListener;
	private LinearLayout main;
	private Button touchPad;
	private final Context context;
	private LinearLayout layout_up;
	private Button btn_leftUp,
	btn_up,
	btn_rightUp;

	private LinearLayout layout_center;
	private Button btn_left,
	btn_center,
	btn_right;

	private LinearLayout layout_down;
	private Button btn_leftDown,
	btn_down,
	btn_rightDown;

	private boolean fromCenter=false;
	private boolean fromUp=false;
	public MioCrossingKeyboard(Context context) {
		super(context);
		this.context = context;
		init();
		hideBtn();
	}
	public MioCrossingKeyboard(Context context,AttributeSet attr) {
		super(context, attr);
		this.context = context;
		init();
		hideBtn();
	}
	public MioCrossingKeyboard(Context context,AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
		this.context = context;
		init();
		hideBtn();
	}
	public void setListener(MioListener mListener) {
		this.mListener = mListener;
	}
	private void init() {
		main = new LinearLayout(context);
		main.setOrientation(LinearLayout.VERTICAL);
		layout_up = new LinearLayout(context);
		layout_up.setOrientation(LinearLayout.HORIZONTAL);
		layout_center = new LinearLayout(context);
		layout_center.setOrientation(LinearLayout.HORIZONTAL);
		layout_down = new LinearLayout(context);
		layout_down.setOrientation(LinearLayout.HORIZONTAL);

		btn_leftUp = createButton("◤", 25);
		btn_up = createButton("▲", 32);
		btn_rightUp = createButton("◥", 25);

		btn_left = createButton("◀", 32);
		btn_center = createButton("◆", 32);
		btn_right = createButton("▶", 32);

		btn_leftDown = createButton("◣", 25);
		btn_down = createButton("▼", 32);
		btn_rightDown = createButton("◢", 25);

		layout_up.addView(btn_leftUp);
		layout_up.addView(btn_up);
		layout_up.addView(btn_rightUp);

		layout_center.addView(btn_left);
		layout_center.addView(btn_center);
		layout_center.addView(btn_right);

		layout_down.addView(btn_leftDown);
		layout_down.addView(btn_down);
		layout_down.addView(btn_rightDown);
		LinearLayout.LayoutParams params1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
																		LinearLayout.LayoutParams.MATCH_PARENT,
																		1.0f);
		main.addView(layout_up, params1);
		main.addView(layout_center, params1);
		main.addView(layout_down, params1);
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
		                                                               LinearLayout.LayoutParams.MATCH_PARENT);
		this.addView(main, params);
		touchPad = new Button(context);
		touchPad.setOnTouchListener(this);
		touchPad.setAlpha(0);
		this.addView(touchPad, params);

	}
	private Button createButton(String text, float textSize) {
		Button b=new Button(context);
		b.setText(text);
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
		                                                               LinearLayout.LayoutParams.MATCH_PARENT,
																	   1.0f);
		b.setLayoutParams(params);
		b.setTextSize(textSize);
		b.setTextColor(Color.WHITE);
		GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(Color.parseColor("#00FFFFFF"));
        drawable.setCornerRadius(25);
        drawable.setStroke(1, Color.WHITE);
		b.setBackground(drawable);
		b.setFocusable(false);
		b.setEnabled(false);
		return b;
	}
	public void 自定义(){
		自定义= !自定义;
	}

	/**
	 * 计算两个点的距离
	 *
	 * @param event 事件
	 * @return 返回数值
	 */
	private double spacing(MotionEvent event) {
		if (event.getPointerCount() == 2) {
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return Math.sqrt(x * x + y * y);
		} else {
			return 0;
		}
	}


	/**
	 * 设置放大缩小
	 *
	 * @param scale 缩放值
	 */
	public void setScale(float scale) {
		setScaleX(scale);
		setScaleY(scale);
		mScale=scale;
	}
	//最大的缩放比例
	public float mScale;
	public static final float SCALE_MAX = 3.0f;
	public static final float SCALE_MIN = 0.5f;

	private double oldDist = 0;
	private double moveDist = 0;

	private TouchInfo mInfo;
	private int lastPos=0;
	private final int LEFT_UP=1,UP=2,RIGHT_UP=3;
	private final int LEFT=4,CENTER=5,RIGHT=6;
	private final int LEFT_DOWN=7,DOWN=8,RIGHT_DOWN=9;
	private boolean 自定义=false;

	private int 触摸点横坐标;

	private int 触摸点纵坐标;

	private int 横坐标偏移;

	private int 纵坐标偏移;

	private int 左边距;

	private int 上边距;

	private int 右边距;

	private int 下边距;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (自定义){
			if (event.getPointerCount()==1){
				if (event.getAction()==MotionEvent.ACTION_DOWN){
					触摸点横坐标 = (int)event.getX();
					触摸点纵坐标 = (int)event.getY();
				}else if (event.getAction()==MotionEvent.ACTION_MOVE) {
					ViewGroup mViewGroup = (ViewGroup) getParent();
					横坐标偏移 = (int)event.getX() - 触摸点横坐标;
					纵坐标偏移 = (int)event.getY() - 触摸点纵坐标;
					左边距 = this.getLeft() + 横坐标偏移;
					上边距 = this.getTop() +  纵坐标偏移;
					右边距 = this.getRight() + 横坐标偏移;
					下边距 = mViewGroup.getHeight() - this.getBottom() - 纵坐标偏移;
					this.layout(左边距, 上边距, 右边距, 下边距);

					RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)this.getLayoutParams();
					params.leftMargin=左边距;
					params.bottomMargin=下边距;
//				params.setMargins(左边距, 上边距, 右边距, 下边距);
					this.setLayoutParams(params);
				}else if (event.getAction()==MotionEvent.ACTION_UP){

				}
			}else if (event.getPointerCount()==2){
				switch (event.getActionMasked()){
					case MotionEvent.ACTION_MOVE:
						moveDist = spacing(event);
						double space = moveDist - oldDist;
						float scale = (float) (getScaleX() + space / getWidth());
						if (scale > SCALE_MIN && scale < SCALE_MAX) {
							setScale(scale);
						} else if (scale < SCALE_MIN) {
							setScale(SCALE_MIN);
						}
						break;
					case MotionEvent.ACTION_POINTER_DOWN:
						//两点按下时的距离
						oldDist = spacing(event);
						break;
					default:
						break;
				}
			}


		}else {
			if (mInfo == null) {
				mInfo = new TouchInfo();
				mInfo.setLayoutWidth(v.getWidth());
				mInfo.setLayoutHeight(v.getHeight());
			}
			mInfo.setFingerX(event.getX() - v.getLeft());
			mInfo.setFingerY(event.getY() - v.getTop());
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				mInfo.setDownX(event.getX() - v.getLeft());
				mInfo.setDownY(event.getY() - v.getTop());
			}
			if (mInfo.getFingerY() < mInfo.getLayoutHeight() / 3) {
				if (mInfo.getFingerX() < mInfo.getLayoutWidth() / 3) {
					if (fromCenter) {
						if (event.getAction() == MotionEvent.ACTION_UP) {
							if (Math.abs(mInfo.getDownX() - mInfo.getFingerX()) > mInfo.getLayoutWidth() / 2 && Math.abs(mInfo.getDownY() - mInfo.getFingerY()) > mInfo.getLayoutHeight() / 2) {
								mListener.onSlipLeftUp();
							}
						}
					} else {
						showBtn(btn_leftUp);
						if (mListener != null&&lastPos!=LEFT_UP) {
							mListener.onLeftUp();
							lastPos=LEFT_UP;
						}
					}
				} else {
					if (mInfo.getFingerX() < mInfo.getLayoutWidth() * 2 / 3) {
						if (fromCenter) {
							if (event.getAction() == MotionEvent.ACTION_UP) {
								if (Math.abs(mInfo.getDownX() - mInfo.getFingerX()) < mInfo.getLayoutWidth() / 2 && Math.abs(mInfo.getDownY() - mInfo.getFingerY()) > mInfo.getLayoutHeight() / 2) {
									mListener.onSlipUp();
								}
							}
						} else {
							fromUp=true;
							hideBtn();
							btn_leftUp.setAlpha(1);
							btn_rightUp.setAlpha(1);
							if (mListener != null&&lastPos!=UP) {
								mListener.onUp();
								lastPos=UP;
							}
						}
					} else {
						if (fromCenter) {
							if (event.getAction() == MotionEvent.ACTION_UP) {
								if (Math.abs(mInfo.getDownX() - mInfo.getFingerX()) > mInfo.getLayoutWidth() / 2 && Math.abs(mInfo.getDownY() - mInfo.getFingerY()) > mInfo.getLayoutHeight() / 2) {
									mListener.onSlipRightUp();
								}
							}
						}else if (mInfo.getFingerX() < mInfo.getLayoutWidth()) {
							showBtn(btn_rightUp);
							if (mListener != null&&lastPos!=RIGHT_UP) {
								mListener.onRightUp();
								lastPos=RIGHT_UP;
							}
						}
					}
				}
			} else {
				if (mInfo.getFingerY() < mInfo.getLayoutHeight() * 2 / 3) {
					if (mInfo.getFingerX() < mInfo.getLayoutWidth() / 3) {
						if (fromCenter) {
							if (event.getAction() == MotionEvent.ACTION_UP) {
								if (Math.abs(mInfo.getDownX() - mInfo.getFingerX()) > mInfo.getLayoutWidth() / 2 && Math.abs(mInfo.getDownY() - mInfo.getFingerY()) < mInfo.getLayoutHeight() / 2) {
									mListener.onSlipLeft();
								}
							}
						} else {
							hideBtn();
							btn_leftUp.setAlpha(1);
							btn_leftDown.setAlpha(1);
							if (mListener != null&&lastPos!=LEFT) {
								mListener.onLeft();
								lastPos=LEFT;
							}
						}
					} else {
						if (mInfo.getFingerX() < mInfo.getLayoutWidth() * 2 / 3) {
							if(fromUp&&lastPos!=CENTER){
								mListener.onUpToCenter();
							}
							hideBtn();
							if (mListener != null&&lastPos!=CENTER) {
								mListener.onCenter(lastPos == 0);
								lastPos=CENTER;
							}
							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								fromCenter = true;
							}
						} else {
							if (fromCenter) {
								if (event.getAction() == MotionEvent.ACTION_UP) {
									if (Math.abs(mInfo.getFingerX() - mInfo.getDownX()) > mInfo.getLayoutWidth() / 2 && Math.abs(mInfo.getDownY() - mInfo.getFingerY()) < mInfo.getLayoutHeight() / 2) {
										mListener.onSlipRight();
									}
								}
							}else if (mInfo.getFingerX() < mInfo.getLayoutWidth()) {
								hideBtn();
								btn_rightDown.setAlpha(1);
								btn_rightUp.setAlpha(1);
								if (mListener != null&&lastPos!=RIGHT) {
									mListener.onRight();
									lastPos=RIGHT;
								}
							}
						}
					}
				} else {
					if (mInfo.getFingerX() < mInfo.getLayoutWidth() / 3) {
						if (fromCenter) {
							if (event.getAction() == MotionEvent.ACTION_UP) {
								if (Math.abs(mInfo.getDownX() - mInfo.getFingerX()) > mInfo.getLayoutWidth() / 2 && Math.abs(mInfo.getDownY() - mInfo.getFingerY()) > mInfo.getLayoutHeight() / 2) {
									mListener.onSlipLeftDown();
								}
							}
						} else {
							showBtn(btn_leftDown);
							if (mListener != null&&lastPos!=LEFT_DOWN) {
								mListener.onLeftDown();
								lastPos=LEFT_DOWN;
							}
						}
					} else {
						if (mInfo.getFingerX() < mInfo.getLayoutWidth() * 2 / 3) {
							if (fromCenter) {
								if (event.getAction() == MotionEvent.ACTION_UP) {
									if (Math.abs(mInfo.getDownX() - mInfo.getFingerX()) < mInfo.getLayoutWidth() / 2 && Math.abs(mInfo.getDownY() - mInfo.getFingerY()) > mInfo.getLayoutHeight() / 2) {
										mListener.onSlipDown();
									}
								}
							}else{
								hideBtn();
								btn_leftDown.setAlpha(1);
								btn_rightDown.setAlpha(1);
								if (mListener != null&&lastPos!=DOWN) {
									lastPos=DOWN;
									mListener.onDown();
								}
							}
						} else {
							if (fromCenter) {
								if (event.getAction() == MotionEvent.ACTION_UP) {
									if (Math.abs(mInfo.getDownX() - mInfo.getFingerX()) > mInfo.getLayoutWidth() / 2 && Math.abs(mInfo.getDownY() - mInfo.getFingerY()) > mInfo.getLayoutHeight() / 2) {
										mListener.onSlipRightDown();
									}
								}
							}else if (mInfo.getFingerX() < mInfo.getLayoutWidth()) {
								showBtn(btn_rightDown);
								if (mListener != null&&lastPos!=RIGHT_DOWN) {
									mListener.onRightDown();
									lastPos=RIGHT_DOWN;
								}
							}
						}
					}
				}

			}
			if (event.getAction() == MotionEvent.ACTION_DOWN) {

			}
			if (event.getAction() == MotionEvent.ACTION_MOVE) {

			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				hideBtn();
				fromCenter = false;
				fromUp=false;
				mListener.onFingerUp();
				lastPos=0;
			}
			if(event.getAction()==MotionEvent.ACTION_CANCEL){
				hideBtn();
				fromCenter = false;
				fromUp=false;
				mListener.onFingerUp();
				lastPos=0;
			}
		}

		return true;
	}
	private List<Button> btnList;
	private void showBtn(Button b) {
		if (btnList == null) {
			btnList = new ArrayList<Button>();
			btnList.add(btn_leftUp);
			btnList.add(btn_leftDown);
			btnList.add(btn_rightUp);
			btnList.add(btn_rightDown);
		}
		for (Button bt:btnList) {
			if (bt != b) {
				bt.setAlpha(0);
			} else {
				bt.setAlpha(1);
			}
		}
	}
	private void hideBtn() {
		if (btnList == null) {
			btnList = new ArrayList<Button>();
			btnList.add(btn_leftUp);
			btnList.add(btn_leftDown);
			btnList.add(btn_rightUp);
			btnList.add(btn_rightDown);
		}
		for (Button bt:btnList) {
			bt.setAlpha(0);
		}
	}
	private class TouchInfo {
		private int layoutWidth;
		private int layoutHeight;
		private float fingerX;
		private float fingerY;

		private float downX;
		private float downY;

		public void setDownX(float downX) {
			this.downX = downX;
		}

		public float getDownX() {
			return downX;
		}

		public void setDownY(float downY) {
			this.downY = downY;
		}

		public float getDownY() {
			return downY;
		}


		public void setLayoutWidth(int layoutWidth) {
			this.layoutWidth = layoutWidth;
		}

		public int getLayoutWidth() {
			return layoutWidth;
		}

		public void setLayoutHeight(int layoutHeight) {
			this.layoutHeight = layoutHeight;
		}

		public int getLayoutHeight() {
			return layoutHeight;
		}

		public void setFingerX(float fingerX) {
			this.fingerX = fingerX;
		}

		public float getFingerX() {
			return fingerX;
		}

		public void setFingerY(float fingerY) {
			this.fingerY = fingerY;
		}

		public float getFingerY() {
			return fingerY;
		}
	}
	public interface MioListener {
		void onLeftUp();
		void onUp();
		void onRightUp();
		void onLeft();
		void onCenter(boolean direct);
		void onRight();
		void onLeftDown();
		void onDown();
		void onRightDown();

		void onSlipLeftUp();
		void onSlipUp();
		void onSlipRightUp();
		void onSlipLeft();
		void onSlipRight();
		void onSlipLeftDown();
		void onSlipDown();
		void onSlipRightDown();
		
		void onUpToCenter();
        void onFingerUp();
	}
}
