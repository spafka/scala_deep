package storm.user_visit;

import storm.base.SourceSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

public class UVTopo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("spout", new SourceSpout(), 1);
		builder.setBolt("FmtLogBolt", new FmtLogBolt(), 4).shuffleGrouping("spout");
		builder.setBolt("sumBolt", new DeepVisitBolt(),4).fieldsGrouping("FmtLogBolt", new Fields("date","session_id"));
		builder.setBolt("UvSum", new UVSumBolt(), 1).shuffleGrouping("sumBolt") ;
		
		Config conf = new Config() ;
		conf.setDebug(true);

		if (args.length > 0) {
			try {
				StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
			} catch (AlreadyAliveException e) {
				e.printStackTrace();
			} catch (InvalidTopologyException e) {
				e.printStackTrace();
			} catch (AuthorizationException e) {
				e.printStackTrace();
			}
		}else {
			LocalCluster localCluster = new LocalCluster();
			localCluster.submitTopology("mytopology", conf, builder.createTopology());
		}
		
		
		
		

	}

}
