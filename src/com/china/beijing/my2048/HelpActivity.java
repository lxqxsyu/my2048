package com.china.beijing.my2048;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

public class HelpActivity extends Activity{
	private ViewFlipper heroList;
	private ImageView heroArrowLeft;
	private ImageView heroArrowRight;
	private ViewFlipper zhuangbeiList;
	private ImageView zhuangbeiArrowLeft;
	private ImageView zhuangbeiArrowRight;
	private int[] drawables = {
		R.drawable.lol_0,
		R.drawable.lol_1,	
		R.drawable.lol_2,
		R.drawable.lol_3,
		R.drawable.lol_4,
		R.drawable.lol_5,
		R.drawable.lol_6,
		R.drawable.lol_7,
		R.drawable.lol_8,
		R.drawable.lol_9,
		R.drawable.lol_10,
		R.drawable.lol_11,
	};
	
	private int[] skin3 = {
			R.drawable.lol_0,
			R.drawable.lol_zb_1,	
			R.drawable.lol_zb_2,
			R.drawable.lol_zb_3,
			R.drawable.lol_zb_4,
			R.drawable.lol_zb_5,
			R.drawable.lol_zb_6,
			R.drawable.lol_zb_7,
			R.drawable.lol_zb_8,
			R.drawable.lol_zb_9,
			R.drawable.lol_zb_10,
			R.drawable.lol_zb_11,
		};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		heroList = (ViewFlipper)findViewById(R.id.lol_hero_list);
		zhuangbeiList = (ViewFlipper)findViewById(R.id.lol_zhuangbei_list);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		for(int i=0; i<drawables.length; i++){
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(params);
			iv.setImageResource(drawables[i]);
			iv.setScaleType(ImageView.ScaleType.FIT_XY);
			heroList.addView(iv);
		}
		for(int i=0; i<skin3.length; i++){
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(params);
			iv.setImageResource(skin3[i]);
			zhuangbeiList.addView(iv);
		}
		heroArrowLeft = (ImageView) findViewById(R.id.hero_arrow_left);
		heroArrowRight = (ImageView) findViewById(R.id.hero_arrow_right);
		zhuangbeiArrowLeft = (ImageView) findViewById(R.id.zhuangbei_arrow_left);
		zhuangbeiArrowRight = (ImageView) findViewById(R.id.zhuangbei_arrow_right);
		heroArrowLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				heroList.showPrevious();
			}
		});
		
		heroArrowRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				heroList.showNext();
			}
		});
		zhuangbeiArrowLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				zhuangbeiList.showPrevious();
			}
		});
		
		zhuangbeiArrowRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				zhuangbeiList.showNext();
			}
		});
	}
}
