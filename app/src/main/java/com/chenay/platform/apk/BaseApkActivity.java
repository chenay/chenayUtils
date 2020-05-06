package com.chenay.platform.apk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.core.content.FileProvider;

import com.chenay.platform.ChyApplication;
import com.chenay.platform.mvp.factory.IMvpCreatePresenter;
import com.chenay.platform.mvp.model.IMvpBaseAppCompatActivity;
import com.chenay.platform.utils.R;
import com.chenay.platform.utils.ResourcesUtils;
import com.chenay.platform.utils.ToastUtil;

import java.io.File;


/**
 * @author Y.Chen5
 */
@IMvpCreatePresenter(ApkPresenter.class)
public class BaseApkActivity extends IMvpBaseAppCompatActivity<ApkView, ApkPresenter> implements ApkView {
    private static final String TAG = BaseApkActivity.class.getName();
    private static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private final static String APK_ROOT_PATH = "/apk/update";
    public static String DOWN_FILE_PATH = SDCARD_PATH + File.separator + APK_ROOT_PATH;
    private AlertDialog dialog;
    private CyDownloadDialogFragment downloadDialogFragment;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ChyApplication.context);
    }

    public void uploadApk(String url) {
        ApkModelImpl.ApkModelListener listener = new ApkModelImpl.ApkModelListener() {
            @Override
            public void doToastMsg(final String stringBuffer) {
                ToastUtil.showDefaultShortToast(BaseApkActivity.this, stringBuffer);
            }

            @Override
            public void doUploadApk(final String nowVersionName, final ApkConfigBean apkConfigBean) {
                doNewVersionUpdate(nowVersionName, apkConfigBean);
            }
        };
        ApkModelImpl apkModel = new ApkModelImpl();
        apkModel.setListener(listener);
        apkModel.httpRequestApkConfig(url, apkModel.getApkConfig(), true);

    }


    /**
     * Role:是否进行更新提示框<BR>
     */
    protected void doNewVersionUpdate(String nowVersion, final ApkConfigBean updateConfigBean) {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        final String apkName = updateConfigBean.getPath();
        String newVersion = apkName.substring(apkName.lastIndexOf("-") + 1, apkName.lastIndexOf("."));

        dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.str_update_apk_1))
                .setMessage(String.format(ResourcesUtils.getString(R.string.str_update_apk_msg), nowVersion, newVersion))
                // 设置内容
                .setPositiveButton(getString(R.string.str_update),// 设置确定按钮
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                downloadApk(updateConfigBean);
                            }
                        })
                .setNegativeButton(R.string.str_update_not_now,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // 点击"取消"按钮之后退出程序

                            }
                        }).create();// 创建
        // 显示对话框
        dialog.show();
    }


    public void downloadApk(ApkConfigBean updateConfigBean) {

    }

    public void downloadApk(String url, ApkConfigBean updateConfigBean) {
        getMvpPresenter().requestDownLoadApk(url, updateConfigBean);
    }


    public void showDownDialog(DownloadInfoEntity downloadInfoEntity) {
        if (downloadInfoEntity.isDloadStart()) {
            if (downloadDialogFragment == null) {
                downloadDialogFragment = new CyDownloadDialogFragment();
                final Bundle bundle = new Bundle();
                bundle.putSerializable("data", downloadInfoEntity);
                downloadDialogFragment.setArguments(bundle);
                downloadDialogFragment.show(getSupportFragmentManager(), "download");
            }

            if (downloadDialogFragment.isVisible()) {
                downloadDialogFragment.update();
            }

        } else if (downloadInfoEntity.isDloadEnd()) {
            if (downloadDialogFragment != null) {
                downloadDialogFragment.dismiss();
                downloadDialogFragment.onDestroy();
                downloadDialogFragment = null;
            }
        }

        if (downloadInfoEntity.isSuccess()) {
            final File apkFile = new File(downloadInfoEntity.getPath());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showDefaultShortToast(ChyApplication.context, BaseApkActivity.this.getString(R.string.str_dowm_success));
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    //判断是否是AndroidN以及更高的版本
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri contentUri = FileProvider.getUriForFile(ChyApplication.context, "com.chenay.fileProvider", apkFile);
                        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                    } else {
                        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
//                logger.debug(TAG, "开始安装!...");
                    BaseApkActivity.this.startActivity(intent);
                }
            });

        }


    }

    @Override
    public void httpResponseDownload(DownloadInfoEntity downloadInfoEntity) {
        showDownDialog(downloadInfoEntity);
    }
}
