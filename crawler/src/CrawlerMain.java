import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import java.io.IOException;

public class CrawlerMain {
    private static int numCrawlers = 1;
    public static final String URL1 = "http://localhost:8983/solr/core1";
    public static final String URL2 = "http://localhost:8983/solr/core2";
    public static final SolrClient client1 = new HttpSolrClient.Builder(URL1).build();
    public static final HttpSolrClient client2 = new HttpSolrClient.Builder(URL2).build();

    public static void main(String[] args) {
//        deleteAll(client2);
//        crawl();
        Search.searchAll();
    }

    public static void deleteAll(HttpSolrClient client) {
        try {
            client.deleteByQuery("*:*");
            client.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void crawl() {
        // CRAWLER4J CONFIGURATION
        CrawlConfig config = new CrawlConfig();
        config.setMaxConnectionsPerHost(10);
        config.setConnectionTimeout(4000);
        config.setSocketTimeout(5000);
        config.setCrawlStorageFolder("tmp");
        config.setIncludeHttpsPages(true);
        //minimum 250ms for tests
        config.setPolitenessDelay(250);
        config.setUserAgentString("crawler4j/WEM/2021");
        //max 2-3 levels for tests on large website
        config.setMaxDepthOfCrawling(8);
        //-1 for unlimited number of pages
        config.setMaxPagesToFetch(10);
        try {
            PageFetcher pageFetcher = new PageFetcher(config);
            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            RobotstxtServer robotstxtServer= new RobotstxtServer(robotstxtConfig, pageFetcher);
            CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

            controller.addSeed("https://fr.wikipedia.org/wiki");

            CrawlController.WebCrawlerFactory<Crawler2> factory = Crawler2::new;

            controller.start(factory, numCrawlers);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
