import java.net.http.*;
import java.net.URI;

import org.json.*;
import org.json.JSONTokener;

import java.awt.Desktop;

import java.util.ArrayList;
import java.util.logging.*;

import java.time.LocalTime;

import java.util.Base64;


public class Kucoin
{
String [] sym;
double [] fp;
String[] split;
Float[] r;
Float price = 0.0f;
Float rise = 0.0f;
Float low = 0.0f;
Float firstprice = 0.0f;
String symbol = "";
String lastsym = "";
boolean match = false;

Logger logger = Logger.getLogger("Kucoin");  
FileHandler fh;  

double percent = 0.10;

Object[] obj;

ArrayList<String> list = new ArrayList<String>();

ArrayList<Object> array;

	JSONObject jo;
	JSONArray ticker;
	JSONObject ArrayObj;
	double pluspercent;
	double first;

public Kucoin()
{
	array = new ArrayList<Object>();
	obj = new Object[7];
}

	public JSONObject geturl(String uri) throws Exception
	{
    		HttpClient client = HttpClient.newHttpClient();
    		HttpRequest request = HttpRequest.newBuilder()
          	.uri(URI.create(uri))
		.header("Accept", "application/json")
		.build();

    		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    		JSONObject root = null;
    		if(response.body().contains("{"))
    		{
    			root = new JSONObject(response.body());
    		}
   		return root;
	}
	
	public JSONObject login(String uri) throws Exception
	{
        final String api_key = "617da39ff17a750001694f33";
        final String api_secret = "be2d0acd-a0c3-4c76-b580-4853ea9a3b03";
        final String timestamp = String.valueOf(System.currentTimeMillis());
        final String api_passphrase = "kucoinapi";

        // Concatenate customer key and customer secret and use base64 to encode the concatenated string
        String plainCredentials = api_key + ":" + api_secret + ":" + timestamp + ":" + api_passphrase;
        String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));
        // Create authorization header
        String authorizationHeader = "Basic " + base64Credentials;
		
    		HttpClient client = HttpClient.newHttpClient();
    		HttpRequest request = HttpRequest.newBuilder()
          	.uri(URI.create(uri))
		.GET()
                .header("Authorization", authorizationHeader)
		.build();

    		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    		JSONObject root = new JSONObject(response.body());
    		System.out.println(response.body());
   		return root;
	}

	public ArrayList<Object> run(JSONObject root) throws Exception
	{       
		jo = root.getJSONObject("data");
		ticker = jo.getJSONArray("ticker");

		for(int i=0;i<sym.length;i++)
		{
			ArrayObj = ticker.getJSONObject(i);

			if(ArrayObj.getString("symbol").toString().equals(sym[i])&&sym[i].contains("USDT")&&root!=null)
			{
				first = fp[i];
				pluspercent = first+(percent*first);

				if(Double.valueOf(ArrayObj.getString("buy")) > pluspercent)
				{
					if(!lastsym.equals(sym[i]))
					{
						for(int j=0;j<list.size();j++)
						{
							if(list.get(j).equals(sym[i]))
							{
								match = true;
							}
						}
						list.add(sym[i]);
						
						if(match == false)
						{
							//Kucoin_Thread t = new Kucoin_Thread(sym[i], percent);
							//t.start();
						}
						
						match = false;

						/*
						System.out.println("***************************************************");
						System.out.println(LocalTime.now());
						System.out.println(ArrayObj.getString("symbol").toString());
						System.out.print("first price: ");
						System.out.println(fp[i]);
						System.out.print("fp + percentage: ");
						System.out.println(pluspercent);
						System.out.print("price: ");
						System.out.println(ArrayObj.getString("buy").toString());
						System.out.print("difference :");
						System.out.println(Double.valueOf(ArrayObj.getString("buy")) - fp[i]);
						System.out.print("percent :");
						System.out.println(((((Double.valueOf(ArrayObj.getString("buy")))-fp[i])/fp[i])*100)+"%");
						*/

						obj[0] = LocalTime.now();
						obj[1] = ArrayObj.getString("symbol").toString();
						obj[2] = fp[i];
						obj[3] = pluspercent;
						obj[4] = ArrayObj.getString("buy").toString();
						obj[5] = Double.valueOf(ArrayObj.getString("buy")) - fp[i];
						obj[6] = ((((Double.valueOf(ArrayObj.getString("buy")))-fp[i])/fp[i])*100)+"%";
						array.add(obj);
/*
						logger.info("***************************************************");
						logger.info(ArrayObj.getString("symbol").toString());
						logger.info("first price: ");
						logger.info(fp[i]);
						logger.info("fp + percentage: ");
						logger.info(pluspercent);
						logger.info("price: ");
						logger.info(ArrayObj.getString("buy").toString());
						logger.info("difference :");
						logger.info(Double.valueOf(ArrayObj.getString("buy")) - fp[i]);
						logger.info("percent :");
						logger.info(((((Double.valueOf(ArrayObj.getString("buy")))-fp[i])/fp[i])*100)+"%");
						*/
						//Desktop d = Desktop.getDesktop();
						//d.browse(URI.create("https://www.kucoin.com/trade/"+sym[i]));
					}
					lastsym=sym[i];
				}
				else if(Double.valueOf(ArrayObj.getString("buy")) < fp[i])
				{
					fp[i] = Double.valueOf(ArrayObj.getString("buy"));
				}
			}
		}
		return array;
	}
	
	public void runonce(JSONObject root) throws Exception
	{       
		jo = root.getJSONObject("data");
		ticker = jo.getJSONArray("ticker");
		System.out.println(ticker.length());
		sym = new String[ticker.length()];
                fp = new double[ticker.length()];
		for(int i=0;i<ticker.length();i++)
		{
			ArrayObj = ticker.getJSONObject(i);
			sym[i] = ArrayObj.getString("symbol").toString();
			fp[i] = Double.valueOf(ArrayObj.getString("buy"));
		}
	}
}

/*
daily risers weekly <>24hr

keep log to file

only print once if equals last symbol
store lastsym

***************************************************
***************************************************
RBS-USDT
0.0
0.0
0.3278
0.3278
***************************************************
***************************************************
leave if starts at 0.0

***************************************************
OOKI-BTC
first price: 9.52E-8
fp + percentage: 1.428E-7
price: 0.0000001702
difference :7.5E-8
percent :78.781512605042%
org.json.JSONException: A JSONObject text must begin with '{' at 1 [character 2 line 1]
https://www.kucoin.com/trade/PLD-USDT?spm=kcWeb.B5tradeAccount.Header4.1
*/

