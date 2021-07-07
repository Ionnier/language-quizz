import com.squareup.moshi.Moshi;
import com.squareup.okhttp.*;
import com.squareup.okhttp.Response;

import java.io.IOException;
public class Utils {
    static User user;
    static OkHttpClient client = new OkHttpClient();
    static Moshi moshi = new Moshi.Builder().build();
    public static String apiLink = "http://localhost:3000/";
    static String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    static String postJSON(String url,String json) throws IOException{
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
