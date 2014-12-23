package com.hongjing.magicglasses;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wangrui on 12/23/14.
 */
public class MovieService {
    private Context context;

    public MovieService(Context context) {
        this.context = context;
    }

    public List<HashMap<String, String>> getMovies() {
        // 指定要查询的uri资源
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        // 获取ContentResolver
        ContentResolver contentResolver = context.getContentResolver();

        // MediaStore.Video.Thumbnails.DATA:视频缩略图的文件路径
        String[] thumbColumns = { MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID };

        // MediaStore.Video.Media.DATA：视频文件路径；
        // MediaStore.Video.Media.DISPLAY_NAME : 视频文件名，如 testVideo.mp4
        // MediaStore.Video.Media.TITLE: 视频标题 : testVideo
        String[] mediaColumns = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE };

        // 排序
        String sortOrder = MediaStore.Video.Media.DATE_MODIFIED + " desc";

        Cursor cursor = contentResolver.query(uri, mediaColumns, null, null, sortOrder);

        List<HashMap<String, String>> movieList = new ArrayList<HashMap<String, String>>();

        if(cursor == null) {
            Toast.makeText(context, "没有找到可播放视频文件", Toast.LENGTH_LONG).show();
            return movieList;
        }
        if (cursor != null) {
            HashMap<String, String> movieMap = null;
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                movieMap = new HashMap<String, String>();

                movieMap.put("movieId", cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID)).trim());

                movieMap.put("movieData", cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)).trim());

                movieMap.put("movieTitle", cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE)).trim());

                movieMap.put("movieType", cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE)).trim());

                movieMap.put("movieName", cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)).trim());
                int size = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE)));
                int convert_size = size / 1024 /1024;
                movieMap.put("movieSize", convert_size + "M");

                movieList.add(movieMap);
            }
            // 关闭cursor
            cursor.close();
        }
        return movieList;
    }
}
