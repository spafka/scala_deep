// Generated from /Users/spafka/Desktop/flink/spark_deep/antlr4/src/main/antlr4/org/spafka/antlr4/hello.g4 by ANTLR 4.7
package org.spafka.antlr4;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**
 * This class provides an empty implementation of {@link helloVisitor},
 * which can be extended to create a visitor which only needs to handle a subset
 * of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public class helloBaseVisitor<T> extends AbstractParseTreeVisitor<T> implements helloVisitor<T> {
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitR(helloParser.RContext ctx) { return visitChildren(ctx); }
}