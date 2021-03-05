import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.Set;

public class Crawler extends WebCrawler {
    public static final String[] NOT_IN_PAGE_EXTENSIONS = {"css", "js", "jpeg", "png", "jpg", "gif", "bmp", "pdf",
            "mp4", "mp3"};

    private boolean cont (WebURL url, String[] arr ) {
        for (String s : arr) {
            if (url.getURL().contains(s)) return true;
        }
        return false;
    }
    public boolean shouldVisit(Page referringPage, WebURL url) {
        return url.getURL().contains("qoqa.ch") && !cont(url, NOT_IN_PAGE_EXTENSIONS);
    }
    public void visit(Page page) {
        String url = page.getWebURL().getURL();

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String title = htmlParseData.getTitle();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            // do something with the collected data
        }
    }
}
