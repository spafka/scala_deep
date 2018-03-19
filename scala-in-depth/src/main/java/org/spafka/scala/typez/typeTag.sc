import scala.reflect.runtime.universe._

val l = List(1,2,3)

def getTypeTag[T: TypeTag](obj: T) = typeTag[T]

val theType = getTypeTag(l)

theType.tpe


val decls =theType.tpe.decls.take(10)