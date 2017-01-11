/** 
 * @author  LiuCong  
 * @file    test.java
 * @date    Date：2015年12月20日 下午2:55:03 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.symboltbl;

public enum TypeConstants {

	/**
	 * char 类型
	 */
	CHAR_TYPE,
	/**
	 * short 类型
	 */
	SHORT_TYPE,
	/**
	 * int
	 */
	INT_TYPE,
	/**
	 * long
	 */
	LONG_TYPE,
	/**
	 * long long
	 */
	L_LONG_TYPE,
	/**
	 * float
	 */
	FLOAT_TYPE,
	/**
	 * double
	 */
	DOUBLE_TYPE,
	/**
	 * string
	 */
	STRING_TYPE,

	/**
	 * enum
	 */
	ENUM_TYPE,
	/**
	 * set<?>
	 */
	SET_TYPE,
	/**
	 * struct
	 */
	STRUCT_TYPE,
	/**
	 * pointer
	 */
	POINT_TYPE,

	/**
	 * void
	 */
	VOID_TYPE,

	/**
	 * usigned char
	 */
	U_CHAR_TYPE,
	/**
	 * usigned short
	 */
	U_SHORT_TYPE,
	/**
	 * usigned int
	 */
	U_INT_TYPE,
	/**
	 * usigned long
	 */
	U_LONG_TYPE,
	/**
	 * usigned long long
	 */
	U_L_LONG_TYPE,
	/**
	 * usigned float
	 */
	U_FLOAT_TYPE,
	/**
	 * usigned double
	 */
	U_DOUBLE_TYPE,
	/**
	 * proposition
	 */
	PROPOSITION_TYPE,
	/**
	 * nil 
	 */
	NIL_TYPE;
	// private final static String[] images = { "char", "short", "int", "long",
	// "long long", "float", "double", "propersition", "string", "enum",
	// "set", "struct", "*", "void", "unsigned char", "unsigned short",
	// "unsigned int", "unsigned long", "unsigned long long",
	// "unsigned float", "unsigned double", "const char", "const short",
	// "const int", "const long", "const long long", "const float",
	// "const double", "const propersition", "const string", "const enum",
	// "const set", "const struct", "const *", "const void",
	// "const unsigned char", "const unsigned short",
	// "const unsigned int", "const unsigned long",
	// "const unsigned long long", "const unsigned float",
	// "const unsigned double" };

	public boolean isRange(TypeConstants from, TypeConstants to) {
		return this.ordinal() >= from.ordinal()
				&& this.ordinal() <= to.ordinal();
	}

	public String getString() {
		return this.toString().toLowerCase()
				.replaceFirst("l_", "long ").replaceFirst("u_", "unsigned ")
				.replaceFirst("point", "*").replaceFirst("_type", "");
		// return images[this.ordinal()];
	}

//	@Override
//	public String toString() {
//		return null;
//		
////		return this.toString().toLowerCase()
////				.replaceFirst("l_", "long ").replaceFirst("u_", "unsigned ")
////				.replaceFirst("point", "*").replaceFirst("_type", "");
//	}

	TypeConstants asUsigned() {
		if (!isRange(CHAR_TYPE, DOUBLE_TYPE)) {
			throw new IllegalArgumentException(this + " cant be as usigned!");
		}
		return TypeConstants.valueOf("U_" + this.name());
	}
}
