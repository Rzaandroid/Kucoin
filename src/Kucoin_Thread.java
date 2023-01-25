import java.net.http.*;
import java.net.URI;

import org.json.*;

public class Kucoin_Thread extends Thread
{
	String sym = "";
	double percent = 0.0;
	
	double step_percent = 10.0;
    	boolean bought = false;
    	double buy_amount = 2.5;
    	double sell_amount = 2.5;
    	double profitmargin = 1;
    	double buylimit = 0.00005270;
    	double selllimit = 0.00005250;
    	double buyprice = 0;
    	double sl = 0;
    	double tp = 0;
    	double bid = 0;
    	double ask = 0;
    	double amount = 0;
    	double profit = 0;
    	boolean firstrun = true;
    	double s = 0;
    	double t = 0;
    	double loss = 0;
    	double price = 0.0;
    	
    	double balance;
    	double available;
    	double fee;
    	double orig_balance;

    	//balance = client.get_account('616ebbf9d5a5cf0001c9dfcc')['balance']
    	///api/v1/accounts/616ebbf9d5a5cf0001c9dfcc
    //	 "currency": "KCS",  //Currency
   // "balance": "1000000060.6299",  //Total assets of a currency
    //"available": "1000000060.6299",  //Available assets of a currency
    //"holds": "0" //Hold assets of a currency
    	//available = client.get_account('616ebbf9d5a5cf0001c9dfcc')['available']
    	//fee = float(0.1 / 100) * float(available)
    	//available = float(available) - fee
    	//orig_balance = balance
	
