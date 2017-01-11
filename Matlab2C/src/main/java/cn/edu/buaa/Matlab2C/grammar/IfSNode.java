/** 
 * @author  LiuCong  
 * @file    IfSNode.java 
 * @date    Date：2015年12月23日 下午9:20:37 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.grammar;

import java.util.Iterator;

import cn.edu.buaa.Matlab2C.c.FunctionC;
import cn.edu.buaa.Matlab2C.exception.SemanticsException;

public class IfSNode extends MatNode {

	public IfSNode(int line) {
		// TODO Auto-generated constructor stub
		super(line);
	}

	@Override
	public void typeCheck() throws SemanticsException {
		// TODO Auto-generated method stub
		super.typeCheck();
		Iterator<MatNode> iterator = getChildNodes().iterator();
		iterator.next();
		iterator.next();
		EleNode eleNode  = (EleNode)iterator.next();
		eleNode.typeCheck();
		if(!eleNode.getType().isNumber()) {
			throw SemanticsException.mismatchException(getRootNode().getFileName(), line, "if expression's condition should be boolean!");
		}
		iterator.next();
		StatementNode statementNode = (StatementNode)iterator.next();
		statementNode.typeCheck();
		if(iterator.hasNext()) {
			iterator.next();
			statementNode = (StatementNode)iterator.next();
			statementNode.typeCheck();
		}
	}

	@Override
	public void toCCode() {
		// TODO Auto-generated method stub
		super.toCCode();
		getChildNodeAt(2).toCCode();
		FunctionC.functionStaString.append("if ("+cStringBuilder.toString()+")");
		cleanCStringBuilder();
		getChildNodeAt(4).toCCode();
		FunctionC.functionStaString.append(cStringBuilder.toString());
		cleanCStringBuilder();
		if(getChildNodes().size()>5) {
			FunctionC.functionStaString.append(" else ");
			getChildNodeAt(6).toCCode();
		}
		FunctionC.functionStaString.append(cStringBuilder.toString());
		cleanCStringBuilder();
	}
	
}
