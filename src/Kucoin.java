import java.net.http.*;
import java.net.URI;
import org.json.*;
import java.util.ArrayList;
import java.util.logging.*;
import java.time.LocalTime;
import java.util.Base64;

public class Kucoin
{
ArrayList<String> sym;
ArrayList<Double> fp;
ArrayList<Integer> can;
String lastsym = "";
boolean match = false;
double percent = 0.30;
Object[] obj;
ArrayList<String> list = new ArrayList<String>();
ArrayList<Object> array;
JSONObject jo;
JSONArray ticker;
JSONObject ArrayObj;
double pluspercent;
double first;
HttpClient client;
HttpRequest request;
HttpResponse<String> response;
JSONObject root;
JSONObject candles;
Process process;
	String lastbody;

	int symmatch;

	int startTimeinsec;

	public Kucoin()
	{
		symmatch=0;
		lastbody = "";
		array = new ArrayList<Object>();
		can = new ArrayList<Integer>();
		obj = new Object[7];
		startTimeinsec = getTimeinsec();
		sym = new ArrayList<String>();
		fp = new ArrayList<Double>();
		can = new ArrayList<Integer>();
	}

	public JSONObject geturl(String uri)
	{
		try {
			client = HttpClient.newHttpClient();
			request = HttpRequest.newBuilder().uri(URI.create(uri)).header("Accept", "application/json").build();
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
			root = null;
			if (response.body().contains("{")) {
				root = new JSONObject(response.body());
			}
			else
			{
				System.out.println("error {");
			}
			//System.out.println(response.body().length());
		}
		catch(Exception e)
		{
			System.out.println(LocalTime.now());
			System.out.println(e);
		}

			//print to printer lists of arrays to see error instead of 1260 so can do all
//aaaaaaaaaaa aaaaaaaaa aaaaaaaaaaaaa aaaaaaaaaaa aaaaaaaaaaa aaaaaaaaaa
//check which column has a difference
//printnumber in array at the top then data
//even save to txt files and use diff
//even print diff between 2 strings

			//number of chars dont match check tickers incase they all have price symbol at bottom

			//sym[] should be arraylist if symbol isnt in there add it
	return root;
	}
	
	public JSONObject login(String uri)
	{
		try
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
		}
		catch(Exception e)
		{
			System.out.println(LocalTime.now());
			System.out.println(e);
		}
   		return root;
	}

	public ArrayList<Object> run(JSONObject root)
	{
		try
		{
		jo = root.getJSONObject("data");
		ticker = jo.getJSONArray("ticker");
		array.clear(); //clear which array
		//System.out.println(ticker.length());

		for(int i=0;i<ticker.length();i++)//change this to arrayobj and go through sym[] if its not in there then adds it and to fp[]
		{
			//need another for loop to go throught ticker or ^
			ArrayObj = ticker.getJSONObject(i);

			for(int j=0;j<sym.size();j++)
			{
				if(ArrayObj.getString("symbol").toString().equals(sym.get(j)))
				{
					symmatch++;
				}
				else
				{
					if(symmatch==0)
					{
						//add it to array and fp[] make arraylist
						sym.add(ArrayObj.getString("symbol").toString());
						fp.add(Double.valueOf(ArrayObj.getString("buy").toString()));
						can.add(0);

						//System.out.println(ArrayObj.getString("symbol").toString());
						//System.out.println(ArrayObj.getString("buy").toString());

			//massive error prints the same symbol

						if(!(sym.size()==fp.size()))
						{
							System.out.println(sym.size() + "  " + fp.size());
						}
					}
				}
			}
			symmatch=0;

			//if(ArrayObj.getString("symbol").toString().equals(sym.get(i))&&sym.get(i).contains("USDT")&&root!=null)
			if(ArrayObj.getString("symbol").toString().equals(sym.get(i))&&root!=null)
			{
				//if = get it because it exists therefore it might be >
				//if doesnt equal add it at bottom
				first = fp.get(i);
				pluspercent = first+(percent*first);

				if(Double.valueOf(ArrayObj.getString("buy")) > pluspercent)
				{
					//if above percent printout
					//if last sym != what if = print if above 30percent
					if(!lastsym.equals(sym.get(i))) {
						for (int j = 0; j < list.size(); j++) {
							//if = in wrong pllace first if !=
							if (list.get(j).equals(sym.get(i))) {
								match = true;
								//process = Runtime.getRuntime().exec("espeak \"Rising\"");
							} else {
								//System.out.println("not equals");
							}
						}
						list.add(sym.get(i));

						if (match == false) {
							process = Runtime.getRuntime().exec("espeak " + sym.get(i) + "");
						}
						match = false;

						obj[0] = LocalTime.now();
						obj[1] = ArrayObj.getString("symbol").toString();
						obj[2] = fp.get(i);
						obj[3] = pluspercent;
						obj[4] = ArrayObj.getString("buy").toString();
						obj[5] = Double.valueOf(ArrayObj.getString("buy")) - fp.get(i);
						obj[6] = (Double) ((((Double.valueOf(ArrayObj.getString("buy"))) - fp.get(i)) / fp.get(i)) * 100);

						candles = geturl("https://api.kucoin.com/api/v1/market/candles?type=1min&symbol=" + obj[1] + "&startAt=0&endAt="+startTimeinsec+"");
						JSONArray jo2 = candles.getJSONArray("data");
						for(int e=0;e<jo2.length();e++) {
							JSONArray ticker2 = jo2.getJSONArray(e);
							can.set(i,0);
							for (int d = 0; d < ticker2.length(); d++) {
								System.out.println("kl " + ticker2.getString(d));
							}
							if ((ticker2.getDouble(2) - ticker2.optDouble(1) > 0)) {
								can.set(i, can.get(i) + 1);
							}

							if ((ticker2.getDouble(2) - ticker2.optDouble(1) < 0)) {
								can.set(i, can.get(i) - 1);
							}

							System.out.println("GREEN " + can.get(i));


							for (int y = 0; y < obj.length; y++) {
								System.out.println(obj[y]);
							}
							System.out.println("*****************************************");
						}
					}
					else {
						System.out.println("equals");
					}
						lastsym = sym.get(i);
				}
				else if(Double.valueOf(ArrayObj.getString("buy")) < fp.get(i))
				{
					fp.set(i, Double.valueOf(ArrayObj.getString("buy")));
				}
			}
		}
		}
		catch(Exception e)
		{
			System.out.println(LocalTime.now());
			System.out.println(e);
		}
		return array;
	}

	public int getTimeinsec()
	{
		candles = geturl("https://api.kucoin.com/api/v1/market/candles?type=1min&symbol=BTC-USDT&startAt=0&endAt=0");
		JSONArray jo2 = candles.getJSONArray("data");
		JSONArray ticker2 = jo2.getJSONArray(0);

		return ticker2.getInt(0);
	}
	
	public void runonce(JSONObject root)
	{
		try
		{
		jo = root.getJSONObject("data");
		ticker = jo.getJSONArray("ticker");

		for(int i=0;i<ticker.length();i++)
		{
			ArrayObj = ticker.getJSONObject(i);
			sym.add(ArrayObj.getString("symbol").toString());
			can.add(0);
			if(ArrayObj.getString("buy")!=null) {
				fp.add(Double.valueOf(ArrayObj.getString("buy")));
			}
		}
		}
		catch(Exception e)
		{
			System.out.println(LocalTime.now());
			System.out.println(e);
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



//multithreaded server concurrent threads loop through give a small amount of time to each then call stop then start

