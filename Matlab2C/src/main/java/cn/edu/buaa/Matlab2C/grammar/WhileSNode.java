/** 
 * @author  LiuCong  
 * @file    WhileSNode.java 
 * @date    Date：2015年12月23日 下午9:19:31 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.grammar;

import cn.edu.buaa.Matlab2C.c.FunctionC;
import cn.edu.buaa.Matlab2C.exception.SemanticsException;

public class WhileSNode extends MatNode {

	public WhileSNode(int line) {
		// TODO Auto-generated constructor stub
		super(line);
	}

	@Override
	public void typeCheck() throws SemanticsException {
		// TODO Auto-generated method stub
		super.typeCheck();
		EleNode eleNode = (EleNode)getChildNodeAt(2);
		StatementNode statementNode = (StatementNode)getLastChildNode();
		eleNode.typeCheck();
		if(!eleNode.getType().isNumber()) {
			throw SemanticsException.mismatchException(getRootNode().getFileName(), line, "while expression's condition should be boolean!");
		}
		statementNode.typeCheck();
	}

	@Override 
	public void toCCode() {
		// TODO Auto-generated method stub
		super.toCCode();
		getChildNodeAt(2).toCCode();
		FunctionC.functionStaString.append("while ("+cStringBuilder.toString()+")");
		cleanCStringBuilder();
		getChildNodeAt(4).toCCode();
	}
	
}
