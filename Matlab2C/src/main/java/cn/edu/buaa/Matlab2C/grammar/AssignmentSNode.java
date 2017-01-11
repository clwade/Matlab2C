/** 
 * @author  LiuCong  
 * @file    AssignmentNode.java 
 * @date    Date：2015年12月23日 下午9:18:13 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.grammar;

import java.util.Iterator;

import cn.edu.buaa.Matlab2C.c.FunctionC;
import cn.edu.buaa.Matlab2C.exception.SemanticsException;
import cn.edu.buaa.Matlab2C.symboltbl.Type;

public class AssignmentSNode extends MatNode {

	private Type type;

	public AssignmentSNode(int line) {
		// TODO Auto-generated constructor stub
		super(line);
	}

//	@Override
//	public void typeCheck() throws SemanticsException {
//		// TODO Auto-generated method stub
//		// TODO 指针为->,非指针为.,有待区分
//		super.typeCheck();
//		Iterator<MatNode> iterator = getChildNodes().iterator();
//		PointIdNode pointIdNode = (PointIdNode) (iterator.next());
//		pointIdNode.typeCheck();
//		StringNode stringNode = (StringNode) (iterator.next());
//		type = Type.newType(getIdType(pointIdNode.getId()));
//		if (pointIdNode.isPoint()) {
//			if (!type.minusPointType(pointIdNode.getType())) {
//				throw SemanticsException.mismatchException(getRootNode()
//						.getFileName(), line, pointIdNode.getId()
//						+ " type mismatch!");
//			}
//		}
//		while (!stringNode.getString().equals("=")) {
//			if (!type.isStruct()) {
//				throw SemanticsException.mismatchException(getRootNode()
//						.getFileName(), line, pointIdNode.getId()
//						+ " is not a struct!");
//			}
//			IdNode idNode = (IdNode) iterator.next();
//			if (stringNode.getString().equals(".")) {
//				if (type.isPoint()) {
//					throw SemanticsException
//							.mismatchException(getRootNode().getFileName(),
//									line, ". should be replaced by ->!");
//				} else {
//					if (!type.containsSubName(idNode.getId())) {
//						throw SemanticsException.mismatchException(
//								getRootNode().getFileName(), line, "subType "
//										+ idNode.getId() + " mistMatch!");
//					} else {
//						type = type.getSubType(idNode.getId());
//					}
//				}
//			} else {
//				if (!type.isPoint()) {
//					throw SemanticsException
//							.mismatchException(getRootNode().getFileName(),
//									line, "-> should be replaced by .!");
//				} else {@Override
//	public void typeCheck() throws SemanticsException {
//	// TODO Auto-generated method stub
//	// TODO 指针为->,非指针为.,有待区分
//	super.typeCheck();
//	Iterator<MatNode> iterator = getChildNodes().iterator();
//	PointIdNode pointIdNode = (PointIdNode) (iterator.next());
//	pointIdNode.typeCheck();
//	StringNode stringNode = (StringNode) (iterator.next());
//	type = Type.newType(getIdType(pointIdNode.getId()));
//	if (pointIdNode.isPoint()) {
//		if (!type.minusPointType(pointIdNode.getType())) {
//			throw SemanticsException.mismatchException(getRootNode()
//					.getFileName(), line, pointIdNode.getId()
//					+ " type mismatch!");
//		}
//	}
//	while (!stringNode.getString().equals("=")) {
//		if (!type.isStruct()) {
//			throw SemanticsException.mismatchException(getRootNode()
//					.getFileName(), line, pointIdNode.getId()
//					+ " is not a struct!");
//		}
//		IdNode idNode = (IdNode) iterator.next();
//		if (stringNode.getString().equals(".")) {
//			if (type.isPoint()) {
//				throw SemanticsException
//						.mismatchException(getRootNode().getFileName(),
//								line, ". should be replaced by ->!");
//			} else {
//				if (!type.containsSubName(idNode.getId())) {
//					throw SemanticsException.mismatchException(
//							getRootNode().getFileName(), line, "subType "
//									+ idNode.getId() + " mistMatch!");
//				} else {
//					type = type.getSubType(idNode.getId());
//				}
//			}
//		} else {
//			if (!type.isPoint()) {
//				throw SemanticsException
//						.mismatchException(getRootNode().getFileName(),
//								line, "-> should be replaced by .!");
//			} else {
//				type = type.getSubType(0);
//				if (!type.containsSubName(idNode.getId())) {
//					throw SemanticsException.mismatchException(
//							getRootNode().getFileName(), line, "subType "
//									+ idNode.getId() + " mistMatch!");
//				} else {
//					type = type.getSubType(idNode.getId());
//				}
//			}
//		}
//		stringNode = (StringNode) (iterator.next());
//	}
//	EleNode eleNode = (EleNode) iterator.next();
//	eleNode.typeCheck();
//	if (!type.equals(eleNode.getType())) {
//		throw SemanticsException.mismatchException(getRootNode()
//				.getFileName(), line,
//				"Left and right sides of the equation type mismatch");
//	}
//}
//
//@Override
//public void toCCode() {
//	// TODO Auto-generated method stub
//	super.toCCode();
//	EleNode eleNode = (EleNode) getChildNodeAt(getChildNodes().size() - 1);
//	if (eleNode.isSetOpe()) {
//		eleNode.setLeftType(type);
//	}
//	eleNode.toCCode();
//	String ele = cStringBuilder.toString();
//	cleanCStringBuilder();
//	Iterator<MatNode> iterator = getChildNodes().iterator();
//	iterator.next().toCCode();
//	StringNode stringNode = (StringNode) iterator.next();
//	while (!stringNode.getString().equals("=")) {
//		cStringBuilder.append(stringNode.getString());
//		iterator.next().toCCode();
//		stringNode = (StringNode) iterator.next();
//	}
//	cStringBuilder.append(" = ");
//	if (eleNode.getType().isPoint() && !eleNode.getType().isNilType()
//			&& eleNode.getType().getSubType(0).isVoid()) {
//		if (type.isPoint()) {
//			cStringBuilder.append("(" + type.getSubType(0).getImage() + "*)");
//		} else {
//			cStringBuilder.append("(" + type.getImage() + "*)");
//		}
//	}
//	cStringBuilder.append(ele);
//	FunctionC.functionStaString.append(cStringBuilder.toString());
//	cleanCStringBuilder();
//}

//					type = type.getSubType(0);
//					if (!type.containsSubName(idNode.getId())) {
//						throw SemanticsException.mismatchException(
//								getRootNode().getFileName(), line, "subType "
//										+ idNode.getId() + " mistMatch!");
//					} else {
//						type = type.getSubType(idNode.getId());
//					}
//				}
//			}
//			stringNode = (StringNode) (iterator.next());
//		}
//		EleNode eleNode = (EleNode) iterator.next();
//		eleNode.typeCheck();
//		if (!type.equals(eleNode.getType())) {
//			throw SemanticsException.mismatchException(getRootNode()
//					.getFileName(), line,
//					"Left and right sides of the equation type mismatch");
//		}
//	}
//
//	@Override
//	public void toCCode() {
//		// TODO Auto-generated method stub
//		super.toCCode();
//		EleNode eleNode = (EleNode) getChildNodeAt(getChildNodes().size() - 1);
//		if (eleNode.isSetOpe()) {
//			eleNode.setLeftType(type);
//		}
//		eleNode.toCCode();
//		String ele = cStringBuilder.toString();
//		cleanCStringBuilder();
//		Iterator<MatNode> iterator = getChildNodes().iterator();
//		iterator.next().toCCode();
//		StringNode stringNode = (StringNode) iterator.next();
//		while (!stringNode.getString().equals("=")) {
//			cStringBuilder.append(stringNode.getString());
//			iterator.next().toCCode();
//			stringNode = (StringNode) iterator.next();
//		}
//		cStringBuilder.append(" = ");
//		if (eleNode.getType().isPoint() && !eleNode.getType().isNilType()
//				&& eleNode.getType().getSubType(0).isVoid()) {
//			if (type.isPoint()) {
//				cStringBuilder.append("(" + type.getSubType(0).getImage() + "*)");
//			} else {
//				cStringBuilder.append("(" + type.getImage() + "*)");
//			}
//		}
//		cStringBuilder.append(ele);
//		FunctionC.functionStaString.append(cStringBuilder.toString());
//		cleanCStringBuilder();
//	}

}
