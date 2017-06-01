package cn.finalteam.galleryfinal.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.TException;
import cn.finalteam.galleryfinal.model.TExceptionType;

import static android.content.ContentValues.TAG;


/**
 * Uri解析工具类
 * Author: JPH
 * Date: 2015/8/26 0026 16:23
 */
public class TUriParse {

    /**
     * 创建一个用于拍照图片输出路径的Uri (FileProvider)
     *
     * @param context 上下文
     * @return
     */
    public static Uri getUriForFile(Context context, File file) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            return FileProvider.getUriForFile(context, GalleryFinal.getAuthorities(), file);
        } else {
            return Uri.fromFile(file);
        }
    }

    /**
     * 通过URI获取文件的路径
     *
     * @param uri     拍照是使用的URI路径
     * @param context 上下文
     */
    public static String getFilePathWithUri(Uri uri, Context context) throws TException {
        if (uri == null) {
            Log.w(TAG, "uri is null,activity may have been recovered?");
            throw new TException(TExceptionType.TYPE_URI_NULL);
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            File picture = getFileWithUri(uri, context);
            String picturePath = picture == null ? null : picture.getPath();
            if (TextUtils.isEmpty(picturePath))
                throw new TException(TExceptionType.TYPE_URI_PARSE_FAIL);
            if (!TImageFiles.checkMimeType(context, TImageFiles.getMimeType(context, uri)))
                throw new TException(TExceptionType.TYPE_NOT_IMAGE);
            return picturePath;
        } else {
            return uri.getPath();
        }

    }

    /**
     * 通过URI获取文件
     *
     * @param uri     拍照是使用的URI路径
     * @param context 上下文
     */
    public static File getFileWithUri(Uri uri, Context context) {
        String picturePath = null;
        String scheme = uri.getScheme();
        if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                if (columnIndex >= 0) {
                    picturePath = cursor.getString(columnIndex);  //获取照片路径
                } else if (TextUtils.equals(uri.getAuthority(), GalleryFinal.getAuthorities())) {
                    picturePath = parseOwnUri(uri);
                }
                cursor.close();
            }
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            picturePath = uri.getPath();
        }
        return TextUtils.isEmpty(picturePath) ? null : new File(picturePath);
    }

    /**
     * 将TakePhoto 提供的Uri 解析出文件绝对路径
     *
     * @param uri
     * @return
     */
    public static String parseOwnUri(Uri uri) {
        if (uri == null) return null;
        String path;
        if (TextUtils.equals(uri.getAuthority(), GalleryFinal.getAuthorities())) {
            path = new File(uri.getPath().replace("camera_photos/", "")).getAbsolutePath();
        } else {
            path = uri.getPath();
        }
        return path;
    }

}
