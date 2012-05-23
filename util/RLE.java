package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RLE {
	
	static public int[][] read(String file) {
		Scanner in;
		try {
			in = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		String line = in.nextLine();
		for(; line.charAt(0) == '#'; line = in.nextLine());
		
		Pattern pattern = Pattern.compile("^x = ([0-9]+), y = ([0-9]+), rule = (.+)$");
		Matcher matcher = pattern.matcher(line);
		
		if(!matcher.matches()) {
			return null;
		}
		
		int n = Integer.parseInt(matcher.group(1));
		int m = Integer.parseInt(matcher.group(2));
		@SuppressWarnings("unused")
		String rule = matcher.group(3);
		
		int[][] t = new int[m][n];
		int buffer = 0;
		int x=0, y=0;
		
		do {
			line = in.nextLine();
			for(int j=0; j<line.length(); j++) {
				char c = line.charAt(j);
				if(Character.isDigit(c)) {
					buffer = 10*buffer + (int) (c - '0');
				}
				else if(c == 'o') {
					Arrays.fill(t[y], x, Math.min(n, x+Math.max(1, buffer)), 1);
					x += Math.max(1, buffer);
					buffer = 0;
				}
				else if(c == 'b') {
					x += Math.max(1, buffer);
					buffer = 0;
				}
				else if(c == '$') {
					y += Math.max(1, buffer);
					x = 0;
					buffer = 0;
				}
				else if(c == '!')
					return t;
			}
		} while(in.hasNext());
		
		return null;
		
	}
	
	static public void write(String file, int[][] tab) {
		try {
			BufferedWriter out;
			out = new BufferedWriter(new FileWriter(file));
			
			String header = "x = " + tab.length + ", y = " + tab[0].length + ", rule = B3/S23";
			out.write(header);
			out.newLine();
			
			int lines = 0;
			for(int i=0; i<tab.length; i++) {
				int buffer = 0;
				int last = -1;
				for(int j=0; j<tab[i].length; j++) {
					if(tab[i][j] == 1) {
						
						if(lines > 0) {
							if(lines > 1)
								out.write(Integer.toString(lines));
							out.write('$');
							lines = 0;
						}
						
						if(last == 0) {
							if(buffer > 1)
								out.write(Integer.toString(buffer));
							out.write('b');
							buffer = 0;
						}
						
						last = 1;
						buffer++;
					} else {
						if(last == 1) {
							if(buffer > 1)
								out.write(Integer.toString(buffer));
							out.write('o');
							buffer = 0;
						}
						last = 0;
						buffer++;
					}
				}
				if(last == 1) {
					if(buffer > 1)
						out.write(Integer.toString(buffer));
					out.write('o');
					buffer = 0;					
				}
				lines++;
			}

			if(lines > 0) {
				if(lines > 1)
					out.write(Integer.toString(lines));
				out.write('$');
				lines = 0;
			}
			
			out.write('!');
			out.close();
		} catch (IOException e) {
			System.out.println("Couldn't write the file "+file+".");
		} finally {
		}
	}
	
}
