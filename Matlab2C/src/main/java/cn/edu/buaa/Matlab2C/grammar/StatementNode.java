/** 
 * @author  LiuCong  
 * @file    StatementNode.java 
 * @date    Date：2015年12月23日 下午9:16:50 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.grammar;

import java.util.Iterator;

import cn.edu.buaa.Matlab2C.c.FunctionC;
import cn.edu.buaa.Matlab2C.exception.SemanticsException;

public class StatementNode extends MatNode {

	public StatementNode(int line) {
		// TODO Auto-generated constructor stub
		super(line);
	}

	@Override
	public void typeCheck() throws SemanticsException {
		// TODO Auto-generated method stub
		super.typeCheck();
		Iterator<MatNode> iterator = getChildNodes().iterator();
		while(iterator.hasNext()) {
			MatNode MatNode = iterator.next();
			if(MatNode instanceof StringNode) {
				continue;
			}
			MatNode.typeCheck();
		}
	}

	@Override
	public void toCCode() {
		// TODO Auto-generated method stub
		super.toCCode();
		Iterator<MatNode> iterator = getChildNodes().iterator();
		MatNode MatNode = iterator.next();
		if(MatNode instanceof StringNode) {
			if(((StringNode)MatNode).getString().equals(";")) {
				FunctionC.functionStaString.append(";\n");
			} else {
				FunctionC.functionStaString.append("{\n");
				MatNode = iterator.next();
				while(MatNode instanceof StatementNode) {
					MatNode.toCCode();
					MatNode = iterator.next();
				}
				FunctionC.functionStaString.append("}\n");
			}
		} else {
			MatNode.toCCode();
			FunctionC.functionStaString.append(cStringBuilder.toString());
			cleanCStringBuilder();
			if(iterator.hasNext()) {
				FunctionC.functionStaString.append(";\n");
			}
		}
	}
}
