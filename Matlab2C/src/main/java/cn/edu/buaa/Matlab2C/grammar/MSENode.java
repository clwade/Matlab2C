/** 
 * @author  LiuCong  
 * @file    MSENode.java 
 * @date    Date：2015年12月23日 下午9:28:57 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.grammar;

import java.util.Iterator;

import cn.edu.buaa.Matlab2C.c.FunctionC;
import cn.edu.buaa.Matlab2C.exception.SemanticsException;
import cn.edu.buaa.Matlab2C.symboltbl.Type;

public class MSENode extends MatNode {

	private Type type;
	private boolean setOpe = false;
	private Type leftType;

	public MSENode(int line) {
		// TODO Auto-generated constructor stub
		super(line);
	}

	@Override
	public void typeCheck() throws SemanticsException {
		// TODO Auto-generated method stub
		super.typeCheck();
		Iterator<MatNode> iterator = getChildNodes().iterator();
		MSETNode msetNode = (MSETNode) iterator.next();
		msetNode.typeCheck();
		setOpe = msetNode.isSetOpe();
		Type msetType = msetNode.getType();
		this.type = msetType;
		// if(msetType.isSet()&&msetType.setIString() == null) {
		// throw SemanticsException.mismatchException(getRootNode()
		// .getFileName(), line, "setImplement not set!");
		// }
		while (iterator.hasNext()) {
			StringNode stringNode = (StringNode) iterator.next();
			msetNode = (MSETNode) iterator.next();
			msetNode.typeCheck();
			if(stringNode.getString().equals("+") && this.type.isPoint()) {  //指针的偏移
				if (msetNode.getType().isInteger()){
					continue;
				} else {
					throw SemanticsException.mismatchException(getRootNode()
							.getFileName(), line, stringNode
							+ "point type can only move Integer!");
				}
			}
			if ((!this.type.isEnum() && !this.type.equals(msetNode.getType()))  //避免在做运算时候使用enum的equal方法
					|| (this.type.isEnum() && !msetNode.getType().isNumber())) {
				throw SemanticsException.mismatchException(getRootNode()
						.getFileName(), line, stringNode
						+ " both sides of type mismatch!");
			}
			if (stringNode.getString().equals("--")) {
				setOpe = setOpe || msetNode.isSetOpe();
				if (!this.type.isSet()) {
					throw SemanticsException.mismatchException(getRootNode()
							.getFileName(), line, stringNode
							+ " both sides of type should be set!");
				} else {
					if (msetNode.getType().setIString() != null
							&& (!msetNode.getType().setIString()
									.equals(this.type.setIString()))) {
						throw SemanticsException
								.mismatchException(
										getRootNode().getFileName(),
										line,
										stringNode
												+ " both sides of set implement should be same!");
					}
				}
			} else {
				if (!this.type.isNumber()) {
					throw SemanticsException.mismatchException(getRootNode()
							.getFileName(), line, stringNode
							+ " both sides of type should be number!");
				} else if (stringNode.getString().equals("<<") || stringNode.getString().equals(">>")) {
					if(!msetNode.getType().isInteger()) {
						throw SemanticsException.mismatchException(getRootNode()
								.getFileName(), line, stringNode
								+ " right side should be Integer!");
					}
				}
				if(this.type.isEnum()) {
					this.type = Type.newIntType();//转为整形，防止传参或者返回值类型不匹配
				}
			}
		}
	}

	@Override
	public void toCCode() {
		// TODO Auto-generated method stub
		super.toCCode();
		int scope = pSymTab == null ? 0 : 1;
		if (getChildNodes().size() == 1) {
			if(((MSETNode)getChildNodeAt(0)).isSetOpe()) {
				((MSETNode)getChildNodeAt(0)).setLeftType(this.leftType);
			}
			getChildNodeAt(0).toCCode();
		} else {
			Iterator<MatNode> iterator = getChildNodes().iterator();
			if (this.type.isSet()) {
				MSETNode msetNode = (MSETNode)iterator.next();
				if(msetNode.isSetOpe()) {
					msetNode.setLeftType(this.leftType);
				}
				msetNode.toCCode();
				String name1 = cStringBuilder.toString();
				cleanCStringBuilder();
				while (iterator.hasNext()) {
					iterator.next();
					msetNode = (MSETNode) iterator.next();
					if(msetNode.isSetOpe()) {
						msetNode.setLeftType(this.leftType);
					}
					msetNode.toCCode();
					String name2 = cStringBuilder.toString();
					cleanCStringBuilder();
					if (scope == 0) {
						FunctionC.functionStaString.append(type.setIString() + "_delete_"
								+ type.setIString() + "(" + name1 + ", "
								+ name2 + ");\n");
					} else {
						FunctionC.functionStaString.append(type.setIString()
								+ "_delete_" + type.setIString() + "(" + name1
								+ ", " + name2 + ");\n");
					}
				}
				cStringBuilder.append(name1);
			} else {
				StringBuilder stringBuilder = new StringBuilder();
				while (true) {
					iterator.next().toCCode();
					stringBuilder.append(cStringBuilder);
					cleanCStringBuilder();
					if (iterator.hasNext()) {
						stringBuilder.append(((StringNode) iterator.next())
								.getString());
					} else {
						break;
					}
				}
				cStringBuilder.append(stringBuilder.toString());
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
