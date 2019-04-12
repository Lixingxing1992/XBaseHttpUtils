package com.xhttp.org;

import com.xhttp.lib.BaseHttpUtils;
import com.xhttp.lib.impl.data.TDDataListener;
import com.xhttp.lib.impl.service.TDHttpService;
import com.xhttp.lib.impl.service.YGHttpService;
import com.xhttp.lib.interfaces.IHttpService;

import org.junit.Test;

/**
 * Created by lixingxing on 2019/4/12.
 */
public class ExampleTest {
    @Test
    public void testClass(){
        BaseHttpUtils.init(YGHttpService.class,TDDataListener.class);
//        System.out.print(IHttpService.class.isAssignableFrom(TDHttpService.class));
    }
}
