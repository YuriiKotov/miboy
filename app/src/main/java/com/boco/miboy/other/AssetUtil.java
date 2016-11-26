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
    private Context context;
    private static final String QUESTIONS_PATH = "questions";
    private static final int QUESTION_1 = 1;
    private static final int QUESTION_2 = 2;
    private static final int QUESTION_3 = 3;
    private static final int QUESTION_4 = 4;
    private static final int QUESTION_5 = 5;
    private static final int QUESTION_6 = 6;
    private static final int QUESTION_7 = 7;
    private static final int[] QUESTION_FOLDERS = new int[]{
            QUESTION_1,
            QUESTION_2,
            QUESTION_3,
            QUESTION_4,
            QUESTION_5,
            QUESTION_6,
            QUESTION_7
    };

    public AssetUtil(Context context) {
        this.context = context;
    }

    public Map<Integer, List<Drawable>> getQuestions() {
        Map<Integer, List<Drawable>> smsMap = new TreeMap<>();
        try {
            for (int folderName : QUESTION_FOLDERS) {
                String pathToQuestion = QUESTIONS_PATH + "/" + folderName;
                List<String> names = listNames(pathToQuestion);
                List<Drawable> drawables = new ArrayList<>();
                for (int j = 0; j < names.size(); j++) {
                    String image = names.get(j);
//                    Log.i(TAG, "getQuestions: pos = " + folderName + " name " + image);
                    InputStream is = context.getResources().getAssets().open(pathToQuestion + "/" + image);
                    Drawable drawable = Drawable.createFromStream(is, null);
                    drawables.add(drawable);
                    is.close();
                }
                smsMap.put(folderName, drawables);
            }
        } catch (Exception e) {
            System.out.println("getQuestions() " + e);
            Log.e(TAG, "getQuestions: ", e);
        }
        return smsMap;
    }

    private List<String> listNames(String path) {
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