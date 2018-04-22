package storm.tx;

import org.apache.storm.coordination.BatchOutputCollector;
import org.apache.storm.transactional.ITransactionalSpout;
import org.apache.storm.transactional.TransactionAttempt;
import org.apache.storm.tuple.Values;
import java.math.BigInteger;
import java.util.Map;



public class MyEmitter implements ITransactionalSpout.Emitter<MyMata>{

	Map<Long, String> dbMap  = null;
	public MyEmitter(Map<Long, String> dbMap) {
		this.dbMap = dbMap;
	}
	@Override
	public void cleanupBefore(BigInteger txid) {
		
	}

	@Override
	public void close() {
		
	}

	@Override
	public void emitBatch(TransactionAttempt tx, MyMata coordinatorMeta,
						  BatchOutputCollector collector) {
		long beginPoint = coordinatorMeta.getBeginPoint() ;
		int num = coordinatorMeta.getNum() ;
		
		for (long i = beginPoint; i < num+beginPoint; i++) {
			if (dbMap.get(i)==null) {
				continue;
			}
			collector.emit(new Values(tx,dbMap.get(i)));
		}
	}
	
	

}
