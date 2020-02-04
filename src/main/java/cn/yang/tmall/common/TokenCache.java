package cn.yang.tmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Yangtz
 * @ClassName: TokenCache
 * @Description:
 * @create 2020-02-03 15:34
 */
@Slf4j
public class TokenCache {
    public static final String TOKEN_PREFIX = "token_";
    private static LoadingCache<String,String> loadingCache = CacheBuilder.newBuilder().initialCapacity(1000)
            .maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).build(new CacheLoader<String, String>() {
                //默认的数据加载实现，当调用get取值时，如果key没有对应的值，就调用这个方法
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key,String value){
        loadingCache.put(key,value);
    }
    public static String getKey(String key){
        String value = null;
        try {
            value = loadingCache.get(key);
            if("null".equals(value)){
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            e.printStackTrace();
            log.error("localCache get error:",e);
        }
        return null;
    }
}
