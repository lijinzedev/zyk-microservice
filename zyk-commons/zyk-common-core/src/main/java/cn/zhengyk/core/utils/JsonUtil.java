package cn.zhengyk.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * @author yakai
 */
@Slf4j
public class JsonUtil {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 对象映射
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(DATE_FORMAT));
        OBJECT_MAPPER.setSerializationInclusion(Include.NON_NULL);
    }


    /**
     * Java对象转换为Json串
     *
     * @param obj Java对象
     * @return Json串
     */
    public static String toJson(Object obj) {
        String rst;
        if (obj == null || obj instanceof String) {
            return (String) obj;
        }
        try {
            rst = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("将Java对象转换成Json串出错！");
            throw new RuntimeException("将Java对象转换成Json串出错！", e);
        }
        return rst;
    }

    /**
     * Json串转换为Java对象
     *
     * @param json Json串
     * @param type Java对象类型
     * @return Java对象
     */
    public static <T> T fromJson(String json, Class<T> type) {
        T rst;
        try {
            rst = OBJECT_MAPPER.readValue(json, type);
        } catch (Exception e) {
            log.error("Json串转换成对象出错：{}", json);
            throw new RuntimeException("Json串转换成对象出错!", e);
        }
        return rst;
    }

    /**
     * Json串转换为Java对象
     * <br>使用引用类型，适用于List&ltObject&gt、Set&ltObject&gt 这种无法直接获取class对象的场景
     * <br>使用方法：TypeReference ref = new TypeReference&ltList&ltInteger&gt&gt(){};
     *
     * @param json    Json串
     * @param typeRef Java对象类型引用
     * @return Java对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String json, TypeReference<T> typeRef) {
        T rst;
        try {
            rst = OBJECT_MAPPER.readValue(json, typeRef);
        } catch (Exception e) {
            log.error("Json串转换成对象出错：{}", json);
            throw new RuntimeException("Json串转换成对象出错!", e);
        }
        return rst;
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, Object> fromJsonToMap(String json) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            map = OBJECT_MAPPER.readValue(json, map.getClass());
        } catch (IOException e) {
            log.error("Json串转换成对象出错：{}", json);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, Object> toMap(String json) {
        HashMap<String, Object> map;
        try {
            map = OBJECT_MAPPER.readValue(json, HashMap.class);
        } catch (Exception e) {
            map = null;
            log.error("Json串转换成对象出错：{}", json);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static List<HashMap<String, Object>> fromJsonToList(String json) {
        List<HashMap<String, Object>> list;
        try {
            list = OBJECT_MAPPER.readValue(json, List.class);
        } catch (IOException e) {
            log.error("Json串转换成对象出错：{}", json);
            throw new RuntimeException("Json串转换成List出错!", e);
        }
        return list;
    }

    public static byte[] writeValueAsBytes(Object obj) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsBytes(obj);
    }

    public static <T> T writeValueAsBytes(byte[] bytes, Class<T> tClass) throws IOException {
        return OBJECT_MAPPER.readValue(bytes, tClass);
    }
}
