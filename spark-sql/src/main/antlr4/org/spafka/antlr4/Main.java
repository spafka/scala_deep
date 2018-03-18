package org.spafka.antlr4;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;

public class Main {

    // @see https://www.cnblogs.com/sld666666/p/6145854.html
    public static void main(String[] args) throws IOException {


        String add = "1 + 2 + 3 * 4+ 6 / 2";

        ANTLRInputStream input = new ANTLRInputStream(add);

        ExprLexer lexer = new ExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprParser parser = new ExprParser(tokens);
        ParseTree tree = parser.prog();                      // 生成语法树
        MyExprVisitor visitor = new MyExprVisitor();
        visitor.visit(tree);
    }

}
