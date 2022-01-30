package de.felixeckert.degration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
	public static void main(String[] args) throws IOException {
		System.out.println("Degration - Simple (Probably Shitty) Implementation by Felix Eckert //\n");

		String pathRaw = "";
		for (String s : args) pathRaw += s;

		Path path = Paths.get(new File(pathRaw).getAbsolutePath());
		String source = new String(Files.readAllBytes(path));

		// Compile
		byte[] bytecode = Compiler.compile(source);

		// Init Tape
		LinkedList<Byte> TAPE = new LinkedList<Byte>();
		for (byte b : bytecode) TAPE.add(b); // Add Bytecode to tape

		// Add Starting Cell
		TAPE.add((byte) 0);
		int pointer = TAPE.size()-1;

		Scanner input = new Scanner(System.in);
		int instExecCount = 0; // Counts command executions

		for (int i = 0; i < bytecode.length; i++) {
			if (instExecCount == 8) { // Tape Degration
				instExecCount = 0;
				TAPE = degradeTape(TAPE);
			}

			byte instruction = TAPE.get(i);

			switch (instruction) {
				case 0x01: // P+
					pointer += 1;
					if (pointer > TAPE.size()) { // Expand tape to Fit
						int initialSize = TAPE.size();
						for (int j = 0; j < pointer - initialSize; j++) {
							TAPE.add((byte) 0x00);
						}
					}
					instExecCount++;
					break;
				case 0x02: // P-
					pointer = pointer == 0 ? TAPE.size()-1 : pointer-1;
					instExecCount++;
					break;
				case 0x03: // V+
					TAPE.set(pointer, (byte) (TAPE.get(pointer)+1));
					instExecCount++;
					break;
				case 0x04: // V-
					TAPE.set(pointer, (byte) (TAPE.get(pointer)-1));
					instExecCount++;
					break;
				case 0x05: // [
					instExecCount++;
					break;
				case 0x06: // ]
					instExecCount++;
					if (TAPE.get(pointer) != TAPE.get(i-1)) {
						for (int j = i; j >= 0; j--) {
							if (TAPE.get(j) == 0x05)  {i = j; break;}
							i = 0;
						}
					}
					break;
				case 0x07: // *
					System.out.print((char) ((byte) TAPE.get(pointer)));
					instExecCount++;
					break;
				case 0x08: // ?
					char[] in = input.nextLine().toCharArray();
					for (char c : in) {
						TAPE.set(i, (byte) c);
						i++;
					}
					instExecCount++;
					break;
				case 0x09: // 1
					System.exit(0);
					instExecCount++;
				case 0x0a: // Number Start
					i++;
					break;
			}
		}
		System.out.println();
	}

	public static LinkedList<Byte> degradeTape(LinkedList<Byte> list)  {
		int cellAmount = list.size()/16;               // Calculate Cell Amount
		cellAmount = cellAmount == 0 ? 2 : cellAmount;
		int[] cells = new int[cellAmount];

		// Select Cells
		for (int i = 0; i < cells.length; i++) {
			int cell = 0;
			while (true) { // Select Cell (And Ensure non Duplicate)
				cell = ThreadLocalRandom.current().nextInt(list.size());
				boolean duplicated = false;
				for (int c : cells) {
					duplicated = c == cell;
				}
				if (!duplicated) break;
			}
			cells[i] = cell;
		}

		// Degrade Cells
		for (int c : cells) {
			byte oldVal = list.get(c);
			list.set(c, (byte) (oldVal+ThreadLocalRandom.current().nextInt(-4,4)));
		}

		return list;
	}
}
