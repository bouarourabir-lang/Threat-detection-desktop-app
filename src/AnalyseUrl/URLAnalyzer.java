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
        if(scheme.equals("http")||scheme.equals("fttp"))
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


    public boolean punyCode(String host)
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


    public boolean urlShortener(String host)
    {
        String[]Shorteners= { "bit.ly","tinyurl.com","t.co","is.gd","ow.ly"};
        for(String Shortener:Shorteners )
        {
            if(host.equalsIgnoreCase(Shortener))
            {
                return true;
            }
        }
        return false;
    }


    public boolean suspiciousKeywords(String url)
    {
        String[] keywords= {"login","verify","password","account","security","update"};
        String lowerurl=url.toLowerCase();
        for(String keyword:keywords)
        {
            if(lowerurl.contains(keyword))
            {
                return true;
            }
        }
        return false;
    }


    public boolean longPath(String path)
    {
        if(path.length()>100)
        {
            return true;
        }
        return false;
    }


    public boolean deepPath(String path)
    {
        String []parts=path.split("/");
        if(parts.length>5)
        {
            return true;
        }
        return false;
    }


    public boolean multipledot(String path)
    {
        int count=0;
        for(int i=0;i<path.length();i++)
        {
            if(path.charAt(i)=='.')
            {
                count++;
            }
        }
        if(count>4)
        {
            return true;
        }
        return false;
    }
}