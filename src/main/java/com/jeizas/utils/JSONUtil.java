package com.jeizas.utils;

/**
 * @author jeizas
 * @date 2018-12-19 22:52
 */
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

@Slf4j
public class JSONUtil {
    private static final Gson GSON = handleMapDouble(getBaseBuilder()).create();
    private static final Gson GSON_PRETTY = handleMapDouble(getBaseBuilder()).setPrettyPrinting().create();

    public JSONUtil() {
    }

    public static GsonBuilder getBaseBuilder() {
        GsonBuilder gb = new GsonBuilder();
        gb.setExclusionStrategies(new ExclusionStrategy[]{new ExclusionStrategy() {
            public boolean shouldSkipField(FieldAttributes f) {
                JSONUtil.ExcludeField annotation = (JSONUtil.ExcludeField)f.getAnnotation(JSONUtil.ExcludeField.class);
                return annotation != null;
            }

            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }});
        gb.registerTypeAdapter(Integer.class, new JSONUtil.IntegerTypeAdapter());
        gb.registerTypeAdapter(Integer.TYPE, new JSONUtil.IntegerTypeAdapter(true));
        gb.registerTypeAdapter(Long.class, new JSONUtil.LongTypeAdapter());
        gb.registerTypeAdapter(Long.TYPE, new JSONUtil.LongTypeAdapter(true));
        gb.registerTypeAdapter(Date.class, new JSONUtil.DateGsonAdapter()).registerTypeAdapter(Timestamp.class, new JSONUtil.DateGsonAdapter()).registerTypeAdapter(java.sql.Date.class, new JSONUtil.DateGsonAdapter()).setDateFormat(1);
        gb.registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
            public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                return src == (double)src.longValue() ? new JsonPrimitive(src.longValue()) : new JsonPrimitive(src);
            }
        });
        gb.registerTypeHierarchyAdapter(EnumValue.class, new JSONUtil.EnumValueGsonAdapter());
        gb.disableHtmlEscaping();
        return gb;
    }

    public static GsonBuilder handleMapDouble(GsonBuilder gb) {
        gb.registerTypeAdapter((new TypeToken<Map<String, Object>>() {
        }).getType(), new JSONUtil.DoubleToIntMapTypeAdapter());
        return gb;
    }

    public static Gson getGson() {
        return GSON;
    }

    public static Gson getGsonPretty() {
        return GSON_PRETTY;
    }

    public static <T> T json2Obj(String json, @NonNull JSONType<T> type) {
        return json2Obj(json, type.type);
    }

    public static <T> T json2Obj(String json, @NonNull Type type) {
        if (isEmptyJson(json)) {
            return null;
        } else {
            try {
                return GSON.fromJson(json, type);
            } catch (Exception var3) {
                var3.printStackTrace();
                return null;
            }
        }
    }

    public static <T> T json2Obj(String json, @NonNull Class<T> c) {
        if (isEmptyJson(json)) {
            return null;
        } else {
            try {
                return GSON.fromJson(json, c);
            } catch (Exception var3) {
                var3.printStackTrace();
                return null;
            }
        }
    }

    /** @deprecated */
    @Deprecated
    public static <T> T json2Obj(JsonElement json, JSONType<T> type) {
        return json == null ? null : GSON.fromJson(json, type.getType());
    }

    /** @deprecated */
    @Deprecated
    public static <T> T json2Obj(JsonElement json, Class<T> type) {
        return json == null ? null : GSON.fromJson(json, type);
    }

    /** @deprecated */
    @Deprecated
    public static <T> T map2Obj(Map map, Class<T> c) {
        if (StringUtil.isEmpty(map)) {
            return null;
        } else {
            try {
                return GSON.fromJson(GSON.toJson(map), c);
            } catch (Exception var3) {
                var3.printStackTrace();
                return null;
            }
        }
    }

    public static <T> T obj2Obj(Object object, Class<T> c) {
        return StringUtil.isNull(object) ? null : GSON.fromJson(GSON.toJson(object), c);
    }

    public static <T> T obj2Obj(Object object, JSONType<T> type) {
        return obj2Obj(object, type.type);
    }

    public static <T> T obj2Obj(Object object, Type type) {
        if (StringUtil.isNull(object)) {
            return null;
        } else {
            try {
                return GSON.fromJson(GSON.toJson(object), type);
            } catch (Exception var3) {
                var3.printStackTrace();
                return null;
            }
        }
    }

    private static boolean isEmptyJson(String json) {
        return StringUtil.isEmpty(json) || json.matches("\\{\\s*\\}");
    }

    public static <T> String obj2Json(T t) {
        return GSON.toJson(t);
    }

    public static <T> String obj2PrettyJson(T t) {
        return GSON_PRETTY.toJson(t);
    }

    public static boolean validate(String json) {
        return (new JSONValidator()).validate(json);
    }

    public static String getJsonNodeValue(JsonObject jsonObject, String attrs) {
        int index = attrs.indexOf(46);
        if (index == -1) {
            return jsonObject != null && jsonObject.get(attrs) != null ? jsonObject.get(attrs).getAsString() : "";
        } else {
            JsonElement pEle = jsonObject.get(attrs.substring(0, index));
            return pEle == null ? "" : getJsonNodeValue(pEle.getAsJsonObject(), attrs.substring(index + 1));
        }
    }

    public static class IntegerTypeAdapter extends TypeAdapter<Integer> {
        private boolean isPrimitive;

        IntegerTypeAdapter() {
        }

        IntegerTypeAdapter(boolean isPrimitive) {
            this.isPrimitive = isPrimitive;
        }

        private Integer getEmptyValue() {
            return this.isPrimitive ? 0 : null;
        }

        public void write(JsonWriter out, Integer value) {
            try {
                if (value == null) {
                    value = this.getEmptyValue();
                }

                out.value(value);
            } catch (Exception var4) {
                var4.printStackTrace();
            }

        }

        public Integer read(JsonReader in) {
            Integer value = this.getEmptyValue();

            try {
                switch(in.peek()) {
                    case STRING:
                        value = Integer.parseInt(in.nextString());
                        break;
                    case NUMBER:
                    default:
                        value = in.nextInt();
                        break;
                    case BOOLEAN:
                        in.nextBoolean();
                        break;
                    case NULL:
                        in.nextNull();
                }
            } catch (Exception var4) {
//                JSONUtil.logger.debugThrowable(() -> {
//                    return var4;
//                });
            }

            return value;
        }
    }

    public static class DoubleTypeAdapter extends TypeAdapter<Double> {
        private boolean isPrimitive;

        DoubleTypeAdapter() {
        }

        DoubleTypeAdapter(boolean isPrimitive) {
            this.isPrimitive = isPrimitive;
        }

        private Double getEmptyValue() {
            return this.isPrimitive ? 0.0D : null;
        }

        public void write(JsonWriter out, Double value) {
            try {
                if (value == null) {
                    value = this.getEmptyValue();
                }

                if (value == (double)value.longValue()) {
                    out.value(value.longValue());
                } else {
                    out.value(value);
                }
            } catch (Exception var4) {
                var4.printStackTrace();
            }

        }

        public Double read(JsonReader in) {
            Double value = this.getEmptyValue();

            try {
                switch(in.peek()) {
                    case STRING:
                        value = Double.parseDouble(in.nextString());
                        break;
                    case NUMBER:
                    default:
                        value = in.nextDouble();
                        break;
                    case BOOLEAN:
                        in.nextBoolean();
                        break;
                    case NULL:
                        in.nextNull();
                }
            } catch (Exception var4) {
//                JSONUtil.logger.debugThrowable(() -> {
//                    return var4;
//                });
            }

            return value;
        }
    }

    public static class LongTypeAdapter extends TypeAdapter<Long> {
        private boolean isPrimitive;

        LongTypeAdapter() {
        }

        LongTypeAdapter(boolean isPrimitive) {
            this.isPrimitive = isPrimitive;
        }

        private Long getEmptyValue() {
            return this.isPrimitive ? 0L : null;
        }

        public void write(JsonWriter out, Long value) {
            try {
                if (value == null) {
                    value = this.getEmptyValue();
                }

                out.value(value);
            } catch (Exception var4) {
                var4.printStackTrace();
            }

        }

        public Long read(JsonReader in) {
            Long value = this.getEmptyValue();

            try {
                switch(in.peek()) {
                    case STRING:
                        value = Long.parseLong(in.nextString());
                        break;
                    case NUMBER:
                    default:
                        value = in.nextLong();
                        break;
                    case BOOLEAN:
                        in.nextBoolean();
                        break;
                    case NULL:
                        in.nextNull();
                }
            } catch (Exception var4) {
//                JSONUtil.logger.debugThrowable(() -> {
//                    return var4;
//                });
            }

            return value;
        }
    }

    public static class DoubleToIntMapTypeAdapter extends TypeAdapter<Object> {
        private final TypeAdapter<Object> delegate = JSONUtil.getBaseBuilder().create().getAdapter(new TypeToken<Object>() {
        });

        public DoubleToIntMapTypeAdapter() {
        }

        public void write(JsonWriter out, Object value) throws IOException {
            this.delegate.write(out, value);
        }

        public Object read(JsonReader in) throws IOException {
            JsonToken token = in.peek();
            switch(token) {
                case BEGIN_ARRAY:
                    List<Object> list = new ArrayList();
                    in.beginArray();

                    while(in.hasNext()) {
                        list.add(this.read(in));
                    }

                    in.endArray();
                    return list;
                case BEGIN_OBJECT:
                    Map<String, Object> map = new LinkedTreeMap();
                    in.beginObject();

                    while(in.hasNext()) {
                        map.put(in.nextName(), this.read(in));
                    }

                    in.endObject();
                    return map;
                case STRING:
                    return in.nextString();
                case NUMBER:
                    double dbNum = in.nextDouble();
                    if (dbNum > 9.223372036854776E18D) {
                        return dbNum;
                    } else {
                        long lngNum = (long)dbNum;
                        if (dbNum == (double)lngNum) {
                            if (lngNum > 2147483647L) {
                                return lngNum;
                            }

                            return (int)lngNum;
                        }

                        return dbNum;
                    }
                case BOOLEAN:
                    return in.nextBoolean();
                case NULL:
                    in.nextNull();
                    return null;
                default:
                    throw new IllegalStateException();
            }
        }
    }

    public static class BeanNullAdapter<T> extends TypeAdapter<T> {
        private final Class<T> clazz;

        public BeanNullAdapter(Class<T> clazz) {
            this.clazz = clazz;
        }

        public T read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            } else {
                return JSONUtil.json2Obj(reader.nextString(), this.clazz);
            }
        }

        public void write(JsonWriter writer, T value) throws IOException {
            if (value == null) {
                try {
                    value = this.clazz.newInstance();
                } catch (Exception var9) {
                    var9.printStackTrace();
                }
            }

            if (value != null) {
                if (this.clazz.getPackage().getName().startsWith("org.sean.framework")) {
                    writer.beginObject();
                    Field[] fileds = this.clazz.getFields();
                    Package pkg = null;
                    Field[] var5 = fileds;
                    int var6 = fileds.length;

                    for(int var7 = 0; var7 < var6; ++var7) {
                        Field field = var5[var7];
                        pkg = field.getType().getPackage();
                        if (pkg != null && pkg.getName().startsWith("org.sean.framework")) {
                            System.out.println("");
                        }
                    }

                    writer.endObject();
                }

                TypeAdapter<Object> typeAdapter = JSONUtil.GSON.getAdapter((Class<Object>) value.getClass());
                if (typeAdapter instanceof ObjectTypeAdapter) {
                    writer.beginObject();
                    writer.endObject();
                } else {
                    typeAdapter.write(writer, value);
                }
            }
        }
    }

    public static class NullBeanToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        public NullBeanToEmptyAdapterFactory() {
        }

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType.getPackage().getName().startsWith("org.sean.framework")) {
                try {
                    return new JSONUtil.BeanNullAdapter(rawType);
                } catch (Exception var5) {
//                    JSONUtil.logger.printStackTrace(var5);
                    var5.printStackTrace();
                }
            }

            return null;
        }
    }

    public static class EnumValueGsonAdapter implements JsonSerializer<EnumValue>, JsonDeserializer<EnumValue> {
        public EnumValueGsonAdapter() {
        }

        public JsonElement serialize(EnumValue src, Type typeOfSrc, JsonSerializationContext context) {
            if (!(src instanceof Enum)) {
                throw new UnsupportedOperationException("EnumValue mush Emnu class");
            } else {
                Object obj = src.getValue();
                if (obj instanceof String) {
                    return new JsonPrimitive((String)src.getValue());
                } else if (obj instanceof Number) {
                    return new JsonPrimitive((Number)src.getValue());
                } else if (obj instanceof Character) {
                    return new JsonPrimitive((Character)src.getValue());
                } else if (obj instanceof Boolean) {
                    return new JsonPrimitive((Boolean)src.getValue());
                } else {
                    throw new UnsupportedOperationException("Generic Just Support String,Number,Character,Boolean");
                }
            }
        }

        public EnumValue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Class classOfT = null;

            try {
                classOfT = typeOfT instanceof Class ? (Class)typeOfT : Class.forName(typeOfT.getTypeName());
            } catch (ClassNotFoundException var7) {
//                JSONUtil.logger.printStackTrace(var7);
                var7.printStackTrace();
            }

            if (classOfT != null && classOfT.isEnum()) {
                try {
                    return this.locateEnumValue(classOfT, json.getAsJsonPrimitive().getAsInt());
                } catch (Exception var6) {
                    return this.locateEnumValue(classOfT, json.getAsJsonPrimitive().getAsString());
                }
            } else {
                throw new UnsupportedOperationException("EnumValue must Enum class");
            }
        }

        private EnumValue locateEnumValue(Class classOfT, String value) {
            EnumValue[] enums = (EnumValue[])classOfT.getEnumConstants();
            EnumValue[] var4 = enums;
            int var5 = enums.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                EnumValue en = var4[var6];
                if (en.name().equals(value)) {
                    return en;
                }
            }

            return null;
        }

        private EnumValue locateEnumValue(Class classOfT, int value) {
            EnumValue[] enums = (EnumValue[])classOfT.getEnumConstants();
            EnumValue[] var4 = enums;
            int var5 = enums.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                EnumValue en = var4[var6];
                if (en.getValue().equals(value)) {
                    return en;
                }
            }

            throw new IllegalArgumentException("unknow value：" + value + ",class:" + classOfT.getName());
        }
    }

    public static class DateGsonAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
        public DateGsonAdapter() {
        }

        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getTime());
        }

        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new Date(json.getAsJsonPrimitive().getAsLong());
        }
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface ExcludeField {
    }
}

