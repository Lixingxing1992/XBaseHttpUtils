package com.xhttp.lib.interfaces.callback;

import java.io.File;

/**
 * Created by lixingxing on 2019/6/21.
 */
public interface IFileUploadListener {
    void onFileProgress(int position, File file, long curlenth, long total);
}
