/** 
 * @author  LiuCong  
 * @file    ConditionTermENode.java 
 * @date    Date：2015年12月23日 下午9:28:41 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.grammar;

import cn.edu.buaa.Matlab2C.exception.SemanticsException;
import cn.edu.buaa.Matlab2C.symboltbl.Type;

public class ConditionTermENode extends MatNode {

	private Type type;
	private boolean setOpe = false;// 是否是集合操作
	private Type leftType;

	public ConditionTermENode(int line) {
		// TODO Auto-generated constructor stub
		super(line);
	}

	@Override
	public void typeCheck() throws SemanticsException {
		// TODO Auto-generated method stub
		super.typeCheck();
		if (getChildNodes().size() == 1) {
			MatNode MatNode = getChildNodeAt(0);
			if (MatNode instanceof MSENode) {
				MatNode.typeCheck();
				this.setOpe = ((MSENode) MatNode).isSetOpe();
				this.type = ((MSENode) MatNode).getType();
			} else {
				MatNode.typeCheck();
				this.type = Type.newIntType();
			}
		} else {
			MSENode mseNode = (MSENode) getChildNodeAt(0);
			mseNode.typeCheck();
			Type leftType = mseNode.getType();
			StringNode stringNode = (StringNode) getChildNodeAt(1);
			mseNode = (MSENode) getChildNodeAt(2);
			mseNode.typeCheck();
			Type rightType = mseNode.getType();
			this.type = Type.newIntType();
			if (stringNode.getString().equals("!=")
					|| stringNode.getString().equals("==")) {
				if (!leftType.equals(rightType)) {
					throw SemanticsException
							.mismatchException(getRootNode().getFileName(),
									line,
									"condition statement Both sides do not match the type!");
				}
			} else if (!leftType.isNumber() || !rightType.isNumber()) {
				throw SemanticsException.mismatchException(getRootNode()
						.getFileName(), line,
						"condition statement Both sides can not be unNumber!");
			}
		}
	}

	@Override
	public void toCCode() {
		// TODO Auto-generated method stub
		super.toCCode();
		if (getChildNodes().size() == 1) {
			MatNode MatNode = getChildNodeAt(0);
			if (MatNode instanceof MSENode) {
				((MSENode) MatNode).setLeftType(this.leftType);
			}
			MatNode.toCCode();
			/*if (getChildNodeAt(0) instanceof ElementTakeSNode) {
				cStringBuilder.insert(0, "(").append(") != NULL");
			}*/
		} else {
			getChildNodeAt(2).toCCode();
			String string = cStringBuilder.toString();
			cleanCStringBuilder();
			if (((MSENode) getChildNodeAt(0)).getType().isSet()
					&& string.equals("NULL")) {
				Type type = ((MSENode) getChildNodeAt(0)).getType();
				getChildNodeAt(0).toCCode();
				string = cStringBuilder.toString();
				cleanCStringBuilder();
				if (((StringNode) getChildNodeAt(1)).getString().equals("!=")) {
					cStringBuilder.append("!" + type.setIString() + "_isEmpty("
							+ string + ")");
				} else {
					cStringBuilder.append(type.setIString() + "_isEmpty("
							+ string + ")");
				}
			} else {
				string = ((StringNode) getChildNodeAt(1)).getString() + string;
				getChildNodeAt(0).toCCode();
				cStringBuilder.append(string);
			}
		}
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isSetOpe() {
		return this.setOpe;
	}

	public Type getLeftType() {
		return leftType;
	}

	public void setLeftType(Type leftType) {
		this.leftType = leftType;
	}

}
