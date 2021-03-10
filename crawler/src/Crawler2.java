import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Crawler2 extends WebCrawler {
    public static final String URL = "http://localhost:8983/solr/core2";
    public static final SolrClient client = new ConcurrentUpdateSolrClient.Builder(URL).build();

    public static final String[] NOT_IN_PAGE_EXTENSIONS = {"css", "js", "jpeg", "png", "jpg", "gif", "bmp", "pdf",
            "mp4", "mp3"};

    private boolean cont (WebURL url, String[] arr ) {
        for (String s : arr) {
            if (url.getURL().contains(s)) return true;
        }
        return false;
    }
    public boolean shouldVisit(Page referringPage, WebURL url) {
        return url.getURL().contains("wikipedia.org") && !cont(url, NOT_IN_PAGE_EXTENSIONS);
    }
    public void visit(Page page) {
        String url = page.getWebURL().getURL();

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String title = htmlParseData.getTitle();
            String html = htmlParseData.getHtml();

            SolrInputDocument doc = new SolrInputDocument();

            Elements scriptTags = Jsoup.parse(html).body().getElementsByAttribute("type");
            for (Element el : scriptTags) {
                if (el.attr("type").equals("application/ld+json")) {
                    JSONObject obj = new JSONObject(el.html());
                    doc.setField("name", obj.getString("name"));
                    doc.setField("image", obj.getString("image"));
                    doc.setField("datePublished", obj.getString("datePublished"));
                    doc.setField("author", obj.getJSONObject("author").getString("name"));
                }
            }
            Elements categories = Jsoup.parse(html).body().getElementById("mw-normal-catlinks").getElementsByTag("li");
            List<String> catStr = new ArrayList<>();
            for (Element el :
                    categories) {
                catStr.add(el.getElementsByTag("a").text());
            }
            doc.setField("categories", catStr);

            doc.setField("title", title);


            try {
                client.add(doc);
                client.commit();
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
