import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class CrawlerMain {
    private static int numCrawlers = 1;

    public static void main(String[] args) {
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
        config.setMaxPagesToFetch(1000);

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer= new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        controller.addSeed("https://www.qoqa.ch/");

        CrawlController.WebCrawlerFactory<Crawler> factory = Crawler::new;

        controller.start(factory, numCrawlers);
    }
}
