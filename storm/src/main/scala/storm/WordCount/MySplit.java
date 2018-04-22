package storm.WordCount;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.FailedException;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;


public class MySplit implements IBasicBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String patton ;
	public MySplit(String patton)
	{
		this.patton = patton;
	}
	
	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		// TODO Auto-generated method stub
		try {
			String sen = input.getString(0);
			if(sen != null)
			{
				for(String word : sen.split(patton))
				{
					collector.emit(new Values(word));
				}
			}
		} catch (Exception e) {
			throw new FailedException("split fail!");
		}
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
		// TODO Auto-generated method stub
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
