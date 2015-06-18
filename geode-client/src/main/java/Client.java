import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.query.FunctionDomainException;
import com.gemstone.gemfire.cache.query.NameResolutionException;
import com.gemstone.gemfire.cache.query.QueryInvocationTargetException;
import com.gemstone.gemfire.cache.query.QueryService;
import com.gemstone.gemfire.cache.query.TypeMismatchException;

import java.util.concurrent.TimeUnit;

/**
 * @author wmarkito
 */
public class Client {

    public static void main(String[] args) {
        ClientCacheFactory clientCacheFactory = connectStandalone(Client.class.getName());
        final ClientCache cache = clientCacheFactory.create();

        long start = System.nanoTime();
        Region region = cache.getRegion("exampleRegion");


        for (int i=0; i < 100; i++)
            region.put(i,i*2);


        QueryService qs = cache.getQueryService();

        try {
            System.out.println(qs.newQuery("select * from /exampleRegion").execute());
        } catch (FunctionDomainException e) {
            e.printStackTrace();
        } catch (TypeMismatchException e) {
            e.printStackTrace();
        } catch (NameResolutionException e) {
            e.printStackTrace();
        } catch (QueryInvocationTargetException e) {
            e.printStackTrace();
        }

        long end = System.nanoTime();
        long elapsedTime = end - start;

        System.out.println("Done. " + TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS) + "ms");
    }


    public static ClientCacheFactory connectStandalone(String name) {
        return new ClientCacheFactory()
//              .set("log-file", name + ".log")
                .set("archive-file-size-limit", "100")
                .set("statistic-sampling-enabled", "true")
                .set("cache-xml-file", "cache-client.xml");

    }
}
