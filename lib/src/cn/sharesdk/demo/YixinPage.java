/*
 * Offical Website:http://www.ShareSDK.cn
 * Support QQ: 4006852216
 * Offical Wechat Account:ShareSDK   (We will inform you our updated news at the first time by Wechat, if we release a new version. If you get any problem, you can also contact us with Wechat, we will reply you within 24 hours.)
 *
 * Copyright (c) 2013 ShareSDK.cn. All rights reserved.
 */

package cn.sharesdk.demo;

import android.graphics.BitmapFactory;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.TitleLayout;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.yixin.friends.Yixin;
import cn.sharesdk.yixin.moments.YixinMoments;
import m.framework.ui.widget.slidingmenu.SlidingMenu;

import java.util.HashMap;

/**
 * page to show Yixin apis.
 */
public class YixinPage extends SlidingMenuPage implements
        OnClickListener, PlatformActionListener {
    private TitleLayout llTitle;
    private CheckedTextView[] ctvPlats;
    private View pageView;

    public YixinPage(SlidingMenu menu) {
        super(menu);
        pageView = getPage();

        llTitle = (TitleLayout) pageView.findViewById(R.id.llTitle);
        llTitle.getBtnBack().setOnClickListener(this);
        llTitle.getTvTitle().setText(R.string.sm_item_yixin);

        ctvPlats = new CheckedTextView[]{
                (CheckedTextView) pageView.findViewById(R.id.ctvStWc),
                (CheckedTextView) pageView.findViewById(R.id.ctvStWm),
                (CheckedTextView) pageView.findViewById(R.id.ctvStWf)
        };
        int[] labels = new int[]{
                R.string.share_to_yixin,
                R.string.share_to_yixin_moment,
                R.string.share_to_yixin_favorite
        };
        for (int i = 0; i < ctvPlats.length; i++) {
            ctvPlats[i].setText(labels[i]);
            ctvPlats[i].setOnClickListener(this);
        }
        ViewGroup vp = (ViewGroup) ctvPlats[0].getParent().getParent();
        for (int i = 0, size = vp.getChildCount(); i < size; i++) {
            vp.getChildAt(i).setOnClickListener(this);
        }

        ctvPlats[0].setChecked(true);
        int[] invIds = new int[]{
                R.id.btnEmoji,
                R.id.btnEmojiUrl,
                R.id.btnEmojiBitmap,
                R.id.btnApp,
                R.id.btnAppExt,
                R.id.btnFile
        };
        for (int id : invIds) {
            pageView.findViewById(id).setVisibility(View.GONE);
        }
        ctvPlats[2].setVisibility(View.GONE);
    }

    protected View initPage() {
        return LayoutInflater.from(menu.getContext())
                .inflate(R.layout.page_wechate, null);
    }

    public void onClick(View v) {
        if (v.equals(llTitle.getBtnBack())) {
            if (menu.isMenuShown()) {
                menu.hideMenu();
            } else {
                menu.showMenu();
            }
            return;
        }

        if (v instanceof CheckedTextView) {
            for (CheckedTextView ctv : ctvPlats) {
                ctv.setChecked(ctv.equals(v));
            }

            int[] visIds = null;
            int[] invIds = null;
            if (v.equals(ctvPlats[0])) {
                visIds = new int[]{
                        R.id.btnUpdate,
                        R.id.btnUpload,
                        R.id.btnUploadBm,
                        R.id.btnUploadUrl,
                        R.id.btnMusic,
                        R.id.btnVideo,
                        R.id.btnWebpage,
                        R.id.btnWebpageBm,
                        R.id.btnWebpageUrl
                };
                invIds = new int[]{
                        R.id.btnEmoji,
                        R.id.btnEmojiUrl,
                        R.id.btnEmojiBitmap,
                        R.id.btnApp,
                        R.id.btnAppExt,
                        R.id.btnFile
                };
            } else if (v.equals(ctvPlats[1])) {
                visIds = new int[]{
                        R.id.btnUpdate,
                        R.id.btnUpload,
                        R.id.btnUploadBm,
                        R.id.btnUploadUrl,
                        R.id.btnMusic,
                        R.id.btnVideo,
                        R.id.btnWebpage,
                        R.id.btnWebpageBm,
                        R.id.btnWebpageUrl
                };
                invIds = new int[]{
                        R.id.btnEmoji,
                        R.id.btnEmojiUrl,
                        R.id.btnEmojiBitmap,
                        R.id.btnApp,
                        R.id.btnAppExt,
                        R.id.btnFile
                };
            } else {
                visIds = new int[]{
                        R.id.btnUpdate,
                        R.id.btnUpload,
                        R.id.btnUploadBm,
                        R.id.btnUploadUrl,
                        R.id.btnMusic,
                        R.id.btnVideo,
                        R.id.btnWebpage,
                        R.id.btnWebpageBm,
                        R.id.btnWebpageUrl
                };
                invIds = new int[]{
                        R.id.btnEmoji,
                        R.id.btnEmojiUrl,
                        R.id.btnEmojiBitmap,
                        R.id.btnApp,
                        R.id.btnAppExt,
                        R.id.btnFile
                };
            }

            for (int id : visIds) {
                menu.findViewById(id).setVisibility(View.VISIBLE);
            }
            for (int id : invIds) {
                menu.findViewById(id).setVisibility(View.GONE);
            }
            return;
        }

        Platform plat = null;
        ShareParams sp = null;
        if (ctvPlats[0].isChecked()) {
            plat = ShareSDK.getPlatform(menu.getContext(), "Yixin");
            sp = getYixinShareParams(v);
        } else if (ctvPlats[1].isChecked()) {
            plat = ShareSDK.getPlatform(menu.getContext(), "YixinMoments");
            sp = getYixinMomentsShareParams(v);
        }
        plat.setPlatformActionListener(this);
        plat.share(sp);
    }

    private ShareParams getYixinShareParams(View v) {
        Yixin.ShareParams sp = new Yixin.ShareParams();
        sp.title = menu.getContext().getString(R.string.yixin_demo_title);
        sp.text = menu.getContext().getString(R.string.share_content);
        sp.shareType = Platform.SHARE_TEXT;
        final int id = v.getId();
        if (id == R.id.btnUpload) {
            sp.shareType = Platform.SHARE_IMAGE;
            sp.imagePath = MainActivity.TEST_IMAGE;
        } else if (id == R.id.btnUploadBm) {
            sp.shareType = Platform.SHARE_IMAGE;
            sp.imageData = BitmapFactory.decodeResource(v.getResources(), R.drawable.ic_launcher);
        } else if (id == R.id.btnUploadUrl) {
            sp.shareType = Platform.SHARE_IMAGE;
            sp.imageUrl = "http://img.appgo.cn/imgs/sharesdk/content/2013/07/16/1373959974649.png";
        } else if (id == R.id.btnMusic) {
            sp.shareType = Platform.SHARE_MUSIC;
            sp.musicUrl = "http://ubuntuone.com/45XSEOwdODtXSH0WYGAcR7";
            sp.url = "http://sharesdk.cn";
            sp.imagePath = MainActivity.TEST_IMAGE;
        } else if (id == R.id.btnVideo) {
            sp.shareType = Platform.SHARE_VIDEO;
            sp.url = "http://t.cn/zT7cZAo";
            sp.imagePath = MainActivity.TEST_IMAGE;
        } else if (id == R.id.btnWebpage) {
            sp.shareType = Platform.SHARE_WEBPAGE;
            sp.url = "http://t.cn/zT7cZAo";
            sp.imagePath = MainActivity.TEST_IMAGE;
        } else if (id == R.id.btnWebpageBm) {
            sp.shareType = Platform.SHARE_WEBPAGE;
            sp.url = "http://t.cn/zT7cZAo";
            sp.imageData = BitmapFactory.decodeResource(v.getResources(), R.drawable.ic_launcher);
        } else if (id == R.id.btnWebpageUrl) {
            sp.shareType = Platform.SHARE_WEBPAGE;
            sp.url = "http://t.cn/zT7cZAo";
            sp.imageUrl = "http://img.appgo.cn/imgs/sharesdk/content/2013/07/16/1373959974649.png";
        }
        return sp;
    }

    private ShareParams getYixinMomentsShareParams(View v) {
        YixinMoments.ShareParams sp = new YixinMoments.ShareParams();
        sp.title = menu.getContext().getString(R.string.yixin_demo_title);
        sp.text = menu.getContext().getString(R.string.share_content);
        sp.shareType = Platform.SHARE_TEXT;
        final int id = v.getId();
        if (id == R.id.btnUpload) {
            sp.shareType = Platform.SHARE_IMAGE;
            sp.imagePath = MainActivity.TEST_IMAGE;
        } else if (id == R.id.btnUploadBm) {
            sp.shareType = Platform.SHARE_IMAGE;
            sp.imageData = BitmapFactory.decodeResource(v.getResources(), R.drawable.ic_launcher);
        } else if (id == R.id.btnUploadUrl) {
            sp.shareType = Platform.SHARE_IMAGE;
            sp.imageUrl = "http://img.appgo.cn/imgs/sharesdk/content/2013/07/16/1373959974649.png";
        } else if (id == R.id.btnMusic) {
            sp.shareType = Platform.SHARE_MUSIC;
            sp.musicUrl = "http://ubuntuone.com/45XSEOwdODtXSH0WYGAcR7";
            sp.url = "http://sharesdk.cn";
            sp.imagePath = MainActivity.TEST_IMAGE;
        } else if (id == R.id.btnVideo) {
            sp.shareType = Platform.SHARE_VIDEO;
            sp.url = "http://t.cn/zT7cZAo";
            sp.imagePath = MainActivity.TEST_IMAGE;
        } else if (id == R.id.btnWebpage) {
            sp.shareType = Platform.SHARE_WEBPAGE;
            sp.url = "http://t.cn/zT7cZAo";
            sp.imagePath = MainActivity.TEST_IMAGE;
        } else if (id == R.id.btnWebpageBm) {
            sp.shareType = Platform.SHARE_WEBPAGE;
            sp.url = "http://t.cn/zT7cZAo";
            sp.imageData = BitmapFactory.decodeResource(v.getResources(), R.drawable.ic_launcher);
        } else if (id == R.id.btnWebpageUrl) {
            sp.shareType = Platform.SHARE_WEBPAGE;
            sp.url = "http://t.cn/zT7cZAo";
            sp.imageUrl = "http://img.appgo.cn/imgs/sharesdk/content/2013/07/16/1373959974649.png";
        }
        return sp;
    }

    public void onComplete(Platform plat, int action,
                           HashMap<String, Object> res) {
        Message msg = new Message();
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = plat;
        UIHandler.sendMessage(msg, this);
    }

    public void onCancel(Platform plat, int action) {
        Message msg = new Message();
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = plat;
        UIHandler.sendMessage(msg, this);
    }

    public void onError(Platform plat, int action, Throwable t) {
        t.printStackTrace();

        Message msg = new Message();
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);
    }

    public boolean handleMessage(Message msg) {
        String text = MainActivity.actionToString(msg.arg2);
        switch (msg.arg1) {
            case 1: {
                // success
                Platform plat = (Platform) msg.obj;
                text = plat.getName() + " completed at " + text;
            }
            break;
            case 2: {
                // failed
                if ("YixinClientNotExistException".equals(msg.obj.getClass().getSimpleName())) {
                    text = menu.getContext().getString(R.string.yixin_client_inavailable);
                } else if ("YixinClientNotExistException".equals(msg.obj.getClass().getSimpleName())) {
                    text = menu.getContext().getString(R.string.yixin_client_inavailable);
                } else {
                    text = menu.getContext().getString(R.string.share_failed);
                }
            }
            break;
            case 3: {
                // canceled
                Platform plat = (Platform) msg.obj;
                text = plat.getName() + " canceled at " + text;
            }
            break;
        }

        Toast.makeText(menu.getContext(), text, Toast.LENGTH_LONG).show();
        return false;
    }

}
