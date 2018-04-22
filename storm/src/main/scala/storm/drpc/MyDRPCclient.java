package storm.drpc;

import org.apache.storm.Config;
import org.apache.storm.thrift.TException;
import org.apache.storm.thrift.transport.TTransportException;
import org.apache.storm.utils.DRPCClient;
import org.apache.storm.utils.Utils;

import java.util.Map;

public class MyDRPCclient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws TTransportException {


		Map config = Utils.readDefaultConfig();
		//conf.setDebug(false);
		DRPCClient client = new DRPCClient(config,"127.0.0.1", 3772);
		try {
			String result = client.execute("exclamation", "hello ");
			
			System.out.println(result);
		} catch (TException e) {
			e.printStackTrace();
		}

	}

}
