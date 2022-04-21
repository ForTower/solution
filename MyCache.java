package exam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * @Author lxr
 * @Date 2022/4/21 - 19:37
 */

/*
题目三：缓存设计
设计一个简单缓存工具(key, value)，该工具类主要功能有
    1. 提供基本的读写操作
    2. 支持设置有效期
    3. 对已过期失效的缓存进行定期清理
 */
class MyCache<K, V>{
    // 建立key与有效时间的映射关系，默认key是长期有效
    private HashMap<K, Date> dateRecode = new HashMap<>();
    // 缓存
    private HashMap<K, V> cache = new HashMap<>();

    //设置该key在缓存中的有效时间
    // 传递的时间格式支持的格式形如 2022-4-21 14:30:20
    public void setValidTime(K key, String validTime){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = df.parse(validTime);
            dateRecode.put(key, date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    // 读取缓存
    public V get(K key) throws Exception {
        if (dateRecode.get(key) != null) {
            // 该key在缓存中处于有效期内
            return cache.get(key);
        }
        // 获取当前的时间
        Date curDate = new Date();
        long time1 = curDate.getTime();
        // 获取该key在缓存中的有效时间
        Date validTime = dateRecode.get(curDate);
        long time2 = validTime.getTime();
        if (time2 > time1){
            return cache.get(key);

        }else {
            cache.remove(key);
            throw new Exception("该key在缓存中已经失效，获取失败");
        }
    }

    // 写缓存
    public V put(K key, V value){
        V oldValue = cache.get(key);
        cache.put(key, value);
        return oldValue;
    }
}
