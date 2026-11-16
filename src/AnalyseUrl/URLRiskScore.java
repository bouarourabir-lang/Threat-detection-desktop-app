package AnalyseUrl;
public class URLRiskScore
{
    private URLAnalyzer analyzer=new URLAnalyzer();
    StringBuilder description=new StringBuilder();
    public int calculateScore(String scheme,String host,String url)
    {   description.setLength(0);  //vider la description
        int score=0;
        boolean ishttp=analyzer.analyseProtocol(scheme);
        boolean isip=analyzer.analyseIp(host);
        boolean domain=analyzer.analyzeDomain(host);
        boolean symbol=analyzer.analyzeSymbol(url);
        boolean punycode=analyzer.punyCode(host);
        boolean shortener=analyzer.urlShortener(host);
        boolean keyword=analyzer.suspiciousKeywords(url);

        if(ishttp)
        {
            score+=3;
            description.append("the url uses http protocol."+"\n");
        }
        if(isip)
        {
            score+=2;
            description.append("the url uses a direct ip address."+"\n");
        }
        else if(domain)
        {
            score+=2;
            description.append("the url contains a deceptive subdomain."+"\n");
        }
        if(symbol)
        {
            score+=2;
            description.append("the url contains @ symbol."+"\n");
        }
        if(punycode)
        {
            score+=2;
            description.append("the url contains a punycode."+"\n");
        }
        if(shortener)
        {
            score+=2;
            description.append("the url uses a url shortening service."+"\n");
        }
        if(keyword)
        {
            score+=2;
            description.append("the url contains suspicious keyword."+"\n");
        }
        return score;
    }

    public String getDescription()
    {
        return description.toString();
    }


    public String Risklevel(int score)
    {
        if(score==0)
        {
            return"low";
        }
        else if(score<=3)
        {
            return "medium";
        }
        else
        {
            return "high";
        }
    }
}