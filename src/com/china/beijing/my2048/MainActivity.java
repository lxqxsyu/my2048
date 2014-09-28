package com.china.beijing.my2048;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.china.beijing.my2048.My2048View.GameChangeListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.umeng.update.UmengUpdateAgent;
import com.xunyou.ygxq.push.uti.MyPushManager;

public class MainActivity extends Activity {
	private TextView scoreText;
	private TextView maxScoreText;
	private ImageView soundButton;
	private ImageView shareButton;
	private My2048View my2048View;
	private SharedPreferences sharedPreference;
	private boolean soundOpened;
	private static final String DATA_NAME = "my2048Data";
	private UMSocialService mController;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MyPushManager manager = new MyPushManager();
        manager.startService(this, 1);
		//���˸���
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
		sharedPreference = getSharedPreferences("my2048", MODE_PRIVATE);
		soundOpened = sharedPreference.getBoolean("soundOpend", false);
		scoreText = (TextView) findViewById(R.id.score);
		maxScoreText = (TextView) findViewById(R.id.maxScore);
		my2048View = (My2048View) findViewById(R.id.my2048view);
		
		soundButton = (ImageView) findViewById(R.id.sound);
		shareButton = (ImageView) findViewById(R.id.share);
		my2048View.setSoundState(soundOpened);
		if(soundOpened){
			soundButton.setImageResource(R.drawable.sound_opend);
		}else{
			soundButton.setImageResource(R.drawable.sound_closed);
		}
		soundButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Editor edit = sharedPreference.edit();
				edit.putBoolean("soundOpend", !soundOpened);
				edit.commit();
				soundOpened = !soundOpened;
				my2048View.setSoundState(soundOpened);
				if(soundOpened){
					soundButton.setImageResource(R.drawable.sound_opend);
				}else{
					soundButton.setImageResource(R.drawable.sound_closed);
				}
			}
		});
		shareButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				/*my2048View.saveMaxScore();
				Intent intent=new Intent(Intent.ACTION_SEND);    
                intent.setType("image/*");    
                intent.putExtra(Intent.EXTRA_SUBJECT, "LOL��2048"); 
               // intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File("android:resource://com.example.my2048/"+R.raw.share_img)));
                intent.putExtra(Intent.EXTRA_TEXT, "�ҵ���߼�¼��"+ sharedPreference.getInt("maxScore", 0) +"�֣��Ͽ�����ɣ�LOL��2048���棡��ľ��~~����ȥ������http://blog.csdn.net/dawanganban/article/details/37863693");            
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
                startActivity(Intent.createChooser(intent, getTitle()));    */
				// �Ƿ�ֻ���ѵ�¼�û����ܴ򿪷���ѡ��ҳ
				my2048View.saveMaxScore();
		        mController.openShare(MainActivity.this, false);
			}
		});
		my2048View.setOnGameChangeListener(new GameChangeListener() {
			
			@Override
			public void onChangedScore(int score) {
				scoreText.setText(score + "");
			}
			
			@Override
			public void onChangedGameOver(int score, int maxScore) {
				scoreText.setText(score + "");
				maxScoreText.setText(maxScore + "");
			}
		});
		
		if(savedInstanceState != null){
			Toast.makeText(this, "saveInstanceNotNull", 2000).show();
			Bundle map = savedInstanceState.getBundle(DATA_NAME);
			if(map != null){
				Toast.makeText(this, "mapNotNull", 2000).show();
				my2048View.restoreDataAndState(map);
			}
		}
		
		initShareConfig();
	}
	

	

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBundle(DATA_NAME, my2048View.saveDataAndState());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflator = new MenuInflater(this);
		inflator.inflate(R.menu.actionbar, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int skin = 1;
		switch (item.getItemId()) {
		case R.id.item1:
			changeSkin(0);
			break;
		case R.id.item2:
			changeSkin(1);
			break;
		case R.id.item3:
			changeSkin(2);
			break;
		case R.id.game_help:
			Intent intent = new Intent(MainActivity.this, HelpActivity.class);
			startActivity(intent);
			break;
		case R.id.item4:
			changeSound(0);
			break;
		case R.id.item5:
			changeSound(1);
			break;
		default:
			return super.onMenuItemSelected(featureId, item);
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void changeSound(int sound) {
		Editor edit = sharedPreference.edit();
		edit.putInt("sound", 0);
		edit.commit();
		my2048View.changeSound(sound);
	}

	private void changeSkin(int skin) {
		Editor edit = sharedPreference.edit();
		edit.putInt("skin", skin);
		edit.commit();
		my2048View.changeSkin(skin);
	}
	
	private void initShareConfig(){
		String title = "�����������Ϸ";
		String url = "http://m.anzhi.com/info_1747811.html";
		String img_url =  "http://img.my.csdn.net/uploads/201407/24/1406207564_9598.png";
		String message = "�ۣ��ҵ���߼�¼��" + sharedPreference.getInt("maxScore", 0) + "�֣�������һ�����ȫ���������Ϸȫ�¸İ棬������2048�����ˣ�����Ƥ������Ч����ѡ������LOLƤ������Ч�����������ɣ�" + url;
		// ����������Activity��������³�Ա����
		mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);
		
		mController.setAppWebSite(SHARE_MEDIA.RENREN, url);
		
		String appId = "wx6c5a2e03f583f16d";
		// ���΢��ƽ̨
		UMWXHandler wxHandler = new UMWXHandler(MainActivity.this, appId);
		wxHandler.addToSocialSDK();
		// ֧��΢������Ȧ
		UMWXHandler wxCircleHandler = new UMWXHandler(MainActivity.this, appId);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		appId = "1101819329";
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(MainActivity.this, appId,
		                "KW2B3cbViQWIT3Oe");
		qqSsoHandler.addToSocialSDK();  
		//QQ�ռ�
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(MainActivity.this, appId,
		                "KW2B3cbViQWIT3Oe");
		qZoneSsoHandler.addToSocialSDK();
		
		// ���÷���΢�ŵ�����, ͼƬ����
		UMImage mUMImgBitmap = new UMImage(this,
		                img_url);
		WeiXinShareContent weixinContent = new WeiXinShareContent(mUMImgBitmap);
		weixinContent.setShareContent(message);
		weixinContent.setTargetUrl(url);
		weixinContent.setTitle(title);
		mController.setShareMedia(weixinContent);

		// ��������Ȧ���������
		CircleShareContent circleMedia = new CircleShareContent(new UMImage(this,
		                img_url));
		circleMedia.setShareContent(message);
		circleMedia.setTargetUrl(url);
		circleMedia.setTitle(title);
		mController.setShareMedia(circleMedia);
		
		//����qq����
		QQShareContent qqMedia = new QQShareContent(new UMImage(this, img_url));
		qqMedia.setShareContent(message);
		qqMedia.setTitle(title);
		qqMedia.setTargetUrl(url);
		mController.setShareMedia(qqMedia);
		
		//���÷���qq�ռ�
		QZoneShareContent qzoneMedia = new QZoneShareContent(new UMImage(this, img_url));
		qzoneMedia.setShareContent(message);
		qzoneMedia.setTitle(title);
		qzoneMedia.setTargetUrl(url);
		mController.setShareMedia(qzoneMedia);
		
		mController.setShareContent(message);
		// ���÷���ͼƬ������2ΪͼƬ��url. 
		mController.setShareMedia(new UMImage(this, 
		                                img_url));
		
		//��������SSO handler	
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		
		//������Ѷ΢��SSO handler
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		
		// �Ƴ�ƽ̨
		//mController.getConfig().removePlatform(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		 /**ʹ��SSO��Ȩ����������´��� */
	    UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
	    if(ssoHandler != null){
	       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		my2048View.saveMaxScore();
		MobclickAgent.onPause(this);
	}
	
	
}
