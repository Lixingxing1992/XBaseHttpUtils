package com.app.org.db;

import android.content.Context;

import com.app.org.db.impl.DefaultConverter;
import com.app.org.db.impl.DefaultEncryption;
import com.app.org.db.impl.DefaultSerializer;
import com.app.org.db.impl.DefaultStorage;
import com.app.org.db.interfaces.Converter;
import com.app.org.db.interfaces.Encryption;
import com.app.org.db.interfaces.Serializer;
import com.app.org.db.interfaces.Storage;


/**
 * Created by lixingxing on 2018/7/5.
 */
public class DataStroge {
    private final Context applicationContext;
    private Converter converter;
    private Serializer serializer;
    private Encryption encryption;
    private Storage storage;
    private boolean isExcludeEncrypt;

    public DataStroge(Builder builder) {
        applicationContext = builder.applicationContext;
        converter = builder.converter;
        serializer = builder.serializer;
        encryption = builder.encryption;
        storage = builder.storage;
        isExcludeEncrypt = builder.isExcludeEncrypt;
    }

    public <T> void put(String key,T value){
        //1.序列化
        String json = serializer.serializer(value);
        //2.加入类型信息转化
        String convert = converter.convert(json,value);
        //3.加密
        String encryp = encryption.encrypt(convert);
        //4.存储
        storage.put(key,encryp);
    }

    public <T> T get(String key){
        String data = storage.get(key);
        if(null == data){
            return null;
        }
        String data2 = encryption.descrypt(data);

        return (T)new Object();
    }

    public static final class Builder{

        private final Context applicationContext;
        private Converter converter;
        private Serializer serializer;
        private Encryption encryption;
        private Storage storage;
        private boolean isExcludeEncrypt;

        public Builder(Context context) {
            applicationContext = context;
        }

        public Builder setConverter(Converter converter) {
            this.converter = converter;
            return this;
        }

        public Builder setSerializer(Serializer serializer) {
            this.serializer = serializer;
            return this;
        }

        public Builder setEncryption(Encryption encryption) {
            this.encryption = encryption;
            return this;
        }

        public Builder setStorage(Storage storage) {
            this.storage = storage;
            return this;
        }

        public Builder setExcludeEncrypt(boolean excludeEncrypt) {
            isExcludeEncrypt = excludeEncrypt;
            return this;
        }

        public Builder build(){
            if(converter == null){
                converter = new DefaultConverter();
            }
            if(serializer == null){
                serializer = new DefaultSerializer();
            }
            if(storage == null){
                storage = new DefaultStorage();
            }
            if(encryption == null){
                encryption = new DefaultEncryption();
            }
            return this;
        }
    }
}
