package org.apache.spark.serializer

import org.apache.spark.RpcConf
//import org.apache.spark.rpc.SimpleTask
import org.apache.spark.rpc.deep.FileSerializer
import org.scalatest.FunSuite

class serializerTest extends  FunSuite{


  test("JavaSerializer instances are serializable") {
    val serializer = new JavaSerializer(new RpcConf())
    val instance = serializer.newInstance()
    val obj = instance.deserialize[JavaSerializer](instance.serialize(serializer))
    // enforce class cast
    println(obj.getClass)
  }

  test("Deserialize object containing a primitive Class as attribute") {
    val serializer = new JavaSerializer(new RpcConf())
    val instance = serializer.newInstance()
//    val obj = instance.deserialize[ContainsPrimitiveClass](instance.serialize(
//      new ContainsPrimitiveClass()))
//    // enforce class cast
//    obj.getClass
  }


  test("serializer not with classloder"){

//    val task = new SimpleTask()
//    FileSerializer.writeObject(task,"task.ser")
//
//    val task2 = FileSerializer.readObject("task.ser").asInstanceOf[SimpleTask]
//    task2.run()


  }
}


