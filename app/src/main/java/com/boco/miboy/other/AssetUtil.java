package com.boco.miboy.other;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AssetUtil {
    private static final String TAG = AssetUtil.class.getSimpleName();
    private static AssetUtil instance;
    private static final String QUESTIONS_PATH = "questions";
    private static final int[] QUESTION_FOLDERS = new int[]{
            1, 2, 3, 4, 5, 6, 7
    };
    private Map<Integer, List<Drawable>> imageMap;

    private AssetUtil() {
    }

    public static AssetUtil getInstance() {
        if (instance == null) {
            instance = new AssetUtil();

        }
        return instance;
    }

    public Map<Integer, List<Drawable>> getQuestions(Context context) {
        Map<Integer, List<Drawable>> images = new TreeMap<>();
        if (imageMap == null) {
            try {
                for (int folderName : QUESTION_FOLDERS) {
                    String pathToQuestion = QUESTIONS_PATH + "/" + folderName;
                    List<String> names = listNames(pathToQuestion, context);
                    List<Drawable> drawables = new ArrayList<>();
                    for (int j = 0; j < names.size(); j++) {
                        String image = names.get(j);
//                    Log.i(TAG, "getQuestions: pos = " + folderName + " name " + image);
                        InputStream is = context.getResources().getAssets().open(pathToQuestion + "/" + image);
                        Drawable drawable = Drawable.createFromStream(is, null);
                        drawables.add(drawable);
                        is.close();
                    }
                    images.put(folderName, drawables);
                }
                imageMap = images;
            } catch (Exception e) {
                System.out.println("getQuestions() " + e);
                Log.e(TAG, "getQuestions: ", e);
            }
        } else {
            return imageMap;
        }
        return images;
    }

    private List<String> listNames(String path, Context context) {
        List<String> paths = new ArrayList<>();
        try {
            Resources res = context.getResources();
            AssetManager am = res.getAssets();
            String fileList[] = am.list(path);
            if (fileList != null) {
                paths = Arrays.asList(fileList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paths;
    }
}