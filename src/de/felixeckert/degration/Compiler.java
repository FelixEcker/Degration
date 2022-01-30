package de.felixeckert.degration;

import java.util.LinkedList;

public class Compiler {
	public static byte[] compile(String source) {
		char[] tokens = source.toCharArray();
		LinkedList<Byte> bytecode = new LinkedList<>();
		String numberBuffer = "";

		for (int i = 0; i < tokens.length; i++) {
			char token = tokens[i];

			if (numberBuffer.length() == 3 || // Number Buffer at max length
					// Number Terminated
					(numberBuffer.length() > 0 && !(""+token).replaceAll("[0-9]", "").matches(""))) {
				bytecode.add((byte) 0x0a);                // Number Flag

				// Make number in bounds
				int num = Integer.parseInt(numberBuffer);
				num = num > 255 ? 255 : num < 0 ? 0 : num;
				bytecode.add((byte) num);
				numberBuffer = ""; // Clear Number Buffer
			}

			char mod;
			switch (token) {
				case 'P':
					mod = tokens[i+1];
					if (mod == '+') {
						bytecode.add((byte) 0x01);
					} else if (mod == '-') {
						bytecode.add((byte) 0x02);
					} else {
						System.err.printf("Error at Character %s: Invalid Modifier!\n", i+2);
						System.exit(0);
					}
					i++;
					break;
				case 'V':
					mod = tokens[i+1];
					if (mod == '+') {
						bytecode.add((byte) 0x03);
					} else if (mod == '-') {
						bytecode.add((byte) 0x04);
					} else {
						System.err.printf("Error at Character %s: Invalid Modifier!\n", i+2);
						System.exit(0);
					}
					i++;
					break;
				case '[':
					bytecode.add((byte) 0x05);
					break;
				case ']':
					bytecode.add((byte) 0x06);
					break;
				case '*':
					bytecode.add((byte) 0x07);
					break;
				case '?':
					bytecode.add((byte) 0x08);
					break;
				case '!':
					bytecode.add((byte) 0x09);
					break;
				default:
					if ((""+token).replaceAll("[0-9]", "").matches("")) {
						numberBuffer += token;
					}
					break;
			}
		}

		// Convert to ByteArray
		byte[] ret = new byte[bytecode.size()];
		for (int i = 0; i < ret.length; i++) ret[i] = bytecode.get(i);

		return ret;
	}
}
