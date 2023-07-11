package com.mio.customcontrol;
 
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.Button;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;

public class MioCustomButton extends Button
{
	
	@Expose
	private String 唯一标识;

	@Expose
	private String 文本;

	@Expose
	private String 按键类型;//显隐控制按键 命令按键 普通按键

	@Expose
	private List<String> 键值组;

	@Expose
	private int 文本大小;

	@Expose
	private List<Float> 位置;

	@Expose
	private List<Integer> 大小;

	@Expose
	private String 文本颜色;

	@Expose
	private int 圆角半径;

	@Expose
	private String 边框颜色;

	@Expose
	private boolean 按键自动保持;
	
	@Expose
	private boolean 默认隐藏;

	@Expose
	private boolean 控制鼠标指针;

	@Expose
	private String 命令;

	@Expose
	private List<String> 目标按键标识;
	
	private boolean 正在自动保持=false;
	
	private final Context context;

	public MioCustomButton(Context context){
		super(context);
		this.context=context;
	}

	public void 初始化(){
		setText(文本);
		setTextSize(文本大小);
		setTextColor(Color.parseColor(文本颜色));
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(Color.parseColor("#00FFFFFF"));
        drawable.setCornerRadius(圆角半径);
        drawable.setStroke(1, Color.parseColor(边框颜色));
		setBackground(drawable);
		MioCustomUtils.setViewSize(context,this,大小.get(0),大小.get(1));
		setAllCaps(false);
		setX(MioCustomUtils.percentTopx(位置.get(0),MioCustomManager.scrennWidth));
		setY(MioCustomUtils.percentTopx(位置.get(1),MioCustomManager.screenHeight));
	}
	
	
	public void 刷新(){
		List<Float> 位置=new ArrayList<>();
		位置.add(MioCustomUtils.pxToPercent(this.getX(),MioCustomManager.scrennWidth));
		位置.add(MioCustomUtils.pxToPercent(this.getY(),MioCustomManager.screenHeight));
		this.位置.clear();
		this.位置.addAll(位置);
	}
	
	public void set唯一标识(String 唯一标识) {
		this.唯一标识 = 唯一标识;
	}

	public String get唯一标识() {
		return 唯一标识;
	}

	public void set文本(String 文本) {
		this.文本 = 文本;
	}

	public String get文本() {
		return 文本;
	}

	public void set按键类型(String 按键类型) {
		this.按键类型 = 按键类型;
	}

	public String get按键类型() {
		return 按键类型;
	}

	public void set键值组(List<String> 键值组) {
		this.键值组 = 键值组;
	}

	public List<String> get键值组() {
		return 键值组;
	}

	public void set文本大小(int 文本大小) {
		this.文本大小 = 文本大小;
	}

	public int get文本大小() {
		return 文本大小;
	}

	public void set位置(List<Float> 位置) {
		this.位置 = 位置;
	}

		public List<Float> get位置() {
		return 位置;
	}

	public void set大小(List<Integer> 大小) {
		this.大小 = 大小;
	}

	public List<Integer> get大小() {
		return 大小;
	}

	public void set文本颜色(String 文本颜色) {
		this.文本颜色 = 文本颜色;
	}

	public String get文本颜色() {
		return 文本颜色;
	}

	public void set圆角半径(int 圆角半径) {
		this.圆角半径 = 圆角半径;
	}

	public int get圆角半径() {
		return 圆角半径;
	}

	public void set边框颜色(String 边框颜色) {
		this.边框颜色 = 边框颜色;
	}

	public String get边框颜色() {
		return 边框颜色;
	}
	
	public void set按键自动保持(boolean 按键自动保持) {
		this.按键自动保持 = 按键自动保持;
	}

	public boolean is按键自动保持() {
		return 按键自动保持;
	}

	public void set控制鼠标指针(boolean 控制鼠标指针) {
		this.控制鼠标指针 = 控制鼠标指针;
	}

	public boolean is控制鼠标指针() {
		return 控制鼠标指针;
	}

	public void set命令(String 命令) {
		this.命令 = 命令;
	}

	public String get命令() {
		return 命令;
	}

	public void set目标按键标识(List<String> 目标按键标识) {
		this.目标按键标识 = 目标按键标识;
	}

	public List<String> get目标按键标识() {
		return 目标按键标识;
	}
	
	public void set默认隐藏(boolean 默认隐藏) {
		this.默认隐藏 = 默认隐藏;
	}

	public boolean is默认隐藏() {
		return 默认隐藏;
	}

	public void set正在自动保持(boolean 正在自动保持) {
		this.正在自动保持 = 正在自动保持;
		if(正在自动保持){
			this.setTextColor(Color.RED);
		}else{
			this.setTextColor(Color.parseColor(文本颜色));
		}
	}

	@Override
	public boolean performClick() {
		return true;
	}

	public boolean is正在自动保持() {
		return 正在自动保持;
	}
	@Override
	public String toString() {
		return "["+getClass().getCanonicalName()+"]"
			+ "\n唯一标识=" + 唯一标识
			+ ",\n文本=" + 文本
			+ ",\n按键类型=" + 按键类型
			+ ",\n键值组=" + 键值组
			+ ",\n文本大小=" + 文本大小
			+ ",\n位置=" + 位置.get(0)
			+ ",\n大小=" + 大小
			+ ",\n文本颜色=" + 文本颜色
			+ ",\n圆角半径=" + 圆角半径
			+ ",\n边框颜色=" + 边框颜色
			+ ",\n按键自动保持=" + 按键自动保持
			+ ",\n控制鼠标指针=" + 控制鼠标指针
			+ ",\n命令=" + 命令
			+ ",\n目标按键标识=" + 目标按键标识;
	}
}
