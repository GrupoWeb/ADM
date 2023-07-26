package gt.gob.rgm.adm.solr;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
	// TODO: obtener el url de solr de BD
	private static final String BASE_URL = "http://128.5.4.62:8983/solr/rgm/";
	//private static final String BASE_URL = "http://localhost:8983/solr/rgm/";
    //private static final String BASE_URLS = "https://www.google.com";
	private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
		    .connectTimeout(20, TimeUnit.SECONDS)
		    .writeTimeout(60, TimeUnit.SECONDS)
		    .readTimeout(60, TimeUnit.SECONDS)
		    .build();
	private static Retrofit.Builder builder = new Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.client(okHttpClient);
	private static Retrofit retrofit = builder.build();
	private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

	public static <S> S createService(Class<S> serviceClass) {
		return retrofit.create(serviceClass);
	}
}
