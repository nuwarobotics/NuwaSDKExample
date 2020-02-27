package com.nuwarobotics.example.util;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ActivityUtils {
    public static final String TAG = "ActivityUtils";
    public static final String KEY_XML_ROOT = "ActivityList";
    public static final String KEY_LABEL = "Label";
    public static final String KEY_NAME = "name";

    public static ArrayList<ActivityBean> pullXML(Context context, String fileName) {
        ArrayList<ActivityBean> activityBeanList = null;
        ActivityBean activityBean = null;
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream, "utf-8");
            int type = parser.getEventType();
            while (type != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    //TAG: START
                    case XmlPullParser.START_TAG:
                        if (KEY_XML_ROOT.equals(parser.getName())) {
                            activityBeanList = new ArrayList<>();
                        } else if (KEY_LABEL.equals(parser.getName())) {
                            activityBean = new ActivityBean();
                            //fetch name
                            String name = parser.getAttributeValue(null,KEY_NAME);
                            Log.d(TAG, "name = " + name);
                            activityBean.setName(name);
                            //fetch label
                            String label = parser.nextText();
                            Log.d(TAG, "label = " + label);
                            activityBean.setLabel(label);
                            activityBeanList.add(activityBean);
                        }
                        break;
                    //TAG: END
                    case XmlPullParser.END_TAG:
                        break;
                }
                type = parser.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        Log.d(TAG, activityBeanList.toString());
        return activityBeanList;
    }
}
