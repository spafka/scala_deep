package storm.user_visit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import storm.tools.DateFmt;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.FailedException;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

public class UVSumBolt implements IBasicBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Map<String, Integer> counts = new HashMap<String, Integer>();

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}
	long beginTime = System.currentTimeMillis() ;
	long endTime = 0;
	String cur_date = null;

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		// TODO Auto-generated method stub

		try {
			endTime = System.currentTimeMillis() ;
			long PV = 0;// 总数
			long UV = 0; // 个数，去重后

			String dateSession_id = input.getString(0);
			Integer count = input.getInteger(1);

			if (!dateSession_id.startsWith(cur_date)
					&& DateFmt.parseDate(dateSession_id.split("_")[0]).after(
							DateFmt.parseDate(cur_date))) {
				cur_date = dateSession_id.split("_")[0];
				counts.clear();
			}

			counts.put(dateSession_id, count);

			if (endTime - beginTime >= 2000) {
				// 获取word去重个数，遍历counts 的keySet，取count
				Iterator<String> i2 = counts.keySet().iterator();
				while (i2.hasNext()) {
					String key = i2.next();
					if (key != null) {
						if (key.startsWith(cur_date)) {
							UV++;
							PV += counts.get(key);
						}
					}
				}
				System.err.println("PV=" + PV + ";  UV="+ UV);
			}

			

		} catch (Exception e) {
			throw new FailedException("SumBolt fail!");
		}

	}

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		// TODO Auto-generated method stub
		cur_date = DateFmt.getCountDate("2014-01-07", DateFmt.date_short);
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
