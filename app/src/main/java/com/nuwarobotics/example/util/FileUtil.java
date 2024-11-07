package com.nuwarobotics.example.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class FileUtil {
    public static final String TAG = FileUtil.class.getSimpleName();

    /**
     * Create a assets folder in android shared external root /storage/emulated/0/
     * @param shared_root_path create assets folder /storage/emulated/0/[shared_root_path]/assets/motion_bin
     * @return return full value for copy file
     */
    public static String createExternalAssetFolder(String shared_root_path) {
        File dirFile = new File(Environment.getExternalStorageDirectory() + "/"+shared_root_path+"/", "assets");
        Log.d("FileUtil", "check folder " + dirFile.getAbsolutePath());
        if (!dirFile.exists()) {//如果資料夾不存在
            Log.d("FileUtil", "folder not exist, create it");
            dirFile.mkdir();//建立資料夾
        }

        File dir2File = new File(Environment.getExternalStorageDirectory() +"/"+shared_root_path+"/assets/", "motion_bin");
        Log.d("FileUtil", "check folder 2 " + dir2File.getAbsolutePath());
        if (!dir2File.exists()) {//如果資料夾不存在
            Log.d("FileUtil", "folder 2 not exist, create it");
            dir2File.mkdir();//建立資料夾
        }
        return shared_root_path+"/assets/motion_bin" ;
    }
    /**
     * Clone files to nuwa assets folder
     * @param context
     * @param srcPath the source app/assets files path
     * @param dstPath the target nuwa external assets path
     */
    public static void copyAssetsToDst(Context context, String srcPath, String dstPath) {
        try {
            String fileNames[] = context.getAssets().list(srcPath);
            if (fileNames.length > 0) {
                File file = new File(Environment.getExternalStorageDirectory(), dstPath);///assets/motion_bin/
                if (!file.exists()) file.mkdirs();
                for (String fileName : fileNames) {
                    if (!srcPath.equals("")) { // assets 文件夹下的目录
                        Log.d("FileUtil", "copy file " + srcPath + " to " + dstPath);
                        copyAssetsToDst(context, srcPath + File.separator + fileName, dstPath + File.separator + fileName);
                    } else { // assets 文件夹
                        copyAssetsToDst(context, fileName, dstPath + File.separator + fileName);
                    }
                }
            } else {
                File outFile = new File(Environment.getExternalStorageDirectory() , dstPath);
                Log.d("FileUtil", "copy asset " + srcPath + " to " + dstPath);
                InputStream is = context.getAssets().open(srcPath);
                FileOutputStream fos = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024];
                int byteCount;
                while ((byteCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
            }
            //isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            //errorStr = e.getMessage();
            //isSuccess = false;
        }
    }
}
