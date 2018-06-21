package treepye;

import java.util.Properties;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Common {

	public static Properties properies;
	public static String credentials_path;
	
	public static void LoadConfigurations()
	{
		credentials_path = Common.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		
		InputStream input = null;
		
		try{
			input = new FileInputStream("config.properties");
			Common.properies = new Properties();
			Common.properies.load(input);
		}
		catch(FileNotFoundException e){
			System.out.println("Error : Unable to load the configurations."+e.getMessage());
			System.exit(0);
		} catch (IOException e) {
			System.out.println("Error : Unable to load the configurations. "+e.getMessage());
			System.exit(0);
		}
	}
	
	public static String getProperty(String keyname)
	{
		String value = "";
		if(Common.properies != null && Common.properies.getProperty(keyname) != null)
			value = Common.properies.getProperty(keyname);
		
		return value;
	}
	
	public static String GetURLResponse(String targetURL, String urlParameters) {
		  HttpURLConnection connection = null;
		  System.out.println(targetURL);
		  System.out.println(urlParameters);
		  try {
		    //Create connection
		    URL url = new URL(targetURL);
		    connection = (HttpURLConnection) url.openConnection();
		    connection.setRequestMethod("POST");
		    connection.setRequestProperty("Content-Type", 
		        "application/x-www-form-urlencoded");

		    connection.setRequestProperty("Content-Length", 
		        Integer.toString(urlParameters.getBytes().length));
		    connection.setRequestProperty("Content-Language", "en-US");  

		    connection.setUseCaches(false);
		    connection.setDoOutput(true);

		    //Send request
		    DataOutputStream wr = new DataOutputStream (
		        connection.getOutputStream());
		    wr.writeBytes(urlParameters);
		    wr.close();

		    //Get Response  
		    InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
		    String line;
		    while ((line = rd.readLine()) != null) {
		      response.append(line);
		      response.append('\r');
		    }
		    rd.close();
		    return response.toString();
		  } catch (Exception e) {
		    e.printStackTrace();
		    return null;
		  } finally {
		    if (connection != null) {
		      connection.disconnect();
		    }
		  }
		}
}
