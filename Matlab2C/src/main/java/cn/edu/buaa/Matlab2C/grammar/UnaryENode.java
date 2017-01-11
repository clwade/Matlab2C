/** 
 * @author  LiuCong  
 * @file    UnaryENode.java 
 * @date    Date：2015年12月23日 下午9:29:23 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.grammar;

import java.util.Iterator;

import cn.edu.buaa.Matlab2C.exception.SemanticsException;
import cn.edu.buaa.Matlab2C.symboltbl.Type;

public class UnaryENode extends MatNode {

	private Type type;
	private boolean setOpe = false;
	private Type leftType;
	
	public UnaryENode(int line) {
		// TODO Auto-generated constructor stub
		super(line);
	}

	@Override
	public void typeCheck() throws SemanticsException {
		// TODO Auto-generated method stub
		super.typeCheck();
		Iterator<MatNode> iterator = getChildNodes().iterator();
		MatNode MatNode = iterator.next();
		if(MatNode instanceof StringNode) {
			TermNode termNode = (TermNode)iterator.next();
			termNode.typeCheck();
			if(!termNode.getType().isNumber()) {
				throw SemanticsException.mismatchException(getRootNode().getFileName(), line, ((StringNode)MatNode).getString()+" both side type should be number!");
			}
			if(termNode.getType().isEnum()) {
				this.type = Type.newIntType();
			} else {
				this.type = termNode.getType();
			}
		} else {
			MatNode.typeCheck();
			this.type = ((OneENode)MatNode).getType();
			this.setOpe = ((OneENode)MatNode).isSetOpe();
		}
	}

	@Override
	public void toCCode() {
		// TODO Auto-generated method stub
		super.toCCode();
		MatNode MatNode = getChildNodeAt(0);
		if(MatNode instanceof StringNode) {
			String string = ((StringNode)MatNode).getString();
			getChildNodeAt(1).toCCode();
			cStringBuilder.insert(0, string);
		} else {
			if(((OneENode)MatNode).isSetOpe()) {
				((OneENode)MatNode).setLeftType(this.leftType);
			}
			MatNode.toCCode();
		}
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isSetOpe() {
		// TODO Auto-generated method stub
		return this.setOpe;
	}

	public Type getLeftType() {
		return leftType;
	}

	public void setLeftType(Type leftType) {
		this.leftType = leftType;
	}
	
}
