/*
 * Offical Website:http://www.ShareSDK.cn
 * Support QQ: 4006852216
 * Offical Wechat Account:ShareSDK   (We will inform you our updated news at the first time by Wechat, if we release a new version. If you get any problem, you can also contact us with Wechat, we will reply you within 24 hours.)
 *
 * Copyright (c) 2013 ShareSDK.cn. All rights reserved.
 */

package cn.sharesdk.demo;

import java.io.File;
import java.io.FileOutputStream;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import m.framework.ui.widget.slidingmenu.SlidingMenu;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * entrance of the project, UI shell of sliding menu
 * ui logics and events are handled by {@link MainAdapter}
 */
public class MainActivity extends Activity implements Callback {
	private static final String FILE_NAME = "/pic.jpg";
	public static String TEST_IMAGE;
	private SlidingMenu menu;
	private int orientation;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		orientation = getResources().getConfiguration().orientation;

		menu = new SlidingMenu(this);
		menu.setMenuItemBackground(R.color.sliding_menu_item_down, R.color.sliding_menu_item_release);
		menu.setMenuBackground(R.color.sliding_menu_background);
		menu.setTtleHeight(cn.sharesdk.framework.utils.R.dipToPx(this, 44));
		menu.setBodyBackground(R.color.sliding_menu_body_background);
		menu.setShadowRes(R.drawable.sliding_menu_right_shadow);
		menu.setMenuDivider(R.drawable.sliding_menu_sep);
		menu.setAdapter(new MainAdapter(menu));
		setContentView(menu);

		ShareSDK.initSDK(this);

		new Thread() {
			public void run() {
				initImagePath();
				UIHandler.sendEmptyMessageDelayed(1, 100, MainActivity.this);
			}
		}.start();
	}

	private void initImagePath() {
		try {
			String cachePath = cn.sharesdk.framework.utils.R.getCachePath(this, null);
			TEST_IMAGE = cachePath + FILE_NAME;
			File file = new File(TEST_IMAGE);
			if (!file.exists()) {
				file.createNewFile();
				Bitmap pic = BitmapFactory.decodeResource(getResources(), R.drawable.pic);
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

	public boolean handleMessage(Message msg) {
		switch (msg.what) {
			case 1: {
				menu.triggerItem(MainAdapter.GROUP_DEMO, MainAdapter.ITEM_DEMO);
			}
			break;
			case 2: {
				String text = getString(R.string.receive_rewards, msg.arg1);
				Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			}
			break;
		}
		return false;
	}

	/** this method will be called after the screen rotation to refresh the sliding menu */
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (orientation != newConfig.orientation) {
			orientation = newConfig.orientation;
			menu.refresh();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN
				&& !menu.isMenuShown()) {
			menu.showMenu();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void onDestroy() {
		ShareSDK.stopSDK(this);
		super.onDestroy();
	}

	/** converts ShareSDK actions into string */
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

}
