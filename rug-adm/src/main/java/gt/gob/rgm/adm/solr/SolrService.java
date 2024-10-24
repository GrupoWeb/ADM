package gt.gob.rgm.adm.solr;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface SolrService {
	@Multipart
	@POST("update")
	Call<ResponseBody> indexFile(@Part("description") RequestBody description, @Part MultipartBody.Part file, @Query("commitWithin") Integer commitTime, @Query("overwrite") Boolean overwrite);
	
	@GET("select")
	Call<SolrResult> search(@Query("q") String query);
}
