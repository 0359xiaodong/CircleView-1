package com.android.cricle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.Toast;

public class Circleview extends ImageView implements Runnable {

	private Bitmap mHourBitmap;

	private boolean bInitComplete = false;
	private boolean stopRoter = true;
	float Angel = 0.0f;
	Matrix matx = new Matrix();
	/**
	 * 中奖各种计算参数 maxAngel=转动到中奖的角度
	 */

	float maxAngel = 0.0f;
	/**
	 * 屏幕的宽度
	 */

	int screnWidth = 0;

	private int place = 0;

	private Context context;

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	/**
	 * 初始抽奖滑动组件
	 * 
	 * @param context
	 * @param width
	 * 屏幕宽度
	 */

	public Circleview(Context context, int width) {
		super(context);
		this.context = context;
		this.screnWidth = width;
		init();
		new Thread(this).start();
	}

	public void init() {
		mHourBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.rotate_static);
		
		bInitComplete = true;
	}

	public void setRotate_degree(float degree) {
		Angel = degree;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		matx.reset();
		canvas.drawColor(Color.TRANSPARENT);
		if (!bInitComplete) {
			return;
		}
		Paint localPaint = new Paint();
		// 设置取消锯齿效果
		localPaint.setAntiAlias(true);
		localPaint.setFilterBitmap(true);
		/**
		 * 初始中间指针
		 */

		matx.setTranslate(
				this.screnWidth / 2 - mHourBitmap.getWidth() / 2,
				DensityUtil.dip2px(getContext(), 300) / 2
						- mHourBitmap.getHeight() * 4 / 7
						+ DensityUtil.dip2px(getContext(), 20));
		/**
		 * 设置绕点旋转
		 */

		matx.preRotate(Angel, mHourBitmap.getWidth() / 2,
				mHourBitmap.getHeight() * 2 / 5);
		canvas.drawBitmap(mHourBitmap, matx, localPaint);
	}

	public void run() {
		try {
			while (true) {
				if (!isStopRoter()) {
					if (maxAngel != 0 && Angel >= maxAngel) {
						setStopRoter(true);
						maxAngel = 0.0f;
						switch (place) {
						case 1:
							System.out.println("iphone");
							break;
						case 2:
							System.out.println("diy");
							break;
						case 3:
							System.out.println("double");
							break;
						case 4:
							System.out.println("single");
							break;
						case 5:
							System.out.println("three");
							break;
						case 6:
							System.out.println("copou");
							break;
						default:
							break;
						}
					} else {
						if (maxAngel - Angel < 360)
							setRotate_degree(Angel += 10);
						else
							setRotate_degree(Angel += 15);
						this.postInvalidate();
						Thread.sleep(50);
					}
				}
			}

		} catch (InterruptedException e) {

			e.printStackTrace();
		}

	}

	/**
	 * 获取当前的角度，并设置停止角度
	 * 
	 * @param palce
	 *            位置
	 * @return
	 */
	public void setStopPlace(int place) {
		getRoterByPlace(place);
	}

	/**
	 * 顺时针旋转 1 = 330-30 2 = 30-90 3 = 90-150 4 = 150-210 5 = 210-270 6 = 270-330
	 * 
	 * @param place
	 * @return
	 */

	void getRoterByPlace(int place) {
		float roter = getRoteCenter(place);
		float currentRoter = getCurrentRoter();

		// 如果当前的角度小于位置的角度，则表示需要多转多少角度
		float difRoter = currentRoter - roter;
		// 固定三圈360*3，后在加上当前的角度差
		maxAngel = Angel + 360 * 3 - difRoter;
	}

	/**
	 * 得到奖项位置的角度 -转盘360度 根据奖项取各个奖项的平均值，在设置指定各个奖项的中间点
	 * 
	 * @param place
	 * @return
	 */

	float getRoteCenter(int place) {
		float roter = 0.0f;
		switch (place) {
		case 0:
			roter = 23;
			break;
		case 1:
			roter = 23;
			break;
		case 2:
			roter = 23 + 45;
			break;
		case 3:
			roter = 23 + 90;
			break;
		case 4:
			roter = 23 + 135;
			break;
		case 5:
			roter = 23 + 180;
			break;
		case 6:
			roter = 23 + 225;
			break;
		case 7:
			roter = 23 + 270;
			break;
		case 8:
			roter = 23 + 315;
			break;
		default:
			break;
		}
		return roter;
	}
	/**
	 * 得到转动的实际角度--换算角度值
	 * 
	 * @return
	 */

	float getCurrentRoter() {
		int current = (int) Angel / 360;
		if (0 == current)
			return Angel;
		float roter = Angel - 360 * current;
		return roter;
	}

	public boolean isStopRoter() {
		return stopRoter;
	}

	public void setStopRoter(boolean stopRoter) {
		this.stopRoter = stopRoter;
	}
}