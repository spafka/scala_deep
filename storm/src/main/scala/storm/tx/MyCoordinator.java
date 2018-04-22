package storm.tx;

import org.apache.storm.transactional.ITransactionalSpout;
import org.apache.storm.utils.Utils;

import java.math.BigInteger;



public class MyCoordinator implements ITransactionalSpout.Coordinator<MyMata>{

	public static int BATCH_NUM = 10 ;
	@Override
	public void close() {
		
	}


	@Override
	public MyMata initializeTransaction(BigInteger txid, MyMata prevMetadata) {
		long beginPoint = 0;
		if (prevMetadata == null) {
			beginPoint = 0 ;
		}else {
			beginPoint = prevMetadata.getBeginPoint() + prevMetadata.getNum() ;
		}
		
		MyMata mata = new MyMata() ;
		mata.setBeginPoint(beginPoint);
		mata.setNum(BATCH_NUM);
		System.err.println("启动一个事务："+mata.toString());
		return mata;
	}

	@Override
	public boolean isReady() {
		Utils.sleep(2000);
		return true;
	}

}
