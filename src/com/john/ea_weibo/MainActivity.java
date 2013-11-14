package com.john.ea_weibo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.FollowList.FollowAdapter;
import cn.sharesdk.onekeyshare.FollowList.Following;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler.Callback;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
* 功能描述：主Activity类，程序的入口类 
*/ 
public  class MainActivity extends Activity implements OnClickListener  {
	
	//定义log标志
	private static final String TAG="EA_Weibo";
	
	//定义图片存放的地址  
	public static String TEST_IMAGE; 
	
	//定义"账号登陆"按钮，"分享"按钮
    private Button shareBtn,loginBtn,shareEditBtn, followBtn;  
    
    //定义好友列表
	public static String friendslist=null;
	
	
	private Platform plat=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//初始化ShareSDK 
		ShareSDK.initSDK(this);
		
        initImagePath();                      
        
        initView();  
          
        initData();  
		
	}
	/** 
	 * 初始化数据 
     */  
	private void initData() {//获取平台实例
		// TODO Auto-generated method stub
		 //设置按钮监听事件  
        loginBtn.setOnClickListener(this);  
        shareBtn.setOnClickListener(this); 
        shareEditBtn.setOnClickListener(this);
       // followBtn.setOnClickListener(this);
        
    	plat = ShareSDK.getPlatform(this, SinaWeibo.NAME);
	}
    /** 
     * 初始化组件 
     */  
	private void initView() {
		// TODO Auto-generated method stub
	    loginBtn = (Button)findViewById(R.id.btnLogin);  
        shareBtn = (Button)findViewById(R.id.btnFlSw);
        shareEditBtn = (Button)findViewById(R.id.btnFlSwEdit);
        
        //followBtn = (Button) findViewById(R.id.btn_follow);
	}
	/** 
     * 初始化分享的图片 
     */  
	private void initImagePath() {
		// TODO Auto-generated method stub
		 try {//判断SD卡中是否存在此文件夹  
	            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())  
	                    && Environment.getExternalStorageDirectory().exists()) {  
	                TEST_IMAGE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ic_launcher.png";  
	            }  
	            else {  
	                TEST_IMAGE = getApplication().getFilesDir().getAbsolutePath() + "/ic_launcher.png";  
	            }  
	            File file = new File(TEST_IMAGE);  
	            //判断图片是否存此文件夹中  
	            if (!file.exists()) {  
	                file.createNewFile();  
	                Bitmap pic = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);  
	                FileOutputStream fos = new FileOutputStream(file);  
	                pic.compress(CompressFormat.JPEG, 100, fos);  
	                fos.flush();  
	                fos.close();  
	            }  
	        } catch(Throwable t) {  
	            t.printStackTrace();  
	            TEST_IMAGE = null;  
	        }  
	}
	/** 
     * 按钮监听事件 
     */  
    @Override  
    public void onClick(View v) {  
        switch (v.getId()) {  
        case R.id.btnLogin:  
        	Log.d(TAG, "authactivity");
            startActivity(new Intent(MainActivity.this,AuthActivity.class));  
            break;  
        case R.id.btnFlSw:  
        	Log.d(TAG, "one key share weibo");
        	plat.setPlatformActionListener(new PlatformActionListener() {
				
				@Override
				public void onError(Platform arg0, int arg1, Throwable arg2) {
					// TODO Auto-generated method stub
					Log.w("WWWW", "------onError-------");
					
				}
				
				@Override
				public void onComplete(Platform arg0, int arg1, HashMap<String, Object> res) {
					// TODO Auto-generated method stub
					Log.w("WWWW", "------onComplete-------");
					ArrayList<HashMap<String, Object>> users = (ArrayList<HashMap<String, Object>>) res
							.get("users");
					Log.w("WWWW", "------onComplete users -------" + users);
					for (HashMap<String, Object> user : users) {
						Following following = new Following();
						following.uid = String.valueOf(user.get("id"));
						following.screeName = String.valueOf(user.get("name"));
						friendslist +="@"+following.screeName+ " ";
						following.description = String.valueOf(user
								.get("description"));
						Log.w("WWWW", "------follow-------" + following.screeName);
					}
		        	showShare(true, SinaWeibo.NAME);  
				}
				
				@Override
				public void onCancel(Platform arg0, int arg1) {
					// TODO Auto-generated method stub
					
					Log.w("WWWW", "------onCancel-------");
				}
			});
        	plat.listFriend(10, 0, null);
            break;  
        case R.id.btnFlSwEdit:
        	Log.d(TAG, "Edit share");
        	showShare(false, SinaWeibo.NAME);
        	
       /* case R.id.btn_follow:

        	plat.setPlatformActionListener(new PlatformActionListener() {
				
				@Override
				public void onError(Platform arg0, int arg1, Throwable arg2) {
					// TODO Auto-generated method stub
					Log.w("WWWW", "------onError-------");
					
				}
				
				@Override
				public void onComplete(Platform arg0, int arg1, HashMap<String, Object> res) {
					// TODO Auto-generated method stub
					Log.w("WWWW", "------onComplete-------");
					ArrayList<HashMap<String, Object>> users = (ArrayList<HashMap<String, Object>>) res
							.get("users");
					Log.w("WWWW", "------onComplete users -------" + users);
					for (HashMap<String, Object> user : users) {
						Following following = new Following();
						following.uid = String.valueOf(user.get("id"));
						following.screeName = String.valueOf(user.get("name"));
						following.description = String.valueOf(user
								.get("description"));
						Log.w("WWWW", "------follow-------" + following.screeName);
					}
				}
				
				@Override
				public void onCancel(Platform arg0, int arg1) {
					// TODO Auto-generated method stub
					
					Log.w("WWWW", "------onCancel-------");
				}
			});
        	plat.listFriend(10, 0, null);
        	break;*/
        default:  
            break;  
        }  
          
    }  
	private void showShare(boolean silent, String platform) {
		final OnekeyShare oks = new OnekeyShare();
		Log.d(TAG,"showShare");
		// 分享时Notification的图标  
		oks.setNotification(R.drawable.ic_launcher, this.getString(R.string.app_name));
		// address是接收人地址，仅在信息和邮件使用
		//oks.setAddress("12345678901");
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(this.getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		//oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		
		oks.setText(this.getString(R.string.share_content,friendslist));
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		//oks.setImagePath(MainActivity.TEST_IMAGE);
		// imageUrl是图片的网络路径，新浪微博、人人网、QQ空间、  微信的两个平台、Linked-In支持此字段
		//oks.setImageUrl("http://img.appgo.cn/imgs/sharesdk/content/2013/07/25/1374723172663.jpg");
		// url仅在微信（包括好友和朋友圈）中使用
		//oks.setUrl("http://www.sharesdk.cn");
		// filePath是待分享应用程序的本地路劲，仅在微信中使用
		//oks.setFilePath(MainActivity.TEST_IMAGE);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment(this.getString(R.string.share));
		// site是分享此内容的网站名称，仅在QQ空间使用
		//oks.setSite(menu.getContext().getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		//oks.setSiteUrl("http://sharesdk.cn");
		// venueName是分享社区名称，仅在Foursquare使用
		//oks.setVenueName("Share SDK");
		// venueDescription是分享社区描述，仅在Foursquare使用
		//oks.setVenueDescription("This is a beautiful place!");
		// latitude是维度数据，仅在新浪微博、腾讯微博和Foursquare使用
		oks.setLatitude(31.194056f);//31°11'38.6"
		// longitude是经度数据，仅在新浪微博、腾讯微博和Foursquare使用
		oks.setLongitude(121.614611f);//121°36'52.6"
		// 是否直接分享（true则直接分享）
		oks.setSilent(silent);
		// 指定分享平台，和slient一起使用可以直接分享到指定的平台
		Log.d(TAG, "platform: " + platform);
		if (platform != null) {
			oks.setPlatform(platform);
		}
		// 去除注释，可令编辑页面显示为Dialog模式
//		oks.setDialogMode();

		// 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
		oks.setCallback(new OneKeyShareCallback());
		//oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());

		// 去除注释，演示在九宫格设置自定义的图标
//		Bitmap logo = BitmapFactory.decodeResource(menu.getResources(), R.drawable.ic_launcher);
//		String label = menu.getResources().getString(R.string.app_name);
//		OnClickListener listener = new OnClickListener() {
//			public void onClick(View v) {
//				String text = "Customer Logo -- Share SDK " + ShareSDK.getSDKVersionName();
//				Toast.makeText(menu.getContext(), text, Toast.LENGTH_SHORT).show();
//				oks.finish();
//			}
//		};
//		oks.setCustomerLogo(logo, label, listener);

		oks.show(this);
	}
	/**
	 *将action转换为String  作用？
	 */ 
    public static String actionToString(int action) {  
        switch (action) {  
            case Platform.ACTION_AUTHORIZING: return "ACTION_AUTHORIZING";  
            case Platform.ACTION_GETTING_FRIEND_LIST: return "ACTION_GETTING_FRIEND_LIST";  
            case Platform.ACTION_FOLLOWING_USER: return "ACTION_FOLLOWING_USER";  
            case Platform.ACTION_SENDING_DIRECT_MESSAGE: return "ACTION_SENDING_DIRECT_MESSAGE";  
            case Platform.ACTION_TIMELINE: return "ACTION_TIMELINE";  
            case Platform.ACTION_USER_INFOR: return "ACTION_USER_INFOR";  
            case Platform.ACTION_SHARE: return "ACTION_SHARE";  
            default: {  
                return "UNKNOWN";  
            }  
        }  
    } 
	protected void onDestroy() {
		//结束ShareSDK的统计功能并释放资源  
		ShareSDK.stopSDK(this);
		super.onDestroy();
	}


}
