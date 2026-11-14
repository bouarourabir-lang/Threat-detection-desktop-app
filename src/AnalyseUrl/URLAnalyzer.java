package AnalyseUrl;
import java.net.URI ;
import java.net.URISyntaxException;
public class URLAnalyzer
{
    private URLValidation urlvalidate =new URLValidation();
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
            if(num < 0 || num > 255)
            {
                return false;
            }
        }
        return true;
    }


    public boolean analyzeDomain(String host,String url)
    {
        String []parts=host.split("\\.");
        if(parts.length>3)
        {
            return true;
        }
        if(url.contains("@"))
        {
            return true;
        }
        return false;
    }
}