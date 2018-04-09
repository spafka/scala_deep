package org.spafka.antlr4;

import java.util.HashMap;
import java.util.Map;

public class MyExprVisitor extends ExprBaseVisitor<Integer> {
    Map<String,Integer> map=new HashMap<String,Integer>();

    @Override
    public Integer visitProg(ExprParser.ProgContext ctx) {
        return super.visitProg(ctx);
    }

    @Override
    public Integer visitPrintExpr(ExprParser.PrintExprContext ctx) {
        Integer value=visit(ctx.expr());
        System.out.println(value);
        return 0;
    }

    @Override
    public Integer visitAssign(ExprParser.AssignContext ctx) {
        String key=ctx.ID().getText();
        Integer value=visit(ctx.expr());
        map.put(key, value);
        return value;
    }

    @Override
    public Integer visitBlank(ExprParser.BlankContext ctx) {
        return super.visitBlank(ctx);
    }

    @Override
    public Integer visitParens(ExprParser.ParensContext ctx) {
        return super.visitParens(ctx);
    }

    @Override
    public Integer visitMulDiv(ExprParser.MulDivContext ctx) {
        Integer left=visit(ctx.expr(0));        //获取左边表达式最终值
        Integer right=visit(ctx.expr(1));       //获取右边表达式最终值

        if(ctx.op.getType()==ExprLexer.DIV) return left/right;   //如果是除法
        else return left*right;
    }

    @Override
    public Integer visitAddSub(ExprParser.AddSubContext ctx) {
        Integer left=visit(ctx.expr(0));        //获取左边表达式最终值
        Integer right=visit(ctx.expr(1));       //获取右边表达式最终值

        if(ctx.op.getType()==ExprLexer.ADD) return left+right;   //如果是加法
        else return left-right;
    }

    @Override
    public Integer visitId(ExprParser.IdContext ctx) {
        String key=ctx.ID().getText();

        if(map.containsKey(key)){   //如果变量被赋值
            return map.get(key);
        }
        return 0;
    }

    @Override
    public Integer visitInt(ExprParser.IntContext ctx) {
        return Integer.valueOf(ctx.INT().getText());
    }
}
