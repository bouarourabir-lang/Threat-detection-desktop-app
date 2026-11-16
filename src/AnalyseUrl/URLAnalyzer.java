package AnalyseUrl;
import java.net.URI ;
import java.net.URISyntaxException;
public class URLAnalyzer
{
    private URLValidation urlvalidate=new URLValidation();
    public void splittingurl (String url)throws URISyntaxException
    {
        URI uri=new URI(url);
        String scheme=uri.getScheme();
        String host=uri.getHost();
        String path=uri.getPath();
    }


    public boolean analyseProtocol( String scheme)
    {
        if(scheme.equalsIgnoreCase("http"))
        {
            return true;
        }
        return false;
    }


    public boolean analyseIp(String host)
    {
        String []parts=host.split("\\.");
        if(parts.length!=4)
        {
            return false;
        }
        for(String part:parts)
        {
            for(int i=0;i<part.length();i++)
            {
                if(!Character.isDigit(part.charAt(i)))
                {
                    return false;
                }
            }
            int num=Integer.parseInt(part);
            if(num<0||num>255)
            {
                return false;
            }
        }
        return true;
    }


    public boolean analyzeDomain(String host)
    {
        String []parts=host.split("\\.");
        if(parts.length>3)
        {
            return true;
        }

        return false;
    }

    public boolean analyzeSymbol(String url)
    {
        if(url.contains("@"))
        {
            return true;
        }
        return false;
    }
    public boolean punyCode(String host)   // ieeee 2022 phishing url detection
    {
        String []parts=host.split("\\.");
        for(String part:parts)
        {
            if(part.toLowerCase().startsWith("xn--"))
            {
                return true;
            }
        }
        return false;
    }
    public boolean urlShortener(String host)//recherche in lnu-pish
    {
        String[]Shorteners= { "bit.Ly","tinyurl.com","t.co","is.gd","ow.ly"};
        for(String Shortener:Shorteners )
        {
            if(host.equalsIgnoreCase(Shortener))
            {
                return true;
            }
        }
        return false;
    }
    public boolean suspiciousKeywords(String url)//ieeee 2022
    {
        String[] keywords= {"Login","verify","password","account","security","update"};
        for(String keyword:keywords)
        {
            if(url.contains(keyword))
            {
                return true;
            }
        }
        return false;
    }
}