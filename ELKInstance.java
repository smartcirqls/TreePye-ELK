package treepye;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

public class ELKInstance {
	@JsonIgnore
	public String _id;
	public String user_id;
	public String instance_role;
	public String ip_address;
	public String port;

	
	public ELKInstance()
	{
		this._id = "";
		this.user_id = "";
		this.instance_role = "";
		this.ip_address = "";
		this.port = "";
	}
	
	/*public ELKInstance(Instance instance)
	{
		this._id = "";
		this.elk_account_id = "";
		this.instance_role = instance.getInstanceRole();
		this.ip_address = instance.getIpAddress();
		this.port = instance.getPort();
	}*/
	
	public ELKInstance(JsonNode node)
	{
		this._id = node.path("_id").asText();
		this.user_id = node.path("_source").path("user_id").asText();
		this.instance_role = node.path("_source").path("instance_role").asText();
		this.ip_address = node.path("_source").path("ip_address").asText();
		this.port = node.path("_source").path("port").asText();
	}
}
