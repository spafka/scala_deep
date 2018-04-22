package storm.WordCount;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.FailedException;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

public class SumBolt implements IBasicBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Map<String, Integer> counts = new HashMap<String, Integer>();
	
	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		// TODO Auto-generated method stub

		try {
			long word_sum = 0;// 总数
			long word_count = 0; // 个数，去重后
			
			String word = input.getString(0);
			Integer count = input.getInteger(1);
			counts.put(word, count);
			//获取总数，遍历counts 的values，进行sum
			Iterator<Integer> i = counts.values().iterator() ;
			while(i.hasNext())
			{
				word_sum += i.next();
			}
			
			//获取word去重个数，遍历counts 的keySet，取count
			Iterator<String> i2 = counts.keySet().iterator() ;
			while(i2.hasNext())
			{
				String oneWordString = i2.next();
				if (oneWordString != null) {
					word_count ++ ;
				}
			}
			
			System.err.println("word_sum="+word_sum+";  word_count="+word_count);
			
		} catch (Exception e) {
			throw new FailedException("SumBolt fail!");
		}
		
		
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
