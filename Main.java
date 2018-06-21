package treepye;

import java.util.List;

public class Main {

	public static void main(String[] args) {
			Common.LoadConfigurations();
			ElasticController ec = new ElasticController();
			ec.GetAllELKInstances();
			List<ELKInstance> instances = ec.GetAllELKInstances();
			for(ELKInstance i : instances)
			{
				if(i.user_id.isEmpty() == false)
				{
					System.out.println("["+i.user_id+"]["+i.instance_role+"]["+i.ip_address+"]["+i.port+"]");
				}
			}
			
			
			ec.SaveELKInstancesToElastic(instances);
	}
		// TODO Auto-generated method stub
	
}
