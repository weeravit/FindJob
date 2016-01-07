package com.weeravit_it.findjob.findjob.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by Weeravit on 8/10/2558.
 */
public class AppUtils {

    public String getMimeTypeOfUri(Context context, Uri imageUri) {
        return context.getContentResolver().getType(imageUri);
    }

    public String getRealPathForImage(Context context, Uri imageUri) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(imageUri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imgPath = cursor.getString(columnIndex);
        cursor.close();
        return imgPath;
    }

    public String getRealPathForImage(Context context, Intent imageIntent) {
        Uri imageUri = imageIntent.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(imageUri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imgPath = cursor.getString(columnIndex);
        cursor.close();
        return imgPath;
    }

}
