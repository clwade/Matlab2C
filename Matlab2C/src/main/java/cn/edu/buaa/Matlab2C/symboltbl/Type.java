/** 
 * @author  LiuCong  
 * @file    test.java
 * @date    Date：2015年12月20日 下午2:55:03 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.symboltbl;

import static cn.edu.buaa.Matlab2C.symboltbl.TypeConstants.CHAR_TYPE;
import static cn.edu.buaa.Matlab2C.symboltbl.TypeConstants.DOUBLE_TYPE;
import static cn.edu.buaa.Matlab2C.symboltbl.TypeConstants.ENUM_TYPE;
import static cn.edu.buaa.Matlab2C.symboltbl.TypeConstants.FLOAT_TYPE;
import static cn.edu.buaa.Matlab2C.symboltbl.TypeConstants.INT_TYPE;
import static cn.edu.buaa.Matlab2C.symboltbl.TypeConstants.LONG_TYPE;
import static cn.edu.buaa.Matlab2C.symboltbl.TypeConstants.L_LONG_TYPE;
import static cn.edu.buaa.Matlab2C.symboltbl.TypeConstants.POINT_TYPE;
import static cn.edu.buaa.Matlab2C.symboltbl.TypeConstants.SET_TYPE;
import static cn.edu.buaa.Matlab2C.symboltbl.TypeConstants.SHORT_TYPE;
import static cn.edu.buaa.Matlab2C.symboltbl.TypeConstants.STRING_TYPE;
import static cn.edu.buaa.Matlab2C.symboltbl.TypeConstants.STRUCT_TYPE;
import static cn.edu.buaa.Matlab2C.symboltbl.TypeConstants.U_CHAR_TYPE;
import static cn.edu.buaa.Matlab2C.symboltbl.TypeConstants.U_DOUBLE_TYPE;
import static cn.edu.buaa.Matlab2C.symboltbl.TypeConstants.U_FLOAT_TYPE;
import static cn.edu.buaa.Matlab2C.symboltbl.TypeConstants.U_INT_TYPE;
import static cn.edu.buaa.Matlab2C.symboltbl.TypeConstants.U_LONG_TYPE;
import static cn.edu.buaa.Matlab2C.symboltbl.TypeConstants.U_L_LONG_TYPE;
import static cn.edu.buaa.Matlab2C.symboltbl.TypeConstants.U_SHORT_TYPE;
import static cn.edu.buaa.Matlab2C.symboltbl.TypeConstants.VOID_TYPE;
import static cn.edu.buaa.Matlab2C.symboltbl.TypeConstants.NIL_TYPE;
import static cn.edu.buaa.Matlab2C.symboltbl.TypeConstants.PROPOSITION_TYPE;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//import cn.edu.buaa.bstar.grammar.BStarConstants;

public final class Type {

	// public final int type;
	public TypeConstants type;
	private String image; // enum中每个元素都有统一的对外类型,集合类型中元素的名称，其他的存储的是类型的名称（为了强制类型转换,image为“blank”表示为<>定义的结构体
	// private String originType; // typedef原类型
	private final List<Type> subTypes; // 子类型（enum,struct,set，指针(最后一个为类型，剩下的为指针的层数)）
	private final List<String> subNames;
	private int pointweight = 0; // 指针的重数

	private SetImplement setI;// 集合的实现类型

	// private String setImplementS;

	private static final List<String> specialTypes = new LinkedList<String>(
			Arrays.asList("List", "Array", "Stack", "Queue", "Integer"));

	private Type(TypeConstants type) {
		super();
		this.type = type;
		this.image = type.getString();
		// this.originType = null;
		subTypes = new LinkedList<Type>();
		subNames = new LinkedList<String>();
	}

	private Type(TypeConstants type, String image) {
		super();
		this.type = type;
		this.image = image;
		// this.originType = null;
		subTypes = new LinkedList<Type>();
		subNames = new LinkedList<String>();
	}

	private Type(Type other) {
		super();
		this.type = other.type;
		this.image = other.image;
		// this.originType = other.originType;
		this.pointweight = other.pointweight;
		this.subTypes = new LinkedList<Type>(other.subTypes);
		this.subNames = new LinkedList<String>(other.subNames);
		if (other.isSet()) {
			this.setI = SetImplement.fromString(other.setIString());
		}
	}

	public boolean isString() {
		return type == STRING_TYPE || type == NIL_TYPE;
	}

	public boolean isEnum() {
		return type == ENUM_TYPE;
	}

	public boolean isPoint() {
		return type == POINT_TYPE || type == NIL_TYPE;
	}

	public boolean isSet() {
		return type == SET_TYPE || type == NIL_TYPE;
	}

	/*
	 * 是否是整型常量
	 */
	public boolean isInteger() {
		return this.type == INT_TYPE || this.type == U_INT_TYPE
				|| this.type == CHAR_TYPE || this.type == U_CHAR_TYPE;
	}

	public boolean isPro() {
		return this.type == PROPOSITION_TYPE;
	}

	public boolean isStruct() {
		if (type == STRUCT_TYPE) {
			return true;
		} else if (type == POINT_TYPE && pointweight == 1
				&& subTypes.get(0).type == STRUCT_TYPE) {
			return true;
		}
		return false;
	}

	/**
	 * 参数为纯指针，调用方法的对象为带有具体类型的指针
	 * 
	 * @param other
	 * @return
	 */
	public boolean minusPointType(Type other) {
		if (!isPoint()) {
			return false;
		}
		if (!other.isPoint()) {
			return false;
		}
		if (other.pointweight > pointweight) {
			return false;
		} else if (other.pointweight < pointweight) {
			pointweight -= other.pointweight;
		} else {
			this.exchangeType(getSubType(0));
		}
		return true;
	}

	/**
	 * 参数为带具体类型的类型，调用方法的对象为纯指针
	 * 
	 * @param type
	 */
	public void addPointType(Type type) {
		if (type.isPoint()) {
			pointweight += type.pointweight;
			subTypes.addAll(type.subTypes);
		} else {
			subTypes.add(type);
		}
	}

	public void addPointWeight() {
		pointweight++;
	}

	public boolean isNumber() {
		return type.isRange(CHAR_TYPE, DOUBLE_TYPE)
				|| type.isRange(U_CHAR_TYPE, U_DOUBLE_TYPE)
				|| type == ENUM_TYPE || type == PROPOSITION_TYPE;
	}

	public boolean isNormal() {
		return type.isRange(CHAR_TYPE, STRING_TYPE)
				|| type.isRange(U_CHAR_TYPE, PROPOSITION_TYPE);
	}

	public boolean isUsigned() {
		return type.isRange(U_CHAR_TYPE, U_DOUBLE_TYPE);
	}

	public boolean isVoid() {
		return type == VOID_TYPE;
	}

	public boolean isListSet() {
		return setI == SetImplement.List;
	}

	public boolean isArraySet() {
		return setI == SetImplement.Array;
	}

	public boolean isNullSet() {
		return setI == null;
	}

	public boolean isNilType() {
		return type == NIL_TYPE;
	}

	public static boolean isSpecialType(String image) {
		return specialTypes.contains(image);
	}

	public Type asUsigned() {
		switch (this.type) {
		case CHAR_TYPE:
			return new Type(U_CHAR_TYPE);
		case SHORT_TYPE:
			return new Type(U_SHORT_TYPE);
		case INT_TYPE:
			return new Type(U_INT_TYPE);
		case LONG_TYPE:
			return new Type(U_LONG_TYPE);
		case L_LONG_TYPE:
			return new Type(U_L_LONG_TYPE);
		case FLOAT_TYPE:
			return new Type(U_FLOAT_TYPE);
		case DOUBLE_TYPE:
			return new Type(U_DOUBLE_TYPE);
		default:
			throw new IllegalArgumentException(this + " cant be as usigned!");
		}
	}

	// 获取类型的名称
	@Override
	public String toString() {
		if (image != null) {
			return image;
		} else {
			return this.type.toString();
		}
	}

	public void setSetI(SetImplement setI) {
		if (!isSet()) {
			throw new IllegalArgumentException("Not a set!");
		}
		this.setI = setI;
	}

	public String setIString() {
		return setI == null ? null : setI.toString();
	}

	// public void setOriginType(String originType) {
	// this.originType = originType;
	// }
	//
	// public String getOriginType() {
	// return this.originType;
	// }

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return image;
	}

	public int getSubSize() {
		return subTypes.size();
	}

	public boolean subNameIsEmpty() {
		return subNames.isEmpty();
	}

	public Type getSubType(int index) {
		return subTypes.get(index);
	}

	public Type getSubType(String name) {
		Iterator<String> i = subNames.iterator();
		Iterator<Type> j = subTypes.iterator();
		while (i.hasNext()) {
			String n = i.next();
			Type t = j.next();
			if (name.equals(n)) {
				return t;
			}
		}
		return null;
	}

	public String getSubName(int index) {
		return subNames.get(index);
	}

	public boolean containsSubName(String name) {
		return subNames.contains(name);
	}

	// public boolean addSubType(Type type) {
	// if (!this.isSet() && !this.isStruct() && !this.isPorint()) {
	// throw new IllegalArgumentException(this
	// + " is allowed to add subTypes.");
	// } else if ((this.isSet() || this.isPorint())
	// && this.subTypes.size() == 1) {
	// throw new IllegalArgumentException("Set type " + this
	// + " is allowed to add more than two subTypes.");
	// }
	// subTypes.add(type);
	// return subNames.add(null);
	// }

	public boolean addSubType(Type type, String name) {
		subTypes.add(type);
		return subNames.add(name);
	}

	public boolean addSubType(Type type) {
		return subTypes.add(type);
	}

	public static boolean typeCheck(Type t1, Type t2) {

		if (t1 == null && t2 == null || t1 == null && t2 != null
				&& (t2.isPoint() || t2.isSet()) || t2 == null && t1 != null
				&& (t1.isPoint() || t1.isSet())) {
			return true;
		}

		if (t1 == null && t2.isString() || t2 == null && t1.isString()) {
			return true;
		}

		if (t1 == null || t2 == null) {
			return false;
		}

		if (t1.type == VOID_TYPE || t2.type == VOID_TYPE) {
			return true;
		}

		if (t1.isEnum() || t1.isEnum()) {
			if (!(t1.image == t2.image || t1.image != null
					&& t1.image.equals(t2.image))) {
				return false;
			}
		}

		if (t1.isStruct() && t2.isStruct()) {
			if (t1.getSubSize() == 0 || t2.getSubSize() == 0) {
				return true;
			}
		}

		if (t1.isNumber() && t2.isNumber()) {
			return true;
		}

		if (t1.getSubSize() != t2.getSubSize()) {
			return false;
		}

		if (t1.type != t2.type) {
			return false;
		}

		// for (Iterator<String> i = t1.subNames.iterator(), j = t2.subNames
		// .iterator(); i.hasNext();) {
		// String tt1 = i.next();
		// String tt2 = j.next();
		// if (!(tt1 == tt2 || tt1 != null && tt1.equals(tt2))) {
		// return false;
		// }
		// }

		for (Iterator<Type> i = t1.subTypes.iterator(), j = t2.subTypes
				.iterator(); i.hasNext();) {
			Type tt1 = i.next();
			Type tt2 = j.next();
			if (!typeCheck(tt1, tt2)) {
				return false;
			}
		}

		return false;

	}

	@Override
	public boolean equals(Object obj) {
		// TODO struct需要原类型相同或者子类型的type一致,指针的赋值还需要接着细化, string、指针、set类型与nil相等
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Type)) {
			return false;
		}
		Type t = (Type) obj;

		if (((isString() || isPoint() || isSet()) && t.isNilType())
				|| (t.isString() || t.isPoint() || t.isSet()) && isNilType()) {
			return true;
		}

		if (isVoid() && t.isVoid()) {
			return true;
		}

		// void*与任何指针匹配
		if (t.isPoint()
				&& isPoint()
				&& !t.isNilType()
				&& !isNilType()
				&& ((t.getSubType(0).isVoid() && !getSubType(0).isVoid()) 
						|| (!t.getSubType(0).isVoid() && getSubType(0).isVoid()))
				&& pointweight == t.pointweight) {
			return true;
		}

		if (isEnum()) { // enum可以当做整形赋给任意的数值型变量，但是数值型变量不能赋给enum(enum跟数字做加法会判断不行，但是在编译器中+，-等都是通过判断是不是number处理的，不会有问题)
			if (!(image == t.image || image != null && image.equals(t.image))) {
				return false;
			} else {
				return true;
			}
		}

		if (t.isNumber() && isNumber()) {
			return true;
		}

		if ((isPoint() && t.isPoint())) {
			// if (setIString() != null) {
			// if (t.setIString() != null
			// && !setIString().equals(t.setIString())) {
			// return false;
			// }
			// }
			if (pointweight != 0 && pointweight != t.pointweight) {
				return false;
			}
			if (t.subTypes.size() != subTypes.size()) {
				return false;
			}
			if (t.getSubSize() == 0) {
				return true;
			} else {
				Type t1 = getSubType(0);
				Type t2 = t.getSubType(0);

				if (!(t1 == t2 || (t1 != null && t1.equals(t2)))) {
					return false;
				} else {
					return true;
				}
			}
			// 方便<a,b,c>通过类型检测
			// if (t.subNames.size() != subNames.size()) {
			// return false;
			// }
			// for (Iterator<String> i = subNames.iterator(), j = t.subNames
			// .iterator(); i.hasNext();) {
			// String t1 = i.next();
			// String t2 = j.next();
			// if (!(t1 == t2 || t1 != null && t1.equals(t2))) {
			// return false;
			// }
			// }
		}

		if (isStruct() && t.isStruct()) {
			if (getSubSize() != t.getSubSize()) {
				return false;
			} else {
				if (image.equals(t.getImage())) {
					return true;
				}
				if (!(subNames.isEmpty() || t.subNames.isEmpty())) {
					for (Iterator<String> i = subNames.iterator(), j = t.subNames
							.iterator(); i.hasNext();) {
						String t1 = i.next();
						String t2 = j.next();
						if (!(t1 == t2 || t1 != null && t1.equals(t2))) {
							return false;
						}
					}
				}
				if (!(subTypes.isEmpty() || t.subTypes.isEmpty())) {
					for (Iterator<Type> i = subTypes.iterator(), j = t.subTypes
							.iterator(); i.hasNext();) {
						Type t1 = i.next();
						Type t2 = j.next();
						if (!(t1 == t2 || (t1 != null && t1.equals(t2)))) {
							return false;
						}
					}
				}
				return true;
			}
		}

		if ((isSet() && t.isSet())) {
			if (setIString() != null) {
				if (t.setIString() != null
						&& !setIString().equals(t.setIString())) {
					return false;
				}
			}
			if (t.subTypes.size() != subTypes.size()) {
				return false;
			}
			Type type2 = t.subTypes.get(0).isPoint() ? t.getSubType(0)
					.getSubType(0) : t.getSubType(0);
			Type type1 = subTypes.get(0).isPoint() ? getSubType(0)
					.getSubType(0) : getSubType(0);
			if (type1 != null && !type1.equals(type2)
					|| (type2 != null && !type2.equals(type1))) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int re = 17;
		re = 31 * re + type.hashCode();

		if (isEnum()) {
			// if (image != null) {
			re = 31 * re + image.hashCode();
			// }
			return re;
		}

		re = re + subTypes.hashCode() + subNames.hashCode();
		return re;
	}

	// @Override
	// public String toString() {
	// if (image != null) {
	// return image;
	// }
	// // String re = StringUtils.substringBetween(tokenImage[type], "\"");
	// String re = type.getString();
	// if (isPoint()) {
	// re = this.getSubType(0).toString() + re;
	// } else {
	// if (isSet()) {
	// re += "<";
	// } else if (isStruct()) {
	// re += "{";
	// }
	// Iterator<Type> i = subTypes.iterator();
	// Iterator<String> j = subNames.iterator();
	// while (i.hasNext()) {
	// String s = i.next().toString();
	// String st = j.next();
	// if (st != null) {
	// s += " " + st;
	// }
	// re += s;
	// if (isStruct()) {
	// re += ";";
	// }
	// }
	// if (isSet()) {
	// re += ">";
	// } else if (isStruct()) {
	// re += "}";
	// }
	// }
	// return re;
	// }
	//
	// public String toCString() {
	// String re = type.getString();
	// if (isPoint()) {
	// re = this.getSubType(0).toString() + re;
	// } else {
	// if (isSet()) {
	// re += "<";
	// } else if (isStruct()) {
	// re += "{";
	// }
	// Iterator<Type> i = subTypes.iterator();
	// Iterator<String> j = subNames.iterator();
	// while (i.hasNext()) {
	// String s = i.next().toString();
	// String st = j.next();
	// if (st != null) {
	// s += " " + st;
	// }
	// re += s;
	// if (isStruct()) {
	// re += ";";
	// }
	// }
	// if (isSet()) {
	// re += ">";
	// } else if (isStruct()) {
	// re += "}";
	// }
	// }
	// return re;
	// }
	//
	// public String subN(String name) {
	// if (isPoint()) {
	// return getSubType(0).subN(name);
	// }
	// if (!isStruct()) {
	// return null;
	// }
	//
	// if (containsSubName(name)) {
	// return name;
	// }
	// int index = 0;
	// for (Type t : subTypes) {
	// String n = t.subN(name);
	// if (n != null) {
	// return getSubName(index) + "." + n;
	// }
	// index++;
	// }
	// return null;
	// }
	//
	// public Type subT(String name) {
	// if (isPoint()) {
	// return getSubType(0).subT(name);
	// }
	// if (!isStruct()) {
	// return null;
	// }
	// Type rt = getSubType(name);
	// if (rt != null) {
	// return rt;
	// }
	// for (Type t : subTypes) { // 扁平化处理，所以有可能在结构体子元素
	// Type n = t.subT(name);
	// if (n != null) {
	// return n;
	// }
	// }
	// return null;
	// }

	// public List<Type> getSubTypes() {
	// return subTypes;
	// }

	/**
	 * 方便指针的运算
	 * 
	 * @param other
	 * @return
	 */
	public static Type newType(Type other) {
		return new Type(other);
	}

	public static Type newCharType() {
		return new Type(CHAR_TYPE);
	}

	public static Type newShortType() {
		return new Type(SHORT_TYPE);
	}

	public static Type newIntType() {
		return new Type(INT_TYPE);
	}

	public static Type newLongType() {
		return new Type(LONG_TYPE);
	}

	public static Type newLLongType() {
		return new Type(L_LONG_TYPE);
	}

	public static Type newFloatType() {
		return new Type(FLOAT_TYPE);
	}

	public static Type newDoubleType() {
		return new Type(DOUBLE_TYPE);
	}

	public static Type newStringType() {
		return new Type(STRING_TYPE);
	}

	public static Type newVoidType() {
		return new Type(VOID_TYPE);
	}

	public static Type newUCharType() {
		return new Type(U_CHAR_TYPE);
	}

	public static Type newUShortType() {
		return new Type(U_SHORT_TYPE);
	}

	public static Type newUIntType() {
		return new Type(U_INT_TYPE);
	}

	public static Type newULongType() {
		return new Type(U_LONG_TYPE);
	}

	public static Type newULLongType() {
		return new Type(U_L_LONG_TYPE);
	}

	public static Type newUFloatType() {
		return new Type(U_FLOAT_TYPE);
	}

	public static Type newUDoubleType() {
		return new Type(U_DOUBLE_TYPE);
	}

	// enum类型
	public static Type newEnumType(String image) {
		return new Type(ENUM_TYPE, image);
	}

	public static Type newStructType() {
		return new Type(STRUCT_TYPE);
	}

	public static Type newStructTypeBlank() {
		Type type = new Type(STRUCT_TYPE);
		type.setImage("blank");
		return type;
	}

	// // typedef定义的类型，除了基本类型以外
	// public static Type newTypedefType(TypeConstants typeCon, String
	// originType) {
	// Type type = new Type(typeCon);
	// type.setOriginType(originType);
	// return type;
	// }

	public void exchangeType(Type other) {
		if (this == other) {
			return;
		}
		this.type = other.type;
		image = other.image;
		subNames.clear();
		subNames.addAll(other.subNames);
		subTypes.clear();
		subTypes.addAll(other.subTypes);
		pointweight = other.pointweight;
		if (isSet()) {
			setI = SetImplement.fromString(other.setIString());
		}
	}

	public static Type newPointType(Type type) {
		Type re = new Type(POINT_TYPE);
		re.pointweight = 1;
		re.subTypes.add(type);
		re.subNames.add(null);
		return re;
	}

	public static Type newPointType() {
		Type re = new Type(POINT_TYPE);
		re.pointweight = 1;
		return re;
	}

	public static Type newSetType(String name, Type type) {
		Type re = new Type(SET_TYPE);
		re.image = name;
		re.subTypes.add(type);
		re.subNames.add(null);
		return re;
	}

	public static Type newSetType(Type type) {
		Type re = new Type(SET_TYPE);
		re.subTypes.add(type);
		re.subNames.add(null);
		return re;
	}

	public static Type newNilType() {
		return new Type(NIL_TYPE);
	}

	public static Type newPropositionType() {
		// TODO Auto-generated method stub
		return new Type(PROPOSITION_TYPE);
	}

	// public final static Type EMPTY_STRUCT = Type.newStructType(false);
	public static void main(String[] args) {
		Type type = newPointType();
		Type type2 = newPointType();
		type.addPointType(type2);
		System.out.println(type.pointweight);
	}

}
