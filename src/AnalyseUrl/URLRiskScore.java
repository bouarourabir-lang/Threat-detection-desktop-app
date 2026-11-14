package AnalyseUrl;
public class URLRiskScore
{
    private URLAnalyzer analyzer=new URLAnalyzer();
    StringBuilder description=new StringBuilder();
    public int calculateScore(String scheme,String host,String url)
    {
        int score=0;
        boolean ishttp=analyzer.analyseProtocol(scheme);
        boolean isip=analyzer.analyseIp(host);
        boolean domain=analyzer.analyzeDomain(host,url);

        if(ishttp)
        {
            score+=3;
            description.append("the url uses http protocol");
        }
        if(isip)
        {
            score+=2;
            description.append("the url uses a direct ip address");
        }
        if(domain)
        {
            score+=2;
            description.append("the url contains a deceptive subdomain");
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