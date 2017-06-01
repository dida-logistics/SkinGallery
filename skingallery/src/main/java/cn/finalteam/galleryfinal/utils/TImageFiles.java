package cn.finalteam.galleryfinal.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;

import cn.finalteam.galleryfinal.R;

/**
 * 图片类型工具类
 * Created by WangCheng on 2017/3/1.
 */

public class TImageFiles {
    /**
     * 检查文件类型是否是图片
     *
     * @param minType 图片类型
     * @return
     */
    public static boolean checkMimeType(Context context, String minType) {
        boolean isPicture = !TextUtils.isEmpty(minType) && (".jpg|.gif|.png|.bmp|.jpeg|.webp|".contains(minType.toLowerCase()));
        if (!isPicture) {
            Toast.makeText(context, context.getResources().getText(R.string.tip_type_not_image), Toast.LENGTH_SHORT).show();
        }
        return isPicture;
    }

    /**
     * To find out the extension of required object in given uri
     * Solution by http://stackoverflow.com/a/36514823/1171484
     */
    public static String getMimeType(Context context, Uri uri) {
        String extension;
        //Check uri format to avoid null
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            //If scheme is a content
            extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(context.getContentResolver().getType(uri));
            if (TextUtils.isEmpty(extension))
                extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values
            // on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
            if (TextUtils.isEmpty(extension))
                extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(context.getContentResolver().getType(uri));
        }
        if (TextUtils.isEmpty(extension)) {
            extension = getMimeTypeByFileName(TUriParse.getFileWithUri(uri, context).getName());
        }
        return extension;
    }

    public static String getMimeTypeByFileName(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }
}
