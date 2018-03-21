// Generated from /Users/spafka/Desktop/flink/spark_deep/antlr4/src/main/antlr4/org/spafka/antlr4/hello.g4 by ANTLR 4.7
package org.spafka.antlr4;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link helloParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface helloVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link helloParser#r}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitR(helloParser.RContext ctx);
}