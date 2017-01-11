/** 
 * @author  LiuCong  
 * @file    AndProENode.java 
 * @date    Date：2015年12月23日 下午9:28:18 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.grammar;

import java.util.Iterator;

import cn.edu.buaa.Matlab2C.exception.SemanticsException;
import cn.edu.buaa.Matlab2C.symboltbl.Type;

public class AndProENode extends MatNode {

	private Type type;
	private boolean setOpe = false; //是否是集合操作
	private Type leftType;
	
	public AndProENode(int line) {
		// TODO Auto-generated constructor stub
		super(line);
	}

	@Override
	public void typeCheck() throws SemanticsException {
		// TODO Auto-generated method stub
		super.typeCheck();
		if(getChildNodes().size() == 1) {
			ConditionTermENode conNode = (ConditionTermENode)getChildNodeAt(0);
			conNode.typeCheck();
			setOpe = conNode.isSetOpe();
			this.type = conNode.getType();
		} else {
			this.type = Type.newIntType();
			Iterator<MatNode> iterator = getChildNodes().iterator();
			while(iterator.hasNext()) {
				ConditionTermENode conNode = (ConditionTermENode)iterator.next();
				conNode.typeCheck();
				setOpe = setOpe||conNode.isSetOpe();
				if(!conNode.getType().isNumber()) {
					throw SemanticsException.mismatchException(getRootNode().getFileName(), line, "'&&' both side should be boolean type!");
				}
				if(iterator.hasNext()) {
					iterator.next();
				} else {
					break;
				}
			}
		}
	}
	
	@Override
	public void toCCode() {
		// TODO Auto-generated method stub
		super.toCCode();
		Iterator<MatNode> iterator = getChildNodes().iterator();
		StringBuilder stringBuilder = new StringBuilder();
		while(true) {
			ConditionTermENode conNode = (ConditionTermENode)iterator.next();
			if(conNode.isSetOpe()) {
				conNode.setLeftType(this.leftType);
			}
			conNode.toCCode();
			stringBuilder.append(cStringBuilder);
			cleanCStringBuilder();
			if(iterator.hasNext()) {
				stringBuilder.append("&&");
				iterator.next();
			} else {
				break;
			}
		}
		cStringBuilder.append(stringBuilder);
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public boolean isSetOpe() {
		return setOpe;
	}

	public Type getLeftType() {
		return leftType;
	}

	public void setLeftType(Type leftType) {
		this.leftType = leftType;
	}

}
