/*===============================================================================
Copyright (c) 2012-2014 Qualcomm Connected Experiences, Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/

package app.cn.aiyouv.www.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.qualcomm.vuforia.CameraDevice;
import com.qualcomm.vuforia.DataSet;
import com.qualcomm.vuforia.ObjectTracker;
import com.qualcomm.vuforia.STORAGE_TYPE;
import com.qualcomm.vuforia.State;
import com.qualcomm.vuforia.Trackable;
import com.qualcomm.vuforia.Tracker;
import com.qualcomm.vuforia.TrackerManager;
import com.qualcomm.vuforia.Vuforia;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.common.Common;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.download.DownloadListener;
import app.cn.aiyouv.www.download.DownloadTask;
import app.cn.aiyouv.www.download.DownloadTaskManager;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.utils.SharedUtils;
import app.cn.aiyouv.www.vuforia.ImageTargetRenderer;
import app.cn.aiyouv.www.vuforia.VuforiaApplicationControl;
import app.cn.aiyouv.www.vuforia.VuforiaApplicationException;
import app.cn.aiyouv.www.vuforia.VuforiaApplicationSession;
import app.cn.aiyouv.www.vuforia.utils.VuforiaApplicationGLView;
import app.cn.aiyouv.www.widget.Loading;
import app.cn.aiyouv.www.widget.NewDataToast;


public class Content2_item0 extends Fragment implements VuforiaApplicationControl {
    private static final String LOGTAG = "ImageTargets";
    private final String AIYOUV_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/AIYOUV/";
    VuforiaApplicationSession vuforiaAppSession;
    private DataSet mCurrentDataset;
    private int mCurrentDatasetSelectionIndex = 0;
    private int mStartDatasetsIndex = 0;
    private int mDatasetsNumber = 0;
    private ArrayList<String> mDatasetStrings = new ArrayList<String>();
    // Our OpenGL view:
    private VuforiaApplicationGLView mGlView;
    // Our renderer:
    private ImageTargetRenderer mRenderer;
    private GestureDetector mGestureDetector;
    // The textures we will use for rendering:
//    private Vector<Texture> mTextures;
    private boolean mSwitchDatasetAsap = false;
    private boolean mFlash = false;
    private boolean mContAutofocus = false;
    private boolean mExtendedTracking = false;
    private ProgressBar loading;
    private View mFlashOptionView;
    private CheckBox falsh_light;


    // Alert Dialog used to display SDK errors
    private AlertDialog mErrorDialog;

    boolean mIsDroidDevice = false;
    //------------------------------
    private FrameLayout content;
    private LinearLayout item0;
/*
    {
        vuforiaAppSession = new VuforiaApplicationSession(this);

        mDatasetStrings.add(AIYOUV_PATH+"tess.xml");
//        mDatasetStrings.add("Tarmac.xml");

        vuforiaAppSession
                .initAR(qrActivity, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mGestureDetector = new GestureDetector(qrActivity, new GestureListener());

        // Load any sample specific textures:
//        mTextures = new Vector<Texture>();
//        loadTextures();

        mIsDroidDevice = Build.MODEL.toLowerCase().startsWith(
                "droid");
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content2_layout0, container, false);
        return view;


    }

    private Activity qrActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.qrActivity = activity;
    }

    public class VQ_Handler extends Handler {
        public WeakReference<Content2_item0> mLeakActivityRef;

        public VQ_Handler(Content2_item0 leakActivity) {
            mLeakActivityRef = new WeakReference<Content2_item0>(leakActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mLeakActivityRef.get() != null) {
                switch (msg.what) {
                    case -1:
                        //设置扫描进入声音
                        MediaPlayer mPlayer = MediaPlayer.create(getActivity(), R.raw.didi);
                        //设置循环播放
                        mPlayer.setLooping(false);
                        mPlayer.start();
                        Intent intent = new Intent(qrActivity, Vuforia_OK_Activity.class);
                        intent.putExtra("flag", (String) msg.obj);
                        startActivity(intent);
                        //close the activity
//                        AppManager.getAppManager().finishActivity(Vuforia_Qr_Activity.class);
                        break;
                    case 0:
                        C.p("进来了");
                        loading.setVisibility(View.GONE);
                        break;
                    case 1:
                        sig();
                        break;
                    case 2:
                        NewDataToast.makeText(qrActivity, "无可用资源").show();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    ImageView mQrLineView;

    private void init(View layout) {
        content = (FrameLayout) layout.findViewById(R.id.content);
        item0 = (LinearLayout) layout.findViewById(R.id.item0);
        layout.findViewById(R.id.title_top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // AppManager.getAppManager().finishActivity();
                Intent intent = new Intent(qrActivity, MainFragmentActivity.class);
                startActivity(intent);
            }
        });
        mQrLineView = (ImageView) layout.findViewById(R.id.capture_scan_line0);
        falsh_light = (CheckBox) layout.findViewById(R.id.falsh_light);
        loading = (ProgressBar) layout.findViewById(R.id.loading);
    }

    private VQ_Handler vq_handler;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vq_handler = new VQ_Handler(Content2_item0.this);
        utils = new SharedUtils(qrActivity, Common.VUFORIA_DOWNLOAD);
        init(view);
        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(1200);
        mQrLineView.startAnimation(animation);
        C.p("这里");

    }

    private SharedUtils utils;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RequestParams params = new RequestParams();
        params.put("version", utils.getStringValue("version"));
        AsyncHttp.post(U.URL + U.VUFORIA_DOWNLOAD, params, new ResponHandler() {
            @Override
            public void onStart() {
                super.onStart();
                C.p("开始");
            }

            @Override
            public void onSuccess(int i, String s) {
                super.onSuccess(i, s);
                C.p("高通地址" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.has("type")) {
                        if (jsonObject.getString("type").equals(U.SUCCESS)) {

                            JSONObject obj = jsonObject.getJSONObject("data");
                            down(U.URL + obj.getString("url"), obj.getString("version"));
                        } else if (jsonObject.getString("type").equals(U.WARN)) {
                            vq_handler.sendEmptyMessage(1);
                        } else if (jsonObject.getString("type").equals(U.ERROR)) {
                            if (utils.getStringValue("version").length() != 0) {
                                vq_handler.sendEmptyMessage(1);
                            } else {
                                vq_handler.sendEmptyMessage(2);
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private final int BUFF_SIZE = 1024 * 1024; // 1M Byte

    public String upZipFile(File zipFile, String folderPath) throws ZipException, IOException {
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdirs();
        }
        ZipFile zf = new ZipFile(zipFile);
        String name = "";
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            InputStream in = zf.getInputStream(entry);
            String str = folderPath + File.separator + entry.getName();
            str = new String(str.getBytes("8859_1"), "GB2312");
            File desFile = new File(str);
            if (!desFile.exists()) {
                File fileParentDir = desFile.getParentFile();
                if (!fileParentDir.exists()) {
                    fileParentDir.mkdirs();
                }
                desFile.createNewFile();
            }
            OutputStream out = new FileOutputStream(desFile);
            byte buffer[] = new byte[BUFF_SIZE];
            int realLength;
            while ((realLength = in.read(buffer)) > 0) {
                out.write(buffer, 0, realLength);
            }
            in.close();
            out.close();
            name = entry.getName();
        }
        return name;
    }

    private DownloadTaskManager taskManager;

    /**
     * 下载资源文件
     *
     * @param url
     * @param version
     */
    private void down(String url, final String version) {
        File file = new File(Common.AIYOUV_PATH + Common.AIYOUV_CACHE);
        if (!file.exists()) {
            file.mkdirs();
        }
        taskManager = DownloadTaskManager.getInstance(qrActivity);

        String name = url.substring(
                url.lastIndexOf("/") + 1,
                url.lastIndexOf("."));
        String fullName = url.substring(url.lastIndexOf("/") + 1,
                url.length());
        final DownloadTask task = new DownloadTask(url, Common.AIYOUV_PATH + Common.AIYOUV_CACHE, fullName, name, null);

        taskManager.registerListener(task, new DownloadListener() {
            @Override
            public void onDownloadFinish(String filepath) {
                File file = new File(filepath);
                try {
                    String name = upZipFile(file, Common.AIYOUV_PATH + Common.AIYOUV_CACHE);
                    taskManager.deleteDownloadTask(task);
                    taskManager.deleteDownloadTaskFile(task);
                    utils.setStringValue("version", version);
                    utils.setStringValue("name", name);
                    vq_handler.sendEmptyMessage(1);

                } catch (IOException e) {
                    e.printStackTrace();
                    C.p("解压异常");
                }
            }

            @Override
            public void onDownloadStart() {

            }

            @Override
            public void onDownloadPause() {

            }

            @Override
            public void onDownloadStop() {

            }

            @Override
            public void onDownloadFail() {

            }

            @Override
            public void onDownloadProgress(int finishedSize, int totalSize, int speed) {

            }
        });
        taskManager.startDownload(task);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    private void sig() {
        vuforiaAppSession = new VuforiaApplicationSession(this);
        C.p("加载的文件是" + utils.getStringValue("name"));
        mDatasetStrings.add(Common.AIYOUV_PATH + Common.AIYOUV_CACHE + utils.getStringValue("name"));
//        mDatasetStrings.add("Tarmac.xml");

        vuforiaAppSession
                .initAR(qrActivity, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mGestureDetector = new GestureDetector(qrActivity, new GestureListener());

        // Load any sample specific textures:
//        mTextures = new Vector<Texture>();
//        loadTextures();

        mIsDroidDevice = Build.MODEL.toLowerCase().startsWith(
                "droid");
        falsh_light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                CameraDevice.getInstance().setFlashTorchMode(b);
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        C.p("------------------ooo");
    }

    // Process Single Tap event to trigger autofocus
    private class GestureListener extends
            GestureDetector.SimpleOnGestureListener {
        // Used to set autofocus one second after a manual focus is triggered
        private final Handler autofocusHandler = new Handler();

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }


        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // Generates a Handler to trigger autofocus
            // after 1 second
            autofocusHandler.postDelayed(new Runnable() {
                public void run() {
                    boolean result = CameraDevice.getInstance().setFocusMode(
                            CameraDevice.FOCUS_MODE.FOCUS_MODE_TRIGGERAUTO);

                    if (!result)
                        Log.e("SingleTapUp", "Unable to trigger focus");
                }
            }, 1000L);

            return true;
        }
    }


    // We want to load specific textures from the APK, which we will later use
    // for rendering.

