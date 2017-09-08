//Ashutosh Sharma 2016231

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Game extends Coordinate {
	private static int kc, iter;
	static Coordinate qn;
	static Knight knight[];

	Game() {
		kc = 0;
		iter = 0;
	}

	public static void main(String[] ar) throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the number of knights");
		kc = sc.nextInt();
		knight = new Knight[kc];
		System.out.println("Enter the number of iterations");
		iter = sc.nextInt();
		System.out.println("Enter the coordinates​​ of​ Queen​​ x​ and​​ y​");
		qn = new Coordinate(sc.nextInt(), sc.nextInt());
		for (int i = 0; i < kc; i++) {
			String filename = "C:\\Users\\Ashutosh Sharma\\eclipse-workspace\\Lab\\src\\" + (i + 1) + ".txt";
			try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
				String name = br.readLine();
				String coord = br.readLine();
				int x = coord.charAt(0) - 48;
				int y = coord.charAt(2) - 48;
				int size = Integer.parseInt(br.readLine());
				Stack st = new Stack(size);
				for (int j = 0; j < size; j++) {
					st.push(br.readLine());
				}
				knight[i] = new Knight();
				knight[i] = new Knight(name, x, y, st);
			}
		}
		Knight temp;
		for (int i = 0; i < kc; i++) {
			for (int j = 0; j < kc - 1; j++) {
				if ((knight[j].name.compareTo(knight[j + 1].name)) > 0) {
					temp = knight[j];
					knight[j] = knight[j + 1];
					knight[j + 1] = temp;
				}
			}
		}
		PrintWriter pw = new PrintWriter("C:\\Users\\Ashutosh Sharma\\eclipse-workspace\\Lab\\src\\output.txt",
				"UTF-8");
		boolean b2 = true;
		for (int i = 0; i < iter & b2; i++) {
			for (int j = 0; j < kc & b2; j++) {
				if (knight[j].state == 1) {
					try {
						pw.println((i + 1) + " " + knight[j].name + " " + knight[j].posx + " " + knight[j].posy);
						if (knight[j].box.len > 0) {
							String s = knight[j].box.pop();
							if (s.substring(0, 2).equalsIgnoreCase("Co") && knight[j].state == 1) {
								int n = s.indexOf(" ");
								s = s.substring(n + 1);
								int x = s.charAt(0) - 48;
								int y = s.charAt(2) - 48;
								knight[j].posx = x;
								knight[j].posy = y;
								boolean b1 = true;
								if (knight[j].posx == qn.posx && knight[j].posy == qn.posy) {
									QueenFoundException e = new QueenFoundException();
									pw.println(e.toString());
									b2 = false;
									throw e;
								}
								if (b2 == true) {
									for (int k = 0; k < kc; k++) {
										if ((knight[j].posx == knight[k].posx) && (knight[j].posy == knight[k].posy)
												&& k != j) {
											OverlapException e = new OverlapException(knight[k].name);
											pw.println("\n" + e.toString());
											knight[k].remove();
											b1 = false;
											throw e;
										}
									}
								}
								if (b1 && b2) {
									pw.println("\nNo exception " + x + " " + y);
								}
							} else {
								int n = s.indexOf(" ");
								s = s.substring(n + 1);
								NonCoordinateException e = new NonCoordinateException(s);
								pw.println("\n" + e.toString());
								throw e;
							}
						} else {
								StackEmptyException e = new StackEmptyException();
								pw.println("\n" + e.toString());
								knight[j].remove();
								throw e;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}pw.close();sc.close();
}
}

class Knight extends Coordinate {
	String name;
	int posx, posy, state;
	Stack box;

	Knight() {
		name = "";
		posx = 0;
		posy = 0;
		state = 1;
	}

	Knight(String name, int posx, int posy, Stack box) {
		this.name = name;
		this.posx = posx;
		this.posy = posy;
		this.box = box;
		this.state = 1;
	}

	void remove() {
		state = 0;
	}
}

class Coordinate {
	int posx, posy;

	Coordinate() {
		posx = 0;
		posy = 0;
	}

	Coordinate(int posx, int posy) {
		this.posx = posx;
		this.posy = posy;
	}

	void setCoordinate(int a, int b) {
		this.posx = a;
		this.posy = b;
	}
}

// ------------Stack implementation------------//
class Stack {
	String box[];
	int len, size, top;

	Stack() {
		len = 0;
		size = 0;
		top = -1;
	}

	Stack(int size) {
		len = 0;
		this.size = size;
		box = new String[size];
		top = -1;
	}

	boolean isEmpty() {
		return (top < 0) ? true : false;
	}

	void push(String e) {
		box[++top] = e;
		len++;
	}

	String pop() throws StackEmptyException {
		if (isEmpty()) {
			throw new StackEmptyException();
		}
		len--;
		return box[top--];
	}
}

// ------------Exception Classes-------------//

class NonCoordinateException extends Exception {
	NonCoordinateException(String s) {
		super("Not a coordinate Exception " + s);
	}
}

class StackEmptyException extends Exception {
	StackEmptyException() {
		super("Stack Empty Exception");
	}
}

class OverlapException extends Exception {
	OverlapException(String s) {
		super("Knights Overlap Exception " + s);
	}
}

class QueenFoundException extends Exception {
	QueenFoundException() {
		super("Queen has been found. Abort!");
	}
}
