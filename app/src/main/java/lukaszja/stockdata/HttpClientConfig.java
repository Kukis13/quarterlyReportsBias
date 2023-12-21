package lukaszja.stockdata;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import com.github.mizosoft.methanol.CacheControl;
import com.github.mizosoft.methanol.HttpCache;
import com.github.mizosoft.methanol.Methanol;
import com.github.mizosoft.methanol.Methanol.Interceptor;
import com.github.mizosoft.methanol.Methanol.Interceptor.Chain;

public class HttpClientConfig {
	
	public static CacheControl cacheControl = CacheControl.newBuilder()
 		    .maxAge(Duration.ofDays(30))
 		    .build();
	
	public static HttpCache cache = HttpCache.newBuilder()
    	    .cacheOnDisk(Path.of("my-cache-dir"),  500 * 1024 * 1024)
    	    .build();
	
	public static Methanol client = Methanol.newBuilder()
			.backendInterceptor(new Interceptor() {

				@Override
				public <T> HttpResponse<T> intercept(HttpRequest request, Chain<T> chain) throws IOException, InterruptedException {
					Thread.sleep(100);
					return chain.forward(request);
				}

				@Override
				public <T> CompletableFuture<HttpResponse<T>> interceptAsync(HttpRequest request, Chain<T> chain) {
					return chain.forwardAsync(request);
				}
				
			})
    	    .cache(cache)
    	    .build();

}
