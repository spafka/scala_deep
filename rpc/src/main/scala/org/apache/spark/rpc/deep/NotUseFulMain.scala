package org.apache.spark.rpc.deep

object NotUseFulMain {

  def main(args: Array[String]): Unit = {


//    val task = new SimpleTask()
//    FileSerializer.writeObject(task, "task.ser")

    val task2 = FileSerializer.readObject("task.ser").asInstanceOf[Task]
    task2.run()

  }
}
