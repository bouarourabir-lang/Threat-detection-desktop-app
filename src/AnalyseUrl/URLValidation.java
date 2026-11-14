package AnalyseUrl;
import java.net.URI ;
import java.net.URISyntaxException;

public class URLValidation
{
    public boolean validateURL(String url)
    {
        if(url==null || url.trim().isEmpty())
        {
            return false;
        }
           url=url.trim();
        try
        {
            URI uri=new URI(url);
            return(uri.getScheme()!=null && uri.getHost()!=null);
        }
        catch(URISyntaxException e)
        {
            return false ;
        }
    }


    public String extractHostname(String url)
    {
        if(url==null||url.trim().isEmpty())
        {
            return null;
        }
        url=url.trim();
        try
        {
            URI uri=new URI(url);
            return uri.getHost();
        }
        catch(URISyntaxException e)
        {
            return null ;
        }
    }
}