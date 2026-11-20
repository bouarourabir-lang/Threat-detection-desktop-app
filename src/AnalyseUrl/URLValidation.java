package AnalyseUrl;
import java.net.URI ;
import java.net.URISyntaxException;
public class URLValidation
{   StringBuilder description = new StringBuilder();
    public boolean validateURL(String url)
    { description.setLength(0);
        if(url==null)
        { description.append("vide");
            return false;
        }
        if(url.trim().isEmpty())
        {    description.append("url is empty");
            return false;
        }
        url=url.trim();
        try
        {
            URI uri=new URI(url);

            if(uri.getScheme()==null)
            {   description.append("missing url scheme");
                return false;
            }

            String scheme = uri.getScheme();

            if (!scheme.equalsIgnoreCase("http") &&
                    !scheme.equalsIgnoreCase("https"))
            {
                description.append("unsupported url scheme");
                return false;
            }

            if(uri.getHost()==null)
            {   description.append("missing host name");
                return false;
            }
            description.append("url is valid");
            return true;
        }
        catch(URISyntaxException e)
        { description.append("invalid url format");
            return false ;
        }

    }


    public String getDescription()
    {
        return  description.toString();
    }
}