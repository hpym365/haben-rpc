package com.haben.hrpc.protocol;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 03:08
 * @Version: 1.0
 **/
public class LengthField {

	public static final int MAX_FRAME_LENGTH = 65535;
	public static final int LENGTH_FIELD_OFFSET = 0;
	public static final int LENGTH_FIELD_LENGTH = 2;
	public static final int LENGTH_ADJUSTMENT  = 0;
	public static final int INITIAL_BYTES_TO_STRIP = 2;

}
