package com.alpermelkeli.laundrycenter.frequentfunctions;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;

public class FrequentFunctions {
    public FrequentFunctions(){

    }
    public long getTime() throws Exception {
        String url = "https://time.is/Unix_time_now";
        Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
        String[] tags = new String[] {
                "div[id=time_section]",
                "div[id=clock0_bg]"
        };
        Elements elements= doc.select(tags[0]);
        for (int i = 0; i <tags.length; i++) {
            elements = elements.select(tags[i]);
        }
        return Long.parseLong(elements.text());
    }

}
