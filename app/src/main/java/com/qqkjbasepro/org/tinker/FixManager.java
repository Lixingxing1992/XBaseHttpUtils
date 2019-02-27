package com.qqkjbasepro.org.tinker;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by lixingxing on 2018/5/11.
 */
public class FixManager {

    public void loadDex(Context context){
        File filesDir = context.getDir("odex",Context.MODE_PRIVATE);
        File[] listFiles = filesDir.listFiles();
        for (File file : listFiles) {
            if(file.getName().endsWith(".dex")){
                try {
                    // 1.还原系统 Element[] 数组
                    PathClassLoader pathClassLoader = (PathClassLoader)context.getClassLoader();
                    Class baseDexClassLoader = Class.forName("dalvik.system.BaseDexClassLoader");
                    Field pathListFiled = baseDexClassLoader.getDeclaredField("pathList");
                    pathListFiled.setAccessible(true);
                    Object pathListObject = pathListFiled.get(pathClassLoader);

                    Class systemPathClass = Class.forName("dalvik.system.DexPathList");
                    Field systemElementField = systemPathClass.getDeclaredField("dexElements");
                    systemElementField.setAccessible(true);
                    // Element[]
                    Object systemElements = systemElementField.get(pathListObject);


                    // 2. 将下载的dex 转换成Element[]
                    String dir = file.getParent() + File.separator + "opt";
                    File fileDir = new File(dir);
                    if(!fileDir.exists()){
                        fileDir.mkdirs();
                    }
                    // 加载本地 dex
                    DexClassLoader dexClassLoader = new DexClassLoader(file.getAbsolutePath(),dir,
                            null,context.getClassLoader());

                    Class baseDexClassLoader2 = Class.forName("dalvik.system.BaseDexClassLoader");
                    Field pathListFiled2 = baseDexClassLoader2.getDeclaredField("pathList");
                    pathListFiled2.setAccessible(true);
                    Object pathListObject2 = pathListFiled2.get(dexClassLoader);

                    Class systemPathClass2 = Class.forName("dalvik.system.DexPathList");
                    Field systemElementField2 = systemPathClass2.getDeclaredField("dexElements");
                    systemElementField2.setAccessible(true);
                    // Element[]
                    Object myElements = systemElementField.get(pathListObject2);


                    //3. 将下载的 Element[] 和系统还原的Element[] 进行合并成为一个新的Element[],
                    //      并且保证下载的Elment[]要在前面
                    int systemtLength = Array.getLength(systemElements);
                    int myLength = Array.getLength(myElements);
                    int newLenth = systemtLength+myLength;
                    Class singleElementClass = systemElements.getClass().getComponentType();
                    Object newElementArray = Array.newInstance(singleElementClass,newLenth);
                    for (int i = 0; i < newLenth; i++) {
                        if(i < myLength){
                            Array.set(newElementArray,i,Array.get(myElements,i));
                        }else{
                            Array.set(newElementArray,i,Array.get(systemElements,i-myLength));
                        }
                    }
                    //4. 融合完毕
                    systemElementField.set(pathListObject,newElementArray);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
