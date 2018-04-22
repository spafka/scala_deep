package storm_example;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

public class SampleStormTopology {
    public static void main(String[] args) {
        // create an instance of TopologyBuilder class
        TopologyBuilder builder = new TopologyBuilder();
        // set the spout class
        builder.setSpout("SampleSpout", new SampleSpout(), 2);
        // set the bolt class
        builder.setBolt("SampleBolt", new SampleBolt(), 4).shuffleGrouping(
                "SampleSpout");
        Config conf = new Config();
       // conf.setNumWorkers(2);
        conf.setDebug(true);
        // create an instance of LocalCluster class for
        // executing topology in local mode.
        LocalCluster cluster = new LocalCluster();
        // SampleStormTopology is the name of submitted topology
        cluster.submitTopology("SampleStormTopology", conf,
                builder.createTopology());
        try {
            Thread.sleep(10000);
        } catch (Exception exception) {
            System.out.println("Thread interrupted exception : " + exception);
        }
        // kill the SampleStormTopology
        cluster.killTopology("SampleStormTopology");
        // shutdown the storm test cluster
        cluster.shutdown();
    }
}
