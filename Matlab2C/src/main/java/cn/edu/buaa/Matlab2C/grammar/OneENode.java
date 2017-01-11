/** 
 * @author  LiuCong  
 * @file    OneENode.java 
 * @date    Date：2015年12月23日 下午9:29:41 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.grammar;


import cn.edu.buaa.Matlab2C.exception.SemanticsException;
import cn.edu.buaa.Matlab2C.symboltbl.Type;

public class OneENode extends MatNode {

	private Type type;
	private boolean setOpe = false;
	private Type leftType;
	
	public OneENode(int line) {
		// TODO Auto-generated constructor stub
		super(line);
	}

	@Override
	public void typeCheck() throws SemanticsException {
		// TODO Auto-generated method stub
		super.typeCheck();
		if(getChildNodes().size() == 1) {
			TermNode termNode = (TermNode)getChildNodeAt(0);
			termNode.typeCheck();
			this.type = termNode.getType();
			setOpe = termNode.isSetOpe();
		} else {
			StringNode stringNode = (StringNode)getChildNodeAt(0);
			OneENode oneENode = (OneENode)getChildNodeAt(1);
			oneENode.typeCheck();
			setOpe = oneENode.isSetOpe();
			if(stringNode.getString().equals("~")) {
				this.type = Type.newIntType();
				if(!oneENode.getType().isNumber()) {
					throw SemanticsException.mismatchException(getRootNode().getFileName(), line, "After '~' should be boolean type!");
				}
			} else if (stringNode.getString().equals("*")) {
				if(oneENode.getType().isPoint()) {
					this.type=Type.newType(oneENode.getType());
					this.type.minusPointType(Type.newPointType());
				} else {
					throw SemanticsException.mismatchException(getRootNode().getFileName(), line, "get value use '*' should occured on pointType!");
				}
			} else {
				if(oneENode.getType().isPoint()) {
					this.type = Type.newPointType();
					this.type.addPointType(oneENode.getType());
				} else {
					this.type = Type.newPointType(oneENode.getType());
				}
			}
		}
	}

	@Override
	public void toCCode() {
		// TODO Auto-generated method stub
		super.toCCode();
		MatNode MatNode = getChildNodeAt(0);
		if(MatNode instanceof StringNode) {
			String string = ((StringNode)MatNode).getString();
			if(string.equals("~")) {
				string = "!";
			}
			getChildNodeAt(1).toCCode();
			cStringBuilder.insert(0, string);
		} else {
			TermNode termNode = (TermNode)MatNode;
			if(termNode.isSetOpe()) {
				termNode.setLeftType(this.leftType);
			}
			termNode.toCCode();
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
