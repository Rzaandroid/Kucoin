import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

//import org.json.JSONException;
//import org.json.JSONObject;

public class Bitcoins {

static boolean firstime=true;

  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  public static String readJsonFromUrl(String url) throws Exception {
    InputStream is = new URL(url).openStream();
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      //System.out.println(jsonText);
      StringTokenizer tokenizer = new StringTokenizer(jsonText, "{\"symbol\":\"");
      int x = 0;
      int y = 1;
      int z = 4;
      Float a = 0.0f;
      Float b = 0.0f;
      Float c = 0.0f;
      ArrayList price = new ArrayList();
      ArrayList prevprice = new ArrayList();
      ArrayList Initialprice = new ArrayList();
      ArrayList sym = new ArrayList();
      
      String token = "";
    while (tokenizer.hasMoreElements())
    {
    	token = tokenizer.nextToken();
    	if(x>0&&x==y)
    	{
    		//symbol
        	System.out.println(token);
        	sym.add(token);
        	y=y+5;
        }
        else if(x>0&&x==z)
        {
        	//price
        	System.out.print(token+"  :  ");
        	//a=(Float)price.get(x);
        	price.add(Float.valueOf(token));
        	//b=(Float)price.get(x);
        	//System.out.println((a)-(Float.valueOf(token)));
        	z=z+5;
        }
        
        //if(firstime==true)
        //{
        //	Initialprice=price;
        //}
        
        //if(Initialprice.size()>1)
    	//{
    			//change
    			//a=(Float)price.get(x);
    			//b=(Float)Initialprice.get(x);
    			//c=(Float)prevprice.get(x);
    			//System.out.println(a+" "+(a-b)+" "+(a-c));
     	//}
     	//prevprice=price;
        x++;
    }
    x=0;
    y=0;
    firstime=false;
        
      //System.out.println(jsonText.substring(jsonText.indexOf("{\"symbol\":\""),jsonText.lastIndexOf(",")));
      return jsonText;
  }

  public static void main(String[] args) throws Exception {
//    Object json = readJsonFromUrl("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=ABC&apikey=260LDAYFZDF9YHUC");
//      Object json = readJsonFromUrl("https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=A&apikey=260LDAYFZDF9YHUC");
while(true)
{
String json = readJsonFromUrl("https://api.binance.com/api/v3/ticker/price");
}
  }
}
