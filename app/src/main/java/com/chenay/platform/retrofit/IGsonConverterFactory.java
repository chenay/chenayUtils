package com.chenay.platform.retrofit;

import android.annotation.SuppressLint;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.ByteString;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author Y.Chen5
 */
public class IGsonConverterFactory extends Converter.Factory {

    /**
     * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static IGsonConverterFactory create() {
        return create(new Gson());
    }

    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
    public static IGsonConverterFactory create(Gson gson) {
        if (gson == null) {
            throw new NullPointerException("gson == null");
        }
        return new IGsonConverterFactory(gson);
    }

    private final Gson gson;

    private IGsonConverterFactory(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        final TypeToken<?> type1 = TypeToken.get(type);
        TypeAdapter<?> adapter = gson.getAdapter(type1);

        return new IGsonResponseBodyConverter<>(gson, adapter, type1);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new IGsonRequestBodyConverter<>(gson, adapter);
    }


    private class IGsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private static final String TAG = "IGsonRequestBodyConverter";
        private  final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
        private  final Charset UTF_8 = Charset.forName("UTF-8");

        private final Gson gson;
        private final TypeAdapter<T> adapter;

        IGsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @SuppressLint("LongLogTag")
        @Override
        public RequestBody convert(T value) throws IOException {
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            JsonWriter jsonWriter = gson.newJsonWriter(writer);
            adapter.write(jsonWriter, value);
            jsonWriter.close();
            final ByteString content = buffer.readByteString();
            String str = content.utf8();
            str=  EncryptManager.newInstance().encrypt(str);
            final RequestBody requestBody = RequestBody.create(MEDIA_TYPE, str);
            return requestBody;
        }
    }

    private class IGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private static final String TAG = "IGsonResponseBodyConverter";
        private final Gson gson;
        private final TypeAdapter<T> adapter;
        private final TypeToken<?> type;

        IGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, TypeToken<?> type) {
            this.gson = gson;
            this.adapter = adapter;
            this.type = type;
        }

        @Nullable
        @Override
        public T convert(ResponseBody value) throws IOException {
            try {
                String string = value.string();
                string = EncryptManager.newInstance().decrypt(string);//解密
                final T read;
                read = new Gson().fromJson(string, type.getType());
//            final Reader reader = value.charStream();
//            JsonReader jsonReader = gson.newJsonReader(reader);
//            final T read = adapter.read(jsonReader);
                return read;

            } finally {
                value.close();
            }
        }
    }
}
