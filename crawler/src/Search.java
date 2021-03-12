import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Search {
    public static final String URL = "http://localhost:8983/solr/core2";
    public static final HttpSolrClient client = new HttpSolrClient.Builder(URL).build();

    public static void searchAll() {
        SolrQuery q = new SolrQuery(String.format("(name:%s)^5 (categories:%s)^4 (author:*)^3", "1915", "1915"));
        q.set("fl", "score, name, categories");
        try {
            QueryResponse res = client.query(q);
            for (SolrDocument doc : res.getResults()) {
                System.out.println(String.format("%s : %s", doc.getFieldValue("name"), doc.getFieldValue("score")));
                System.out.println("Categories: " + doc.getFieldValue("categories") + "\n");
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }
}
