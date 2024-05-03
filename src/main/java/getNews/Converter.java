package getNews;

public class Converter {
    int headlineSize = 14;
    int paragraphSize = 12;
    int dateSize = 8;

    public String headlineWithLink (String headline, String link) {
        return "[b][size="+ headlineSize + "][url=" + link + "]" + headline + "[/url][/size][/b]" + '\n';
    }

    public String paragraph (String paragraph) {
        return "[size=" + paragraphSize + "]" + paragraph + "[/size]" + '\n';
    }

    public String date (String date) {
        return "[size=" + dateSize + "]" + date + "[/size]" + '\n';
    }
}