//    private void loadTextures()
//    {
//        mTextures.add(Texture.loadTextureFromApk("TextureTeapotBrass.png",
//            getAssets()));
//        mTextures.add(Texture.loadTextureFromApk("TextureTeapotBlue.png",
//            getAssets()));
//        mTextures.add(Texture.loadTextureFromApk("TextureTeapotRed.png",
//            getAssets()));
//        mTextures.add(Texture.loadTextureFromApk("ImageTargets/Buildings.jpeg",
//            getAssets()));
//    }
//    

    // Called when the activity will start interacting with the user.
    @Override
    public void onResume() {
        Log.d(LOGTAG, "onResume");
        super.onResume();

        // This is needed for some Droid devices to force portrait
        if (mIsDroidDevice) {
            qrActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            qrActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        try {
            if (vuforiaAppSession != null) {
                vuforiaAppSession.resumeAR();
            }

        } catch (VuforiaApplicationException e) {
            Log.e(LOGTAG, e.getString());
        }

        // Resume the GL view:
        if (mGlView != null) {
            mGlView.setVisibility(View.VISIBLE);
            mGlView.onResume();
        }

    }


    // Callback for configuration changes the activity handles itself
    @Override
    public void onConfigurationChanged(Configuration config) {
        Log.d(LOGTAG, "onConfigurationChanged");
        super.onConfigurationChanged(config);
        if (vuforiaAppSession != null) {
            vuforiaAppSession.onConfigurationChanged();
        }

    }


    // Called when the system is about to start resuming a previous activity.


    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOGTAG, "onPause");
        super.onPause();

        if (mGlView != null) {
            mGlView.setVisibility(View.INVISIBLE);
            mGlView.onPause();
            System.out.println("暂停0");
        }
        try {
            System.out.println("暂停1");
            if (vuforiaAppSession != null) {
                vuforiaAppSession.pauseAR();
            }


        } catch (VuforiaApplicationException e) {
            Log.e(LOGTAG, e.getString());
        }
    }

    // The final call you receive before your activity is destroyed.
    @Override
    public void onDestroy() {
        Log.d(LOGTAG, "onDestroy");
        super.onDestroy();

        try {
            if (vuforiaAppSession != null) {
                vuforiaAppSession.stopAR();
            }

        } catch (VuforiaApplicationException e) {
            Log.e(LOGTAG, e.getString());
        }

        // Unload texture:
//        mTextures.clear();
//        mTextures = null;

        System.gc();
    }


    // Initializes AR application components.
    private void initApplicationAR() {
        // Create OpenGL ES view:
        int depthSize = 16;
        int stencilSize = 0;
        boolean translucent = Vuforia.requiresAlpha();

        mGlView = new VuforiaApplicationGLView(qrActivity);
        mGlView.init(translucent, depthSize, stencilSize);

        mRenderer = new ImageTargetRenderer(this, vuforiaAppSession, vq_handler);
//        mRenderer.setTextures(mTextures);
        mGlView.setRenderer(mRenderer);

    }


    // Methods to load and destroy tracking data.
    @Override
    public boolean doLoadTrackersData() {
        TrackerManager tManager = TrackerManager.getInstance();
        ObjectTracker objectTracker = (ObjectTracker) tManager
                .getTracker(ObjectTracker.getClassType());
        if (objectTracker == null)
            return false;

        if (mCurrentDataset == null)
            mCurrentDataset = objectTracker.createDataSet();

        if (mCurrentDataset == null)
            return false;

        if (!mCurrentDataset.load(
                mDatasetStrings.get(mCurrentDatasetSelectionIndex),
                STORAGE_TYPE.STORAGE_ABSOLUTE))
            return false;

        if (!objectTracker.activateDataSet(mCurrentDataset))
            return false;

        int numTrackables = mCurrentDataset.getNumTrackables();
        for (int count = 0; count < numTrackables; count++) {
            Trackable trackable = mCurrentDataset.getTrackable(count);
            if (isExtendedTrackingActive()) {
                trackable.startExtendedTracking();
            }

            String name = "Current Dataset : " + trackable.getName();
            trackable.setUserData(name);
            Log.d(LOGTAG, "UserData:Set the following user data "
                    + (String) trackable.getUserData());
        }

        return true;
    }


    @Override
    public boolean doUnloadTrackersData() {
        // Indicate if the trackers were unloaded correctly
        boolean result = true;

        TrackerManager tManager = TrackerManager.getInstance();
        ObjectTracker objectTracker = (ObjectTracker) tManager
                .getTracker(ObjectTracker.getClassType());
        if (objectTracker == null)
            return false;

        if (mCurrentDataset != null && mCurrentDataset.isActive()) {
            if (objectTracker.getActiveDataSet().equals(mCurrentDataset)
                    && !objectTracker.deactivateDataSet(mCurrentDataset)) {
                result = false;
            } else if (!objectTracker.destroyDataSet(mCurrentDataset)) {
                result = false;
            }

            mCurrentDataset = null;
        }

        return result;
    }


    @Override
    public void onInitARDone(VuforiaApplicationException exception) {

        if (exception == null) {
            initApplicationAR();

            mRenderer.mIsActive = true;

            // Now add the GL surface view. It is important
            // that the OpenGL ES surface view gets added
            // BEFORE the camera is started and video
            // background is configured.
            item0.addView(mGlView, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
//            item0.addContentView(mGlView, new LayoutParams(LayoutParams.MATCH_PARENT,
//                    LayoutParams.MATCH_PARENT));

            // Sets the UILayout to be drawn in front of the camera
//            mGlView.bringToFront();

            // Sets the layout background to transparent
            // mGlView.setBackgroundColor(Color.TRANSPARENT);

            try {
                vuforiaAppSession.startAR(CameraDevice.CAMERA.CAMERA_DEFAULT);
            } catch (VuforiaApplicationException e) {
                Log.e(LOGTAG, e.getString());
            }

            boolean result = CameraDevice.getInstance().setFocusMode(
                    CameraDevice.FOCUS_MODE.FOCUS_MODE_CONTINUOUSAUTO);

            if (result)
                mContAutofocus = true;
            else
                Log.e(LOGTAG, "Unable to enable continuous autofocus");


        } else {
            Log.e(LOGTAG, exception.getString());
            showInitializationErrorMessage(exception.getString());
        }
    }

    // Shows initialization error messages as System dialogs
    public void showInitializationErrorMessage(String message) {
        final String errorMessage = message;
        qrActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (mErrorDialog != null) {
                    mErrorDialog.dismiss();
                }

                // Generates an Alert Dialog to show the error message
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        qrActivity);
                builder
                        .setMessage(errorMessage)
                        .setTitle(getString(R.string.INIT_ERROR))
                        .setCancelable(false)
                        .setIcon(0)
                        .setPositiveButton(getString(R.string.button_OK),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        AppManager.getAppManager().finishActivity();
                                    }
                                });

                mErrorDialog = builder.create();
                mErrorDialog.show();
            }
        });
    }


    @Override
    public void onQCARUpdate(State state) {
        if (mSwitchDatasetAsap) {
            mSwitchDatasetAsap = false;
            TrackerManager tm = TrackerManager.getInstance();
            ObjectTracker ot = (ObjectTracker) tm.getTracker(ObjectTracker
                    .getClassType());
            if (ot == null || mCurrentDataset == null
                    || ot.getActiveDataSet() == null) {
                Log.d(LOGTAG, "Failed to swap datasets");
                return;
            }

            doUnloadTrackersData();
            doLoadTrackersData();
        }
    }


    @Override
    public boolean doInitTrackers() {
        // Indicate if the trackers were initialized correctly
        boolean result = true;

        TrackerManager tManager = TrackerManager.getInstance();
        Tracker tracker;

        // Trying to initialize the image tracker
        tracker = tManager.initTracker(ObjectTracker.getClassType());
        if (tracker == null) {
            Log.e(
                    LOGTAG,
                    "Tracker not initialized. Tracker already initialized or the camera is already started");
            result = false;
        } else {
            Log.i(LOGTAG, "Tracker successfully initialized");
        }
        return result;
    }


    @Override
    public boolean doStartTrackers() {
        // Indicate if the trackers were started correctly
        boolean result = true;

        Tracker objectTracker = TrackerManager.getInstance().getTracker(
                ObjectTracker.getClassType());
        if (objectTracker != null)
            objectTracker.start();

        return result;
    }


    @Override
    public boolean doStopTrackers() {
        // Indicate if the trackers were stopped correctly
        boolean result = true;

        Tracker objectTracker = TrackerManager.getInstance().getTracker(
                ObjectTracker.getClassType());
        if (objectTracker != null)
            objectTracker.stop();

        return result;
    }


    @Override
    public boolean doDeinitTrackers() {
        // Indicate if the trackers were deinitialized correctly
        boolean result = true;

        TrackerManager tManager = TrackerManager.getInstance();
        tManager.deinitTracker(ObjectTracker.getClassType());

        return result;
    }


    public boolean isExtendedTrackingActive() {
        return mExtendedTracking;
    }

    final public static int CMD_BACK = -1;
    final public static int CMD_EXTENDED_TRACKING = 1;
    final public static int CMD_AUTOFOCUS = 2;
    final public static int CMD_FLASH = 3;
    final public static int CMD_CAMERA_FRONT = 4;
    final public static int CMD_CAMERA_REAR = 5;
    final public static int CMD_DATASET_START_INDEX = 6;


    private void showToast(String text) {
        Toast.makeText(qrActivity, text, Toast.LENGTH_SHORT).show();
    }
}
