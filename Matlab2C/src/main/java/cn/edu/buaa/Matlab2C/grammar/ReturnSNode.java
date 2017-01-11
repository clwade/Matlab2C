/** 
 * @author  LiuCong  
 * @file    ReturnSNode.java 
 * @date    Date：2015年12月23日 下午9:19:06 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.grammar;

import cn.edu.buaa.Matlab2C.c.FunctionC;
import cn.edu.buaa.Matlab2C.exception.SemanticsException;

public class ReturnSNode extends MatNode {

	public ReturnSNode(int line) {
		// TODO Auto-generated constructor stub
		super(line);
	}

	@Override
	public void typeCheck() throws SemanticsException {
		// TODO Auto-generated method stub
		super.typeCheck();
		if(getChildNodes().size() == 2) {
			EleNode eleNode = (EleNode)getChildNodeAt(1);
			eleNode.typeCheck();
			if(!retType.equals(eleNode.getType())) {
				throw SemanticsException.mismatchException(getRootNode().getFileName(), line, "returnType misMatch!");
			}
		} else {
			if(!retType.isVoid()) {
				throw SemanticsException.mismatchException(getRootNode().getFileName(), line, "returnType misMatch!");
			}
		}
	}

	@Override
	public void toCCode() {
		// TODO Auto-generated method stub
		super.toCCode();
		if(getChildNodes().size() == 2) {
			EleNode eleNode = (EleNode)getChildNodeAt(1);
			if(eleNode.isSetOpe()) {
				eleNode.setLeftType(this.retType);
			}
			eleNode.toCCode();
			FunctionC.functionStaString.append("return "+cStringBuilder.toString());
			cleanCStringBuilder();
		} else {
			FunctionC.functionStaString.append("return");
		}
	}
	
}
