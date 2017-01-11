/** 
 * @author  LiuCong  
 * @file    TermNode.java 
 * @date    Date：2015年12月23日 下午9:30:14 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.grammar;

import java.util.Iterator;

import cn.edu.buaa.Matlab2C.c.FunctionC;
import cn.edu.buaa.Matlab2C.exception.SemanticsException;
import cn.edu.buaa.Matlab2C.symboltbl.SetImplement;
import cn.edu.buaa.Matlab2C.symboltbl.Type;

public class TermNode extends MatNode {

	private Type type;
	private boolean setOpe = false;
	private Type leftType;

	public TermNode(int line) {
		// TODO Auto-generated constructor stub
		super(line);
	}

//	@Override
//	public void typeCheck() throws SemanticsException {
//		// TODO Auto-generated method stub
//		super.typeCheck();
//		Iterator<MatNode> iterator = getChildNodes().iterator();
//		MatNode MatNode = iterator.next();
//		if (MatNode instanceof StringNode) {
//			StringNode stringNode = (StringNode) MatNode;
//			if (stringNode.getString().equals("nil")) {
//				this.type = Type.newNilType();
//				return;
//			} else if (stringNode.getString().equals("{")) {
//				this.setOpe = true;
//				EleNode eleNode = (EleNode) iterator.next();
//				eleNode.typeCheck();
//				this.type = Type.newSetType(eleNode.getType());
//				stringNode = (StringNode) iterator.next();
//				while (!stringNode.getString().equals("}")) {
//					eleNode = (EleNode) iterator.next();
//					eleNode.typeCheck();
//					if (!this.type.getSubType(0).equals(eleNode.getType())) {
//						throw SemanticsException.mismatchException(
//								getRootNode().getFileName(), line,
//								"element in set should have the same type!");
//					} else if (!eleNode.getType().isPoint()
//							&& !(eleNode.isSetOpe())) {// <>默认为是指针
//						throw SemanticsException.mismatchException(
//								getRootNode().getFileName(), line,
//								"element in {} should be point type!");
//					}
//					stringNode = (StringNode) iterator.next();
//				}
//	/*			if (iterator.hasNext()) {
//					AtLitNode atLitNode = (AtLitNode) iterator.next();
//					atLitNode.typeCheck();
//					this.type.setSetI(atLitNode.getSetImplement());
//				}*/
//			} else if (stringNode.getString().equals("<")) {
//				this.setOpe = true;
//				this.type = Type.newStructTypeBlank();
//				while (!stringNode.getString().equals(">")) {
//					EleNode eleNode = (EleNode) iterator.next();
//					eleNode.typeCheck();
//					this.type.addSubType(eleNode.getType());
//					stringNode = (StringNode) iterator.next();
//				}
//				if (iterator.hasNext()) {
//					throw SemanticsException.mismatchException(getRootNode()
//							.getFileName(), line,
//							"after '<>' node can not have others!");
//				}
//			} else if (stringNode.getString().equals("(")) {
//				MatNode = iterator.next();
//				if (MatNode instanceof TypeNode) {
//					TypeNode typeNode = (TypeNode) MatNode;
//					typeNode.typeCheck();
//					stringNode = (StringNode) iterator.next();
//					if (stringNode.getString().equals("*")) {
//						this.type = Type.newPointType();
//						this.type.addPointType(typeNode.getType());
//						iterator.next();
//					} else {
//						this.type = typeNode.getType();
//					}
//					//iterator.next();
//					EleNode eleNode = (EleNode) iterator.next();
//					eleNode.typeCheck();
//					if (!(eleNode.getType().isNumber() || eleNode.getType()
//							.isPoint())) {
//						throw SemanticsException.mismatchException(
//								getRootNode().getFileName(), line,
//								"casts can only be Integer or point");
//					}
//				} else {
//					EleNode eleNode = (EleNode) MatNode;
//					eleNode.typeCheck();
//					this.setOpe = eleNode.isSetOpe();
//					this.type = eleNode.getType();
//					iterator.next();
//				}
//			} else if (stringNode.getString().equals("sizeof")) {
//				this.type = Type.newIntType();
//				iterator.next();
//				iterator.next().typeCheck();
//				iterator.next();
//			}
//		} else {
//			if (MatNode instanceof StringLitNode) {
//				this.type = ((StringLitNode) MatNode).getType();
//			} else if (MatNode instanceof UIntLitNode) {
//				this.type = ((UIntLitNode) MatNode).getType();
//			} else if (MatNode instanceof UFloatLitNode) {
//				this.type = ((StringLitNode) MatNode).getType();
//			} else if (MatNode instanceof CharLitNode) {
//				this.type = ((CharLitNode) MatNode).getType();
//			} else if (MatNode instanceof TrueLitNode) {
//				this.type = ((TrueLitNode) MatNode).getType();
//			} else if (MatNode instanceof FalseLitNode) {
//				this.type = ((FalseLitNode) MatNode).getType();
//			} else {
//				if (MatNode instanceof FunctionCallNode) {
//					FunctionCallNode functionCallNode = (FunctionCallNode) MatNode;
//					functionCallNode.typeCheck();
//					this.type = functionCallNode.getType();
//				} else {
//					IdNode idNode = (IdNode) MatNode;
//					if (containsId(idNode.getId())) {
//						this.type = getIdType(idNode.getId());
//					} else {
//						// if(iterator.hasNext()) {
//						throw SemanticsException.undefineException(
//								getRootNode().getFileName(), line,
//								idNode.getId() + " undefined!");
//						// }
//					}
//				}
//			}@Override
////			public void typeCheck() throws SemanticsException {
////			// TODO Auto-generated method stub
////			// TODO 指针为->,非指针为.,有待区分
////			super.typeCheck();
////			Iterator<MatNode> iterator = getChildNodes().iterator();
////			PointIdNode pointIdNode = (PointIdNode) (iterator.next());
////			pointIdNode.typeCheck();
////			StringNode stringNode = (StringNode) (iterator.next());
////			type = Type.newType(getIdType(pointIdNode.getId()));
////			if (pointIdNode.isPoint()) {
////				if (!type.minusPointType(pointIdNode.getType())) {
////					throw SemanticsException.mismatchException(getRootNode()
////							.getFileName(), line, pointIdNode.getId()
////							+ " type mismatch!");
////				}
////			}
////			while (!stringNode.getString().equals("=")) {
////				if (!type.isStruct()) {
////					throw SemanticsException.mismatchException(getRootNode()
////							.getFileName(), line, pointIdNode.getId()
////							+ " is not a struct!");
////				}
////				IdNode idNode = (IdNode) iterator.next();
////				if (stringNode.getString().equals(".")) {
////					if (type.isPoint()) {
////						throw SemanticsException
////								.mismatchException(getRootNode().getFileName(),
////										line, ". should be replaced by ->!");
////					} else {
////						if (!type.containsSubName(idNode.getId())) {
////							throw SemanticsException.mismatchException(
////									getRootNode().getFileName(), line, "subType "
////											+ idNode.getId() + " mistMatch!");
////						} else {
////							type = type.getSubType(idNode.getId());
////						}
////					}
////				} else {
////					if (!type.isPoint()) {
////						throw SemanticsException
////								.mismatchException(getRootNode().getFileName(),
////										line, "-> should be replaced by .!");
////					} else {
////						type = type.getSubType(0);
////						if (!type.containsSubName(idNode.getId())) {
////							throw SemanticsException.mismatchException(
////									getRootNode().getFileName(), line, "subType "
////											+ idNode.getId() + " mistMatch!");
////						} else {
////							type = type.getSubType(idNode.getId());
////						}
////					}
////				}
////				stringNode = (StringNode) (iterator.next());
////			}
////			EleNode eleNode = (EleNode) iterator.next();
////			eleNode.typeCheck();
////			if (!type.equals(eleNode.getType())) {
////				throw SemanticsException.mismatchException(getRootNode()
////						.getFileName(), line,
////						"Left and right sides of the equation type mismatch");
////			}
////		}
//	//
////		@Override
////		public void toCCode() {
////			// TODO Auto-generated method stub
////			super.toCCode();
////			EleNode eleNode = (EleNode) getChildNodeAt(getChildNodes().size() - 1);
////			if (eleNode.isSetOpe()) {
////				eleNode.setLeftType(type);
////			}
////			eleNode.toCCode();
////			String ele = cStringBuilder.toString();
////			cleanCStringBuilder();
////			Iterator<MatNode> iterator = getChildNodes().iterator();
////			iterator.next().toCCode();
////			StringNode stringNode = (StringNode) iterator.next();
////			while (!stringNode.getString().equals("=")) {
////				cStringBuilder.append(stringNode.getString());
////				iterator.next().toCCode();
////				stringNode = (StringNode) iterator.next();
////			}
////			cStringBuilder.append(" = ");
////			if (eleNode.getType().isPoint() && !eleNode.getType().isNilType()
////					&& eleNode.getType().getSubType(0).isVoid()) {
////				if (type.isPoint()) {
////					cStringBuilder.append("(" + type.getSubType(0).getImage() + "*)");
////				} else {
////					cStringBuilder.append("(" + type.getImage() + "*)");
////				}
////			}
////			cStringBuilder.append(ele);
////			FunctionC.functionStaString.append(cStringBuilder.toString());
////			cleanCStringBuilder();
////		}
//
//		}
//		while (iterator.hasNext()) {
//			if (!this.type.isStruct()) {
//				throw SemanticsException.mismatchException(getRootNode()
//						.getFileName(), line,
//						"you can't take subName from non-struct!");
//			}
//			StringNode stringNode = (StringNode) iterator.next();
//			IdNode idNode = (IdNode) iterator.next();
//			if (this.type.isPoint()) {
//				if (stringNode.getString().equals(".")) {
//					throw SemanticsException.mismatchException(getRootNode()
//							.getFileName(), line,
//							"'.' should replaced with '->'");
//				}
//				if (this.type.getSubType(0).containsSubName(idNode.getId())) {
//					this.type = this.type.getSubType(0).getSubType(
//							idNode.getId());
//				} else {
//					throw SemanticsException.undefinedChildException(
//							getRootNode().getFileName(), line,
//							"struct Without this subtype " + idNode.getId());
//				}
//			} else {
//				if (stringNode.getString().equals("->")) {
//					throw SemanticsException.mismatchException(getRootNode()
//							.getFileName(), line,
//							"'->' should replaced with '.'");
//				}
//				if (this.type.containsSubName(idNode.getId())) {
//					this.type = this.type.getSubType(idNode.getId());
//				} else {
//					throw SemanticsException.undefinedChildException(
//							getRootNode().getFileName(), line,
//							"struct Without this subtype " + idNode.getId());
//				}
//			}
//		}
//	}
//
//	@Override
//	public void toCCode() {
//		// TODO Auto-generated method stub
//		super.toCCode();
//		int scope = pSymTab == null ? 0 : 1;
//		Iterator<MatNode> iterator = getChildNodes().iterator();
//		MatNode MatNode = iterator.next();
//		if (MatNode instanceof StringNode) {
//			StringNode stringNode = (StringNode) MatNode;
//			if (stringNode.getString().equals("nil")) {
//				cStringBuilder.append("NULL");
//			} else if (stringNode.getString().equals("{")) {
//				// StringBuilder stringBuilder = new StringBuilder();
//				String setName = productName();
//				if (this.leftType.setIString().equals(
//						SetImplement.Array.toString())) {
//					this.functionC.addFunctionString(this.leftType.setIString()
//							+ " *" + setName + " = "
//							+ this.leftType.setIString()
//							+ "_new(256, BStar_equal);\n");
//				} else {
//					this.functionC.addFunctionString(this.leftType.setIString()
//							+ " *" + setName + " = "
//							+ this.leftType.setIString()
//							+ "_new(BStar_equal);\n");
//				}
//				while (true) {
//					EleNode eleNode = (EleNode) iterator.next();
//					if (eleNode.isSetOpe()) {
//						eleNode.setLeftType(this.leftType.getSubType(0));
//					}
//					eleNode.toCCode();
//					if (!eleNode.isSetOpe() && !eleNode.getType().isPoint()) {
//						FunctionC.functionStaString.append(this.leftType
//								.setIString()
//								+ "_add("
//								+ setName
//								+ ", &("
//								+ cStringBuilder.toString() + "));\n");
//					} else {
//						FunctionC.functionStaString.append(this.leftType
//								.setIString()
//								+ "_add("
//								+ setName
//								+ ", "
//								+ cStringBuilder.toString() + ");\n");
//					}
//					cleanCStringBuilder();
//					if (((StringNode) iterator.next()).getString().equals("}")) {
//						break;
//					}
//				}
//				// if (scope == 1) {
//				// this.FunctionC.addFunctionString(stringBuilder.toString());
//				// } else {
//				// this.FunctionC.addcFile(stringBuilder.toString());
//				// }
//				cStringBuilder.append(setName);
//			} else if (stringNode.getString().equals("<")) { // <>在{}中必须转化为指针（需要malloc申请空间），如果是直接赋给结构体，则不为指针。
//				boolean isInSet = false; // 是否是{}中定义的
//				if (getFatherNode() // OneE
//						.getFatherNode() // UnaryE
//						.getFatherNode() // MSET
//						.getFatherNode() // MSE
//						.getFatherNode() // ConditionTermE
//						.getFatherNode() // AndProE
//						.getFatherNode() // Ele
//						.getFatherNode() // Term
//				instanceof TermNode) {
//					isInSet = true;
//				}
//				StringBuilder stringBuilder = new StringBuilder();
//				String tupleName = productName();
//
//				String point;
//				Type tmpType;
//				if (isInSet) {
//					point = "->";
//					if (this.leftType.isPoint()) { // 左边的类型有可能是指针（例如集合中的类型就直接是指针类型）
//						tmpType = this.leftType.getSubType(0);
//						this.functionC.addFunctionString(this.leftType
//								.getImage() + " " + tupleName);
//						this.functionC
//								.addFunctionString(" = ("
//										+ this.leftType.getImage()
//										+ ")malloc(sizeof("
//										+ this.leftType.getSubType(0)
//												.getImage() + "))");
//					} else {
//						tmpType = this.leftType;
//						this.functionC.addFunctionString(this.leftType
//								.getImage() + " *" + tupleName);
//						this.functionC.addFunctionString(" = ("
//								+ this.leftType.getImage() + "*)malloc(sizeof("
//								+ this.leftType.getImage() + "))");
//					}
//				} else {
//					point = ".";
//					tmpType = this.leftType;
//					this.functionC.addFunctionString(tmpType.getImage() + " "
//							+ tupleName);
//				}
//				this.functionC.addFunctionString(";\n");
//				for (int i = 0; i < tmpType.getSubSize(); i++) {
//					String subName = tmpType.getSubName(i);
//					iterator.next().toCCode();
//					iterator.next();
//					// if (tmpType.getSubType(i).isPoint()) {
//					// stringBuilder.append(tupleName + point + subName
//					// + " = &(" + cStringBuilder.toString() +
//					// ");\n");//TODO这个地方也有要不要加取地址符号的问题
//					// } else {
//					stringBuilder.append(tupleName + point + subName + " = "
//							+ cStringBuilder.toString() + ";\n");
//					// }
//					cleanCStringBuilder();
//				}
//				if (scope == 1) {
//					FunctionC.functionStaString.append(stringBuilder.toString());
//				} else {
//					FunctionC.functionStaString.append(stringBuilder.toString());
//				}
//				cStringBuilder.append(tupleName);
//			} else if (stringNode.getString().equals("(")) {
//				StringBuilder stringBuilder = new StringBuilder();
//				stringBuilder.append("(");
//				while (iterator.hasNext()) {
//					iterator.next().toCCode();
//					stringBuilder.append(cStringBuilder);
//					cleanCStringBuilder();
//				}
//				cStringBuilder.append(stringBuilder);
//			} else if (stringNode.getString().equals("sizeof")) {
//				iterator.next();
//				iterator.next().toCCode();
//				cStringBuilder.insert(0, "sizeof(");
//				cStringBuilder.append(")");
//			}
//		} else {
//			StringBuilder stringBuilder = new StringBuilder();
//			MatNode.toCCode();
//			stringBuilder.append(cStringBuilder);
//			cleanCStringBuilder();
//			while (iterator.hasNext()) {
//				iterator.next().toCCode();
//				stringBuilder.append(cStringBuilder);
//				cleanCStringBuilder();
//			}
//			cStringBuilder.append(stringBuilder);
//		}
//	}

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

	private String productName() {
		String retain = "bStarTemp";
		String name = retain;
		int scope = pSymTab == null ? 0 : 1;
		int num = 0;
		while (true) {
			if (scope == 1) {
				if (pSymTab.containsId(name)) {
					name = retain + (num++);
				} else {
					pSymTab.addVaraT(name, type);
					return name;
				}
			} else {
				if (gSymTab.containsId(name) || incSymTab.containsId(name)) {
					name = retain + (num++);
				} else {
					gSymTab.addVaraT(name, type);
					return name;
				}
			}
		}
	}

	public Type getLeftType() {
		return leftType;
	}

	public void setLeftType(Type leftType) {
		this.leftType = leftType;
	}

}
