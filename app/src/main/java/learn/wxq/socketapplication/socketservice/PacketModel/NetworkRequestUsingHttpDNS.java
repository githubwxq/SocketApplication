package learn.wxq.socketapplication.socketservice.PacketModel;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.alibaba.sdk.android.httpdns.DegradationFilter;
import com.alibaba.sdk.android.httpdns.HttpDns;
import com.alibaba.sdk.android.httpdns.HttpDnsService;


import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import learn.wxq.socketapplication.socketservice.Global;

/**
 * 堆请求进行dns解析
 */
public class NetworkRequestUsingHttpDNS {

    private static HttpDnsService httpdns;
    public static String accountID = "179979";
    private static String[] hosts = {"api.juziwl.cn", "exiaoxin.com", "juziwl.com",
            "im.imexue.com", "im.exiaoxin.com", "im.juziwl.com", "api.exiaoxin.com",
            "platform.imexue.com", "mp.imexue.com", "platform.exiaoxin.com"};

    private static String[] testHosts = {"api.juziwl.cn", "exiaoxin.com", "juziwl.com",
            "im.imexue.com", "im.exiaoxin.com", "im.juziwl.com", "test.juziwl.com",
            "platform.imexue.com", "mp.imexue.com", "platform.exiaoxin.com"};

    public static String main(final Context ctx, final String host) {
        String newUrl = "";
        try {
            // 设置APP Context和Account ID，并初始化HTTPDNS
            if (httpdns == null) {
                httpdns = HttpDns.getService(ctx, accountID);
            }
            // DegradationFilter用于自定义降级逻辑
            // 通过实现shouldDegradeHttpDNS方法，可以根据需要，选择是否降级
            DegradationFilter filter = new DegradationFilter() {
                @Override
                public boolean shouldDegradeHttpDNS(String hostName) {
                    // 此处可以自定义降级逻辑，例如www.taobao.com不使用HttpDNS解析
                    // 参照HttpDNS API文档，当存在中间HTTP代理时，应选择降级，使用Local DNS
                    return detectIfProxyExist(ctx);
                }
            };
            // 将filter传进httpdns，解析时会回调shouldDegradeHttpDNS方法，判断是否降级
            httpdns.setDegradationFilter(filter);
            // 设置预解析域名列表
//            httpdns.setPreResolveHosts(new ArrayList<>(Arrays.asList(hosts)));
            httpdns.setExpiredIPEnabled(true);
            httpdns.setPreResolveAfterNetworkChanged(true);

            // 发送网络请求
            String originalUrl = host;
            URL url = new URL(originalUrl);
            String ip = httpdns.getIpByHostAsync(url.getHost());
            showLog("host==" + host);
            showLog("ip===main=====" + ip);
            if (ip != null) {
                newUrl = originalUrl.replaceFirst(url.getHost(), ip);
            } else {
                newUrl = host;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newUrl;
    }

    public static String mainSocket(final Context ctx, final String host) {
        String ip = "";
        try {
            // 设置APP Context和Account ID，并初始化HTTPDNS
            if (httpdns == null) {
                httpdns = HttpDns.getService(ctx, accountID);
            }
            // DegradationFilter用于自定义降级逻辑
            // 通过实现shouldDegradeHttpDNS方法，可以根据需要，选择是否降级
            DegradationFilter filter = new DegradationFilter() {
                @Override
                public boolean shouldDegradeHttpDNS(String hostName) {
                    // 此处可以自定义降级逻辑，例如www.taobao.com不使用HttpDNS解析
                    // 参照HttpDNS API文档，当存在中间HTTP代理时，应选择降级，使用Local DNS
                    return detectIfProxyExist(ctx);
                }
            };
            // 将filter传进httpdns，解析时会回调shouldDegradeHttpDNS方法，判断是否降级
            httpdns.setDegradationFilter(filter);
            // 设置预解析域名列表
//            httpdns.setPreResolveHosts(new ArrayList<>(Arrays.asList(hosts)));
            httpdns.setExpiredIPEnabled(true);
            httpdns.setPreResolveAfterNetworkChanged(true);
            // 发送网络请求
            String originalUrl = "http://" + host;
            URL url = new URL(originalUrl);
            ip = httpdns.getIpByHostAsync(url.getHost());
            if (ip == null) {
                ip = host;
            }
            showLog("host==" + host);
            showLog("ip===mainXmpp=====" + ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }

    public static void setPreResolveHosts(Context ctx) {
        if (httpdns == null) {
            httpdns = HttpDns.getService(ctx, accountID);
        }
        httpdns.setPreResolveHosts(new ArrayList<>(Arrays.asList(Global.UrlApi.contains("test") ? testHosts : hosts)));
    }

    /**
     * 检测系统是否已经设置代理，请参考HttpDNS API文档。
     */
    @SuppressWarnings("deprecation")
    public static boolean detectIfProxyExist(Context ctx) {
        boolean IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyHost;
        int proxyPort;
        if (IS_ICS_OR_LATER) {
            proxyHost = System.getProperty("http.proxyHost");
            String port = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt(port != null ? port : "-1");
        } else {
            proxyHost = android.net.Proxy.getHost(ctx);
            proxyPort = android.net.Proxy.getPort(ctx);
        }
        return proxyHost != null && proxyPort != -1;
    }

    public static void showLog(String content) {
        showLog(content, null);
    }

    public static void showLog(String content, Throwable ex) {
        if (ex == null) {
            Log.i("wxq",content);
      //      LogUtil.i(content);
        } else {
       //     LogUtil.i(content, ex);
        }
    }
}
