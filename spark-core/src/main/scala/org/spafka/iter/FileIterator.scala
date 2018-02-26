package org.spafka.iter

//这个类提供了文本文件的包装器，在遍历它的时候，它可以列出文件中的每一行
class TextFile(var filename: String) extends Iterable[String] {
  // one method of the Iterable interface
  override def iterator = new TextFileIterator(filename)
}

import java.io.{BufferedReader, FileReader, IOException}

object TextFileIterator {
  def main(args: Array[String]): Unit = {
    val filename = "spark-core/pom.xml"
    //使用增强for循环进行文件的读取
    for (line <- new TextFile(filename)) {
      System.err.println(line)
    }
  }
}

class TextFileIterator(val filename: String)
  extends Iterator[String] { // 打开文件并读取第一行 如果第一行存在获得第一行

  var in: BufferedReader=_
  var nextline: String=_
  try {
    in = new BufferedReader(new FileReader(filename))
    nextline = in.readLine
  } catch {
    case e: IOException =>
      e.printStackTrace()
  }


  // if the next line is non-null then we have a next line
  override def hasNext: Boolean = nextline != null

  // return the next line,but first read the line that follows it
  override def next: String = try {
    val result = nextline
    //if we dont have reached EOF yet
    if (nextline != null) {
      nextline = in.readLine // read another line

      if (nextline == null) in.close() // and close on EOF
    }
    result
  } catch {
    case e: IOException =>
      throw new IllegalArgumentException(e)
  }
}

