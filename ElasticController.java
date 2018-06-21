package treepye;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ElasticController {
	
	private String SaveJSONToElastic(String elasticPath, String jsonData)
	{
		String elasticResponse = "";
		
		String treepyeHost = Common.getProperty("TREEPYE_HOST");
		String treepyePort = Common.getProperty("TREEPYE_PORT");
		
		if(treepyeHost != "" && treepyePort != "")
		{
			String command[]= {"curl","-XPOST", treepyeHost+":"+treepyePort+"/"+elasticPath,"-H","Content-Type:application/json","-d", jsonData};
			
			try {
				ProcessBuilder builder = new ProcessBuilder(command);
				Process process = builder.start();
				InputStream is = process.getInputStream();
			    InputStreamReader isr = new InputStreamReader(is);
			    BufferedReader br = new BufferedReader(isr);
			    String line;
			    StringBuffer response = new StringBuffer();
			    while ((line = br.readLine()) != null) {
			    	response.append(line);
			    }
			    br.close();
			    elasticResponse = response.toString()+"\n";			    
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				elasticResponse = "";
				System.out.println("Error ELK Save Response : "+e.getMessage());
			}
		}
		return elasticResponse;
	}
	
	private String GetElasticDataToJson(String elasticPath, String filterQuery)
	{
		String jsonResponse = "";
		
		String treepyeHost = Common.getProperty("TREEPYE_HOST");
		String treepyePort = Common.getProperty("TREEPYE_PORT");
		
		if(treepyeHost != "" && treepyePort != "")
		{
			String command[]= {"curl","-XPOST", treepyeHost+":"+treepyePort+"/"+elasticPath+"/_search","-H","Content-Type:application/json","-d", filterQuery};
			//System.out.println(command[0]+" "+command[1]+" "+command[2]+" "+command[3]+" "+command[4]+" "+command[5]+" "+command[6]);
			//System.exit(0);
			try {
				ProcessBuilder builder = new ProcessBuilder(command);
				Process process;
				
				process = builder.start();				 
			    
			    InputStream is = process.getInputStream();
			    InputStreamReader isr = new InputStreamReader(is);
			    BufferedReader br = new BufferedReader(isr);
			    String line;
			    StringBuffer response = new StringBuffer();
			    while ((line = br.readLine()) != null) {
			    	response.append(line);
			    }
			    br.close();
			    jsonResponse = response.toString();	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				jsonResponse = "";
				System.out.println("Error ELK Get Response : "+e.getMessage());
			}
		}
		
		return jsonResponse;
	}
	
	private String GetElasticDataCountToJson(String elasticPath, String filterQuery)
	{
		String jsonResponse = "";
		
		String treepyeHost = Common.getProperty("TREEPYE_HOST");
		String treepyePort = Common.getProperty("TREEPYE_PORT");
		//System.out.println("["+treepyeHost+"]["+treepyePort+"]");
		//System.exit(0);
		if(treepyeHost != "" && treepyePort != "")
		{
			String command[]= {"curl","-XPOST", treepyeHost+":"+treepyePort+"/"+elasticPath+"/_count","-H","Content-Type:application/json","-d", filterQuery};
			//System.out.println(command[0]+" "+command[1]+" "+command[2]+" "+command[3]+" "+command[4]+" "+command[5]+" "+command[6]);
			//System.exit(0);
			try {
				ProcessBuilder builder = new ProcessBuilder(command);
				Process process;
				
				process = builder.start();				 
			    
			    InputStream is = process.getInputStream();
			    InputStreamReader isr = new InputStreamReader(is);
			    BufferedReader br = new BufferedReader(isr);
			    String line;
			    StringBuffer response = new StringBuffer();
			    while ((line = br.readLine()) != null) {
			    	response.append(line);
			    
			    }
			    br.close();
			    jsonResponse = response.toString();	
			    //System.out.println("/" + jsonResponse + "/");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				jsonResponse = "";
				System.out.println("Error ELK Get Response : "+e.getMessage());
			}
		}
		
		//System.out.println(jsonResponse);
		return jsonResponse;
	
	}
	
	public ELKInstance GetELKInstance(String user_id, String ip_address)
	{
		ELKInstance elkinstance = null;
		
		if(user_id != "" && ip_address != "")
		{
			String filterQuery = "{ "+
			 "\"query\": {"+
			   "\"bool\" : {"+
			     "\"must\" : [ {"+
			       "\"match_phrase\" : { \"user_id\" : \""+user_id+"\" }"+
			     "},"+
			     "{ "+
			     "\"match_phrase\" : { \"instance_id\" : \""+ip_address+"\"}"+
			     "}]"+
			   "}"+
			 "}"+
			"}";
						
			String elasticPath = Common.getProperty("TREEPYE_ELK_INSTANCES");
			
			String jsonResponse = GetElasticDataToJson(elasticPath, filterQuery);
			//hii
			System.out.println(jsonResponse);
			
			ObjectMapper mapper = new ObjectMapper();
			try {
				JsonNode rootNode = mapper.readTree(jsonResponse);								
				JsonNode instanceNode = rootNode.path("hits").path("hits");

				if(instanceNode.isArray())
				{
					for(JsonNode node : instanceNode)
					{
						elkinstance = new ELKInstance(node);						
						
						break;
					}
				}				
								
			} catch (JsonProcessingException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return elkinstance;		
	}
	
	public List<ELKInstance> GetAllELKInstances()
	{
		List<ELKInstance> instances = null;
		
		String filterQuery = "{ }";
		int documentCount = 0;			
		String elasticPath = Common.getProperty("TREEPYE_ELK_INSTANCES");
		
		ObjectMapper mapper = new ObjectMapper();
		
		String jsonCountResponse = GetElasticDataCountToJson(elasticPath, filterQuery);
		
		//System.out.println("(" + jsonCountResponse + ")");
		//System.exit(0);
		
		try {
			JsonNode rootCountNode = mapper.readTree(jsonCountResponse);
			documentCount = rootCountNode.path("count").intValue();			
		}
		catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(documentCount > 0){
			filterQuery = "{ \"size\" : \""+documentCount+"\" }";
			
			String jsonResponse = GetElasticDataToJson(elasticPath, filterQuery);
									
			try {
				JsonNode rootNode = mapper.readTree(jsonResponse);								
				JsonNode instanceNode = rootNode.path("hits").path("hits");
	
				if(instanceNode.isArray())
				{
					instances = new ArrayList<ELKInstance>();
					
					for(JsonNode node : instanceNode)
					{
						
						ELKInstance instance = new ELKInstance(node);
						
					    instances.add(instance);
						
					}
				}				
								
			} catch (JsonProcessingException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return instances;
	}
	
	public String SaveELKInstancesToElastic(List<ELKInstance> instances)
	{
		String elasticResponse = "";
		if(instances != null)
		{
			ObjectMapper mapper = new ObjectMapper();
			String elasticPath = Common.getProperty("TREEPYE_ELK_INSTANCES");
			if(elasticPath != ""){
				for(ELKInstance instance : instances)
				{					
					ELKInstance ai = GetELKInstance(instance.user_id, instance.ip_address);
					if(ai == null)
					{
						try {							
							String jsonData = mapper.writeValueAsString(instance);						
							elasticResponse += SaveJSONToElastic(elasticPath, jsonData);				
						} catch (JsonProcessingException e) {
							// TODO Auto-generated catch block
							elasticResponse+=e.getMessage();
							e.printStackTrace();
						}
					}
					else
						elasticResponse += "Instance "+instance.ip_address+" already exists."+"\n";
				}
				elasticResponse += "Instances have been saved to elastic";
			}
		}
		
		return elasticResponse;
	}
	
	
}
	
	
