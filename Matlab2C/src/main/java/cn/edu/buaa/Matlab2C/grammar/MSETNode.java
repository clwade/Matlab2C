/** 
 * @author  LiuCong  
 * @file    MaeTNode.java 
 * @date    Date：2016年1月4日 下午10:45:08 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.grammar;

import java.util.Iterator;

import cn.edu.buaa.Matlab2C.c.FunctionC;
import cn.edu.buaa.Matlab2C.exception.SemanticsException;
import cn.edu.buaa.Matlab2C.symboltbl.Type;

public class MSETNode extends MatNode {

	private Type type;
	private boolean setOpe = false;
	private Type leftType;

	public MSETNode(int line) {
		// TODO Auto-generated constructor stub
		super(line);
	}

	@Override
	public void typeCheck() throws SemanticsException {
		// TODO Auto-generated method stub
		super.typeCheck();
		Iterator<MatNode> iterator = getChildNodes().iterator();
		UnaryENode unaryeNode = (UnaryENode) iterator.next();
		unaryeNode.typeCheck();
		setOpe = unaryeNode.isSetOpe();
		this.type = unaryeNode.getType();

		// if (this.type.isSet() && this.type.setIString() == null) {
		// throw SemanticsException.mismatchException(getRootNode()
		// .getFileName(), line, "setImplement not set!");
		// }
		while (iterator.hasNext()) {
			StringNode stringNode = (StringNode) iterator.next();
			unaryeNode = (UnaryENode) iterator.next();
			unaryeNode.typeCheck();
			if ((!this.type.isEnum() && !this.type.equals(unaryeNode.getType())) // 避免在做运算时候使用enum的equal方法
					|| (this.type.isEnum() && !unaryeNode.getType().isNumber())) {
				throw SemanticsException.mismatchException(getRootNode()
						.getFileName(), line, stringNode.getString()
						+ " both sides of type mismatch!");
			}
			if (stringNode.getString().equals("\\-/")
					|| stringNode.getString().equals("/-\\")) {
				setOpe = setOpe || unaryeNode.isSetOpe();
				if (!this.type.isSet()) {
					throw SemanticsException.mismatchException(getRootNode()
							.getFileName(), line, stringNode.getString()
							+ " both sides of type should be set!");
				} else {
					if (unaryeNode.getType().setIString() != null
							&& !unaryeNode.getType().setIString()
									.equals(this.type.setIString())) {
						throw SemanticsException
								.mismatchException(
										getRootNode().getFileName(),
										line,
										stringNode.getString()
												+ " both sides of set implement should be same!");
					}
				}
			} else {
				if (!this.type.isNumber()) {
					throw SemanticsException.mismatchException(getRootNode()
							.getFileName(), line, stringNode.getString()
							+ " both sides of type should be number!");
				}
				if (this.type.isEnum()) {
					this.type = Type.newIntType();  //转为整形，防止传参或者返回值类型不匹配
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
			if (((UnaryENode) getChildNodeAt(0)).isSetOpe()) {
				((UnaryENode) getChildNodeAt(0)).setLeftType(this.leftType);
			}
			getChildNodeAt(0).toCCode();
		} else {
			Iterator<MatNode> iterator = getChildNodes().iterator();
			if (type.isSet()) { // TODO 交集的模板实现
				UnaryENode unaryENode = (UnaryENode) iterator.next();
				if (unaryENode.isSetOpe()) {
					unaryENode.setLeftType(this.leftType);
				}
				unaryENode.toCCode();
				String name1 = cStringBuilder.toString();
				cleanCStringBuilder();
				while (iterator.hasNext()) {
					if (((StringNode) iterator.next()).getString().equals(
							"\\-/")) {
						unaryENode = (UnaryENode) iterator.next();
						if (unaryENode.isSetOpe()) {
							unaryENode.setLeftType(this.leftType);
						}
						unaryENode.toCCode();
						String name2 = cStringBuilder.toString();
						cleanCStringBuilder();
						if (scope == 0) {
							FunctionC.functionStaString.append(type.setIString() + "_add_"
									+ type.setIString() + "(" + name1 + ", "
									+ name2 + ");\n");
						} else {
							FunctionC.functionStaString.append(type.setIString()
									+ "_add_" + type.setIString() + "(" + name1
									+ ", " + name2 + ");\n");
						}
					} else {
						unaryENode = (UnaryENode) iterator.next();
						if (unaryENode.isSetOpe()) {
							unaryENode.setLeftType(this.leftType);
						}
						unaryENode.toCCode();
						String name2 = cStringBuilder.toString();
						cleanCStringBuilder();
						if (scope == 0) {
							FunctionC.functionStaString.append(type.setIString()
									+ "_intersection_" + type.setIString()
									+ "(" + name1 + ", " + name2 + ");\n");
						} else {
							FunctionC.functionStaString.append(type.setIString()
									+ "_intersection_" + type.setIString()
									+ "(" + name1 + ", " + name2 + ");\n");
						}
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
