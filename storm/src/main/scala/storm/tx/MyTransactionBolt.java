package storm.tx;

import org.apache.storm.coordination.BatchOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseTransactionalBolt;
import org.apache.storm.transactional.TransactionAttempt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;



public class MyTransactionBolt extends BaseTransactionalBolt {


	private static final long serialVersionUID = 5212522882047650151L;

	Integer count = 0;
	BatchOutputCollector collector;
	TransactionAttempt tx ;
	@Override
	public void execute(Tuple tuple) {


		tx = (TransactionAttempt)tuple.getValue(0);
		System.out.println("MyTransactionBolt TransactionAttempt "+tx.getTransactionId() +"  attemptid "+tx.getAttemptId());
		String log = tuple.getString(1);
		if (log != null && log.length()>0) {
			count ++ ;
		}
		
	}

	@Override
	public void finishBatch() {

		collector.emit(new Values(tx,count));
	}

	@Override
	public void prepare(Map conf, TopologyContext context,
			BatchOutputCollector collector, TransactionAttempt id) {

		this.collector = collector;
		System.err.println("MyTransactionBolt prepare "+id.getTransactionId() +"  attemptid "+id.getAttemptId());
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {


		declarer.declare(new Fields("tx","count"));
	}

}