	public Kucoin_Thread(String sym, double percent)
	{
		this.sym = sym;
		this.percent = percent;
	}
    public void run()
    {
        long startTime = System.currentTimeMillis();
        int i = 0;
        while (true)
        {
            System.out.println(this.getName() + ": New Thread is running..." + i++ +" "+sym+" "+percent);
            try
            {
                trailing_trade();
                Thread.sleep(10);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    
    public void trailing_trade() throws Exception
    {
        price = getprice(geturl("https://api.kucoin.com/api/v1/market/orderbook/level1?symbol="+sym));
        System.out.println("PRICE "+price);
        //bid = float(client.get_ticker(self.sym)['bestBid'])
        //ask = float(client.get_ticker(self.sym)['bestAsk'])
        //changerate = float(client.get_24hr_stats(self.sym)['changeRate'])
        //change = float(client.get_24hr_stats(self.sym)['changePrice'])
        //high = float(client.get_24hr_stats(self.sym)['high'])
        //low = float(client.get_24hr_stats(self.sym)['low'])
        //currency = client.get_currency('USDT')

        //histories = client.get_trade_histories(self.sym)
        
            //f = open(self.sym + ".txt", "a")

            //# buyprice=0
            //# print("histories ", histories)

		if(firstrun == true)
            	{
                	//#push = pb.push_note(self.sym, "STARTED")
                	firstrun = false;
                	//# buyprice******************************************************************************************
                	sl = price;
                	tp = price + (percent / 100) * price;
                	//print("sym ", self.sym)
                	//print("percent ", self.percent)
                //balance = getbalance(login("https://api.kucoin.com/api/v1/accounts/616ebbf9d5a5cf0001c9dfcc"));
                	//print("balance ", TrailingTrade.balance)
                	//print("available ", TrailingTrade.available)
                	//print("FEE ", TrailingTrade.fee)
                	//print("PRICE ", TrailingTrade.price)
                	//print("BID ", TrailingTrade.bid)
                	//print("ASK ", TrailingTrade.ask)
                	//#print("SPREAD", ((TrailingTrade.ask - TrailingTrade.bid) / TrailingTrade.ask) * 100)
                	//#       print("SPREAD gbp", ?((TrailingTrade.ask - TrailingTrade.bid)/TrailingTrade.ask)*0.7462)
                	//print("buy-sell% -fee", (
                        //    ((TrailingTrade.selllimit - TrailingTrade.buylimit) / TrailingTrade.selllimit) * (
                        //        1 - (TrailingTrade.fee / 100))) * 100)
                	//print("buy-sell gbp -fee", (
                        //    ((TrailingTrade.selllimit - TrailingTrade.buylimit) / TrailingTrade.selllimit) * (
                        //        1 - (TrailingTrade.fee / 100))) * 0.7462)
                	//print("Change Rate ", TrailingTrade.changerate)
                	//print("Change Price ", TrailingTrade.change)
                	//print("HIGH ", TrailingTrade.high)
                	//print("LOW ", TrailingTrade.low)
                	//print("*********************************************************************")
		}
            	
            	if(price < sl)
            	{
                	System.out.println("RIDING DOWN "+price);
                	//f.write(self.sym + " RIDING DOWN " + str(TrailingTrade.price) + "\n")
                	sl = price;
                	tp = price + (percent / 100) * price;

                	if(bought == true)
                	{
                 		bought = false;
                    		if((price - buyprice) < 0)
                    		{
                        		loss = loss - (price - buyprice);
                    				
                    			if(price >= (buyprice + (percent / 100) * price))
                    			{
                        			//sell_order = client.create_market_order(self.sym, Client.SIDE_SELL, funds=sell_amount)
                        			profit = profit + ((price - buyprice));
                        			//os.system("espeak profit")
                        			//print("1percent rise commented out sell_order still in (sl)")
                        			sl = price;
                        			bought = false;
                    				System.out.println("SOLD");
                    				//os.system("espeak SOLD")
                    				//#push = pb.push_note(self.sym, " SOLD " + str(TrailingTrade.price))
                    				//f.write(self.sym + " SOLD " + str(TrailingTrade.price) + "\n")
                    				System.out.println("loss "+loss);
                    				System.out.println("profit "+profit);
                    				//f.write(self.sym + " LOSS " + str(TrailingTrade.loss) + "\n")
                    				//f.write(self.sym + " PROFIT " + str(TrailingTrade.profit) + "\n")

            					if(price >= tp)
            					{
                					System.out.println("TAKE PROFIT "+price);
                					//f.write(self.sym + " TAKE PROFIT " + str(TrailingTrade.price) + "\n")
                					//# TrailingTrade.profit = TrailingTrade.profit + (TrailingTrade.price - TrailingTrade.buyprice)
                					tp = price;
                					sl = price - ((percent) / 100) * price;
                					
                					if(bought == false)
                					{
                    						//buy_order = client.create_market_order(self.sym, Client.SIDE_BUY, funds=TrailingTrade.buy_amount)
                    						bought = true;
                    						buyprice = price;
                    						System.out.println("BOUGHT");
                    						//os.system("espeak bought")
                    						//#push = pb.push_note(self.sym, "BOUGHT " + str(TrailingTrade.price))
                    						//f.write(self.sym + " BOUGHT " + str(TrailingTrade.price) + "\n")
                    						tp = price;
                
                						if(bought == true)
                						{
                    							tp = price;
                    							sl = price - ((percent) / 100) * price;

                    							if (price >= (
                            buyprice + ((step_percent) / 100) * price))
                           						{
                        							System.out.println("Gained 10%");
                        							//print("balance ", TrailingTrade.balance)
                        							//print("available ", TrailingTrade.available)
                        							//# buy_order = client.create_market_order(self.sym, Client.SIDE_BUY, funds=TrailingTrade.buy_amount)
                        							//# sell_order = client.create_market_order(self.sym, Client.SIDE_SELL, funds=TrailingTrade.sell_amount)
                        							//#TrailingTrade.bought = False
                        							profit = profit + ((price - buyprice));
                        							//print("sold in profit and bought", TrailingTrade.profit)
                        							//os.system("espeak "+self.sym+" Gained 10% profit")
                        							//print("1percent rise still rising")
                        							//##push = pb.push_note(self.sym, " rised 10% " + str(TrailingTrade.price))
                        							//f.write(self.sym + " balance " + str(TrailingTrade.balance) + "\n")
                        							//f.write(self.sym + " available " + str(TrailingTrade.available) + "\n")
                        							//f.write(self.sym + " up 10% " + str(TrailingTrade.price) + "\n")
                        							//f.close()
            									//# print("***************************************************************************")

        									//except requests.exceptions.ReadTimeout as e:  # Error handling for issues with return form site.
            									//print("error")  # Prints Error.
            									//print(e)  # Prints Error.
            									//print(e.status_code)
            									//print(e.message)
            									//#push = pb.push_note(self.sym, e.message)
            									//f.write("error" + "\n")
            									//f.write(e)
            									//f.close()
									}
								}
							}
						}
					}
				}
			}
		}
	}
	public JSONObject geturl(String uri) throws Exception
	{
    		HttpClient client = HttpClient.newHttpClient();
    		HttpRequest request = HttpRequest.newBuilder()
          	.uri(URI.create(uri))
		.header("Accept", "application/json")
		.build();

    		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    		JSONObject root = new JSONObject(response.body());
   		return root;
	}
	
	public JSONObject login(String uri) throws Exception
	{
    		HttpClient client = HttpClient.newHttpClient();
    		HttpRequest request = HttpRequest.newBuilder()
          	.uri(URI.create(uri))
		.header("Accept", "application/json")
		.header("KC-API-KEY", "617da39ff17a750001694f33")
		.header("KC-API-SIGN", "be2d0acd-a0c3-4c76-b580-4853ea9a3b03")
		//.header("KC-API-TIMESTAMP", "application/json")
		.header("KC-API-PASSPHRASE", "kucoinapi")
		.build();

    		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    		JSONObject root = new JSONObject(response.body());
   		return root;
	}
	
	public double getprice(JSONObject r) throws Exception
	{       
		JSONObject jo = r.getJSONObject("data");
		System.out.println(Double.valueOf(jo.getString("price")));
		return Double.valueOf(jo.getString("price"));
	}
	
	public double getbalance(JSONObject root) throws Exception
	{       
		JSONObject jo = root.getJSONObject("data");
		System.out.println(Double.valueOf(jo.getString("balance")));
		return Double.valueOf(jo.getString("balance"));
	}
	
	public double getavailable(JSONObject root) throws Exception
	{       
		JSONObject jo = root.getJSONObject("data");
		System.out.println(Double.valueOf(jo.getString("available")));
		return Double.valueOf(jo.getString("available"));
	}
}
