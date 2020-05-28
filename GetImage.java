import java.util.*;
//General
import java.io.*;
//Websraping
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
//Opening URL's
import java.net.URL;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

class GetImage
{

    public static void main(String args[])
    {
        //Webpage variables
        Document imagesDoc = null;
        Elements elements = null;
        Element image = null;
        String imageString = null;
        int httpsIdx;
        int quoteIdx;
        String imageUrl = null;
        //URL and search
        String url = null;
        String search = null;
        //For loop, and success check
        int i;
        boolean success = false;

        //Input box for search, then add onto URL
        search = JOptionPane.showInputDialog("Search for shit:");

        try
        {

            //If there is '-' as first character, fetch and save image
            if ( search.indexOf("-") == 0 )     
            {

                //Get rid of '-' and create url
                search = search.substring(1);
                search = toggleUrlSuitable(search);
                url = "https://www.google.com/search?tbm=isch&tbs=isz:lt,islt:vga,iar:s,ift:jpg&q=" + search;

                //Try get image 5 times or until success
                for ( i = 0; i < 3 && !success; i++ )
                {
                    try
                    {
                        //Get image search results document
                        imagesDoc = Jsoup.connect(url).get();
            
                        //Get full image string
                        image = imagesDoc.select("div.rg_meta").get(0);
                        imageString = image.childNode(0).toString();
                        
                        //Get index of firsts https, and first " after httpsIdx
                        httpsIdx = imageString.indexOf("https");
                        quoteIdx = imageString.indexOf("\"", httpsIdx);
                        
                        //Get image url from full string
                        imageUrl = imageString.substring( httpsIdx, quoteIdx );
                        
                        //Dowload image using src
                        downloadImages(imageUrl, search);
                        
                        //If made it to this point, set success
                        success = true;
                    }
                    catch(IndexOutOfBoundsException | IOException e)
                    {
                        System.out.println("Unable to get image!");
                    }
                }
                
                //If no success, show error box
                if ( !success )
                {
                    //Error message
                    JOptionPane.showMessageDialog(null, "Ciccio Pasticcio bit my toe off.");
                }

            }
            //Else just open url
            else
            {
                //Create url
                search = toggleUrlSuitable(search);
                url = "https://www.google.com/search?tbm=isch&tbs=isz:lt,islt:vga,iar:s,ift:jpg&q=" + search;
                //Open url
                Desktop.getDesktop().browse( new URI(url) );    
            }

        }
        catch ( URISyntaxException | IOException e )
        {
            System.out.println("Error!");
        }

    }
    
    private static void downloadImages(String inSrc, String inName) throws IOException
    {
        //Exctract the name of the image from the src attribute
        int nameIdx = inSrc.lastIndexOf("/");
 
        if ( nameIdx == inSrc.length() ) 
        {
            inSrc = inSrc.substring(1, nameIdx);
        }
 
        nameIdx = inSrc.lastIndexOf("/");
        String name = inSrc.substring (nameIdx, inSrc.length() );
 
        //Open a URL Stream
        URL url = new URL(inSrc);
        InputStream in = url.openStream();
 
        OutputStream out = new BufferedOutputStream(new FileOutputStream( "C:\\Users\\James\\Desktop\\" + inName + ".jpg" ));
 
        for ( int b; (b = in.read()) != -1; ) 
        {
            out.write(b);
        }
        
        out.close();
        in.close();
    }
    
// - Toggle URL Suitable - //
    
    //Make given text suitable to add onto URL
    private static String toggleUrlSuitable(String inSearch)
    {
        String outSearch = inSearch;
        
        outSearch = outSearch.replaceAll("&", "%26");
        outSearch = outSearch.replaceAll(",", "%2C");
        outSearch = outSearch.replaceAll("\\+", "%2B");
        outSearch = outSearch.replace(' ', '+');
        
        return outSearch;
    }

}